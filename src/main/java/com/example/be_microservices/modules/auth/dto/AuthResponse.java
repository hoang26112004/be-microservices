package com.example.be_microservices.modules.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    /** Thời gian hết hạn access token (giây) */
    private long expiresIn;
}
