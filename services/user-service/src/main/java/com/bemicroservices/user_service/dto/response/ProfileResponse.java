package com.bemicroservices.user_service.dto.response;

import java.time.LocalDateTime;

public record ProfileResponse(
        Integer id,
        String avatar,
        boolean gender,
        LocalDateTime dateOfBirth
) {
}
