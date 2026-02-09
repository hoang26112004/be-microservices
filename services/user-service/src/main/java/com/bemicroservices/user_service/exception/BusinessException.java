package com.bemicroservices.user_service.exception;




public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }
}
