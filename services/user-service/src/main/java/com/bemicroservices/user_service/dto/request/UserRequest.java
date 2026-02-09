package com.bemicroservices.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Schema(description = "Yêu cầu tạo người dùng")
public record UserRequest(

        @Schema(description = "Số điện thoại", example = "0987654321")
        @NotBlank(message = "Không được để trống số điện thoại")
        @Pattern(
                regexp = "^(\\+84|0)[1-9]\\d{8}$",
                message = "Số điện thoại không hợp lệ"
        )
        String phoneNumber,

        @Schema(description = "Giới tính (true = nam, false = nữ)")
        Boolean gender,

        @Schema(description = "Ngày sinh", example = "2000-01-01")
        @Past(message = "Ngày sinh phải ở quá khứ")
        LocalDate dateOfBirth,

        @Schema(description = "Địa chỉ mặc định")
        @Valid
        AddressRequest address

) {}
