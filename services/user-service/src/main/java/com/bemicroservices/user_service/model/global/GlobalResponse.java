package com.bemicroservices.user_service.model.global;

//👉 Cho frontend biết:
//
//request thành công hay thất bại
//
//KHÔNG phụ thuộc HTTP status
public record GlobalResponse<T>(
        Status status,
        T data
) {
}
