package com.example.be_microservices.comom.response;

public record GlobalResponse<T>(
    Status status,
    T data
)
{
    public static <T> GlobalResponse<T> ok(T data) {
        return new GlobalResponse<>(Status.SUCCESS, data);
    }

    public static <T> GlobalResponse<T> error(T data) {
        return new GlobalResponse<>(Status.ERROR, data);
    }
}
