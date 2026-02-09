package com.bemicroservices.user_service.mapper;


import com.bemicroservices.user_service.dto.request.UserRequest;
import com.bemicroservices.user_service.dto.response.UserResponse;
import com.bemicroservices.user_service.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(UserRequest request, @MappingTarget User user);
}
