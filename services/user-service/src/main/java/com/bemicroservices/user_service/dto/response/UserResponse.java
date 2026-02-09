package com.bemicroservices.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Phản hồi thông tin người dùng")
public record UserResponse(
        @Schema(description = "Tên đầy đủ của người dùng", example = "Nguyen Van A")
        String fullName,

        @Schema(description = "Số điện thoại người dùng", example = "+84987654321")
        String phoneNumber,

        @Schema(description = "URL ảnh đại diện", example = "https://res.cloudinary.com/demo/image/upload/v1234567890/avatar.jpg")
        String avatarUrl,

        @Schema(description = "Giới tính", example = "true")
        Boolean gender,

        @Schema(description = "Ngày sinh", example = "1990-01-01")
        LocalDate dateOfBirth,

        @Schema(description = "Danh sách địa chỉ của người dùng")
        List<AddressResponse> addresses
) {
}
