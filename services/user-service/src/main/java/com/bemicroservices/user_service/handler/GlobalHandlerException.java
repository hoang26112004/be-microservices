package com.bemicroservices.user_service.handler;


import com.bemicroservices.user_service.model.global.GlobalResponse;
import com.bemicroservices.user_service.model.global.Status;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Log4j2
public class GlobalHandlerException {

    /**
     * Bắt EntityNotFoundException
     *
     * 👉 Exception này thường được throw ở Service
     *    ví dụ:
     *    userRepository.findById(id)
     *        .orElseThrow(() -> new EntityNotFoundException("User không tồn tại"));
     *
     * 👉 Khi exception xảy ra:
     *    - Controller KHÔNG bị crash
     *    - Request được trả về response chuẩn GlobalResponse
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GlobalResponse<String>> handlerEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new GlobalResponse<>(
                        Status.ERROR,     // Đánh dấu response là lỗi
                        ex.getMessage()   // Trả message cho FE
                ));
    }

    /**
     * Bắt AuthorizationDeniedException
     *
     * 👉 Xảy ra khi:
     *    - User đã đăng nhập (có JWT)
     *    - Nhưng KHÔNG có quyền truy cập API
     *
     * 👉 Thường liên quan tới Spring Security
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<GlobalResponse<String>> handlerAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new GlobalResponse<>(
                        Status.ERROR,
                        ex.getMessage()
                ));
    }

    /**
     * Bắt lỗi validate @RequestBody
     *
     * 👉 Xảy ra khi:
     *    - DTO có @NotNull, @NotBlank, @Size...
     *    - Body gửi lên không hợp lệ
     *
     * 👉 Trả về Map:
     *    field -> message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Lấy danh sách lỗi theo từng field
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        log.error("Error in MethodArgumentNotValidException");

        return ResponseEntity.badRequest().body(new GlobalResponse<>(
                Status.ERROR,
                errors
        ));
    }

    /**
     * Bắt lỗi validate cho:
     *    - @PathVariable
     *    - @RequestParam
     *
     * 👉 Ví dụ:
     *    @Min(1), @NotNull
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                errors.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )
        );

        log.error("Error in ConstraintViolationException");

        return ResponseEntity.badRequest().body(new GlobalResponse<>(
                Status.ERROR,
                errors
        ));
    }

    /**
     * Bắt lỗi bind dữ liệu
     *
     * 👉 Xảy ra khi:
     *    - Dùng @ModelAttribute
     *    - Form-data
     *    - Query param không map được
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<GlobalResponse<Map<String, String>>> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        log.error("Error in BindException");

        return ResponseEntity.badRequest().body(new GlobalResponse<>(
                Status.ERROR,
                errors
        ));
    }
}
