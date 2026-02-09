package com.bemicroservices.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Phản hồi thông tin địa chỉ")
public record AddressResponse(
        @Schema(description = "ID địa chỉ", example = "1")
        Integer id,

        @Schema(description = "Số điện thoại liên hệ", example = "0987654321")
        String phoneNumber,

        @Schema(description = "Đường phố", example = "123 Đường Láng")
        String street,

        @Schema(description = "Thành phố", example = "Hà Nội")
        String city,

        @Schema(description = "Tiểu bang", example = "")
        String state,

        @Schema(description = "Quốc gia", example = "Việt Nam")
        String country,

        @Schema(description = "Mã bưu điện", example = "100000")
        String zipCode,

        @Schema(description = "Mô tả địa chỉ", example = "Gần chợ đêm")
        String description,

        @Schema(description = "Địa chỉ mặc định", example = "true")
        boolean isDefault
) {
}
