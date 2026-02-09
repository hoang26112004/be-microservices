package com.bemicroservices.user_service.service;

import com.bemicroservices.user_service.dto.request.UserRequest;
import com.bemicroservices.user_service.dto.response.UserResponse;
import com.bemicroservices.user_service.model.entity.User;
import com.bemicroservices.user_service.model.global.GlobalResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    GlobalResponse<UserResponse> createUser(UserRequest request, Jwt jwt);

    GlobalResponse<UserResponse> findCurrentUser(Jwt jwt);

    GlobalResponse<UserResponse> updateUser(Integer addressId, UserRequest request, Jwt jwt);

    GlobalResponse<?> uploadAvatar(Jwt jwt, MultipartFile avatar);
}

