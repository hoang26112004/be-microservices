package com.bemicroservices.user_service.service.impl;

import com.bemicroservices.user_service.exception.BusinessException;
import com.bemicroservices.user_service.mapper.AddressMapper;
import com.bemicroservices.user_service.mapper.UserMapper;
import com.bemicroservices.user_service.model.entity.Address;
import com.bemicroservices.user_service.model.entity.User;
import com.bemicroservices.user_service.model.global.GlobalResponse;
import com.bemicroservices.user_service.model.global.Status;
import com.bemicroservices.user_service.dto.request.UserRequest;
import com.bemicroservices.user_service.dto.response.AddressResponse;
import com.bemicroservices.user_service.dto.response.UserResponse;
import com.bemicroservices.user_service.repository.AddressRepository;
import com.bemicroservices.user_service.repository.UserRepository;
import com.bemicroservices.user_service.service.UserService;
import com.bemicroservices.user_service.util.CloudinaryUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    AddressRepository addressRepository;

    UserMapper userMapper;
    AddressMapper addressMapper;

    CloudinaryUtil cloudinaryUtil;

    @Override
    @Transactional
    public GlobalResponse<UserResponse> createUser(UserRequest request, Jwt jwt) {
        String userId = jwt.getSubject();
        if (userRepository.existsById(userId)) {
            throw new BusinessException("Người dùng với ID " + userId + " đã tồn tại");
        }

        User user = userMapper.toUser(request);
        user.setId(userId);

        Address address = null;
        if (request.address() != null) {
            address = addressMapper.toAddress(request.address());
            address.setUser(user);
            address.setIsDefault(true);
            address = addressRepository.save(address);
        }

        user = userRepository.save(user);

        UserResponse response = new UserResponse(
                (String) jwt.getClaims().get("name"),
                user.getPhoneNumber(),
                user.getAvatarUrl(),
                user.getGender(),
                user.getDateOfBirth(),
                address != null ? List.of(addressMapper.toAddressResponse(address)) : null
        );

        return new GlobalResponse<>(
                Status.SUCCESS,
                response
        );
    }

    @Override
    public GlobalResponse<UserResponse> findCurrentUser(Jwt jwt) {
        String userId = jwt.getSubject();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthorizationDeniedException("Không tìm thấy người dùng hiện tại"));

        List<Address> addresses = addressRepository.findByUser(user);
        List<AddressResponse> addressResponses = addresses.stream()
                .map(addressMapper::toAddressResponse)
                .toList();

        UserResponse response = new UserResponse(
                (String) jwt.getClaims().get("name"),
                user.getPhoneNumber(),
                user.getAvatarUrl(),
                user.getGender(),
                user.getDateOfBirth(),
                addressResponses
        );

        return new GlobalResponse<>(
                Status.SUCCESS,
                response
        );
    }

    @Override
    @Transactional
    public GlobalResponse<UserResponse> updateUser(Integer addressId, UserRequest request, Jwt jwt) {
        String userId = jwt.getSubject();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng hiện tại"));

        userMapper.updateUser(request, user);

        Address updatedAddress = null;
        if (addressId != null && request.address() != null) {
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy địa chỉ với ID: " + addressId));
            if (!address.getUser().getId().equals(userId)) {
                throw new BusinessException("Địa chỉ này không thuộc về người dùng hiện tại");
            }
            addressMapper.updateAddress(request.address(), address);
            if (request.address().isDefault()) {
                addressRepository.findByUser(user).stream()
                        .filter(a -> !a.getId().equals(addressId))
                        .forEach(a -> {
                            a.setIsDefault(false);
                            addressRepository.save(a);
                        });
            }
            updatedAddress = addressRepository.save(address);
        }

        user = userRepository.save(user);

        List<Address> addresses = addressRepository.findByUser(user);
        List<AddressResponse> addressResponses = addresses.stream()
                .map(addressMapper::toAddressResponse)
                .toList();

        UserResponse response = new UserResponse(
                (String) jwt.getClaims().get("name"),
                user.getPhoneNumber(),
                user.getAvatarUrl(),
                user.getGender(),
                user.getDateOfBirth(),
                addressResponses
        );

        return new GlobalResponse<>(
                Status.SUCCESS,
                response
        );
    }

    @Override
    @Transactional
    public GlobalResponse<?> uploadAvatar(Jwt jwt, MultipartFile avatar) {
        try {
            String userId = jwt.getSubject();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userId));

            if (avatar == null || avatar.isEmpty()) {
                throw new BusinessException("File ảnh không được để trống");
            }

            if (user.getAvatarUrl() != null) {
                try {
                    cloudinaryUtil.remove(user.getAvatarUrl());
                    log.info("Đã xóa ảnh avatar cũ cho user {}: {}", userId, user.getAvatarUrl());
                } catch (IOException e) {
                    log.error("Không thể xóa ảnh avatar cũ: {}", e.getMessage(), e);
                    throw new BusinessException("Không thể xóa ảnh avatar cũ: " + e.getMessage());
                }
            }

            String avatarUrl;
            try {
                avatarUrl = cloudinaryUtil.upload(avatar);
                user.setAvatarUrl(avatarUrl);
                log.info("Đã upload ảnh avatar mới cho user {}: {}", userId, avatarUrl);
            } catch (IOException e) {
                log.error("Không thể upload ảnh avatar: {}", e.getMessage(), e);
                throw new BusinessException("Không thể upload ảnh avatar: " + e.getMessage());
            }

            user = userRepository.save(user);

            List<Address> addresses = addressRepository.findByUser(user);
            List<AddressResponse> addressResponses = addresses.stream()
                    .map(addressMapper::toAddressResponse)
                    .toList();

            UserResponse response = new UserResponse(
                    (String) jwt.getClaims().get("name"),
                    user.getPhoneNumber(),
                    user.getAvatarUrl(),
                    user.getGender(),
                    user.getDateOfBirth(),
                    addressResponses
            );

            return new GlobalResponse<>(
                    Status.SUCCESS,
                    response
            );
        } catch (EntityNotFoundException | BusinessException ex) {
            log.error("Lỗi khi upload avatar: {}", ex.getMessage(), ex);
            return new GlobalResponse<>(Status.ERROR, ex.getMessage());
        } catch (Exception ex) {
            log.error("Lỗi không xác định khi upload avatar: {}", ex.getMessage(), ex);
            return new GlobalResponse<>(Status.ERROR, "Không thể upload avatar");
        }
    }
}
