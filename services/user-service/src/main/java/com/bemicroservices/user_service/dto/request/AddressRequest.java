package com.bemicroservices.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Yêu cầu tạo địa chỉ")
public record AddressRequest(

        @Schema(description = "SĐT liên hệ")
        @Pattern(regexp = "^0[1-9]\\d{8}$")
        String phoneNumber,

        @Schema(description = "Tên đường")
        @NotBlank
        String street,

        @Schema(description = "Thành phố")
        @NotBlank
        String city,

        @Schema(description = "Tỉnh / Bang")
        String state,

        @Schema(description = "Quốc gia")
        @NotBlank
        String country,

        @Schema(description = "Mã bưu điện")
        String zipCode,

        @Schema(description = "Mô tả")
        String description,

        @Schema(description = "Là địa chỉ mặc định")
        boolean isDefault

) {}
