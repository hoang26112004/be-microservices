package com.bemicroservices.user_service.controller;

import com.bemicroservices.user_service.constant.Endpoint;
import com.bemicroservices.user_service.dto.request.UserRequest;
import com.bemicroservices.user_service.dto.response.UserResponse;
import com.bemicroservices.user_service.model.global.GlobalResponse;
import com.bemicroservices.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Endpoint.User.PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    // OK ✔ Lombok @RequiredArgsConstructor sẽ inject

    @Operation(
            summary = "Tạo người dùng mới",
            description = "API này cho phép tạo người dùng mới dựa trên thông tin gửi lên. Người dùng cần phải xác thực."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tạo người dùng thành công",
                    content = @Content(
                            schema = @Schema(implementation = GlobalResponse.class)
                            // NOTE: Swagger chỉ biết là GlobalResponse,
                            // KHÔNG biết data bên trong là UserResponse (không sai, chỉ hiển thị chưa đẹp)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "401", description = "Người dùng chưa xác thực", content = @Content)
    })
    @PostMapping
    public ResponseEntity<GlobalResponse<UserResponse>> createUser(
            @RequestBody UserRequest request,
            // NOTE: thiếu @Valid → validation sẽ KHÔNG chạy
            // Nếu UserRequest có @NotBlank, @Email...
            // API vẫn chạy OK, chỉ là không auto validate
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(userService.createUser(request, jwt));
    }

    @Operation(
            summary = "Lấy thông tin người dùng hiện tại",
            description = "API này lấy thông tin người dùng hiện tại dựa trên token xác thực JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy thông tin thành công",
                    content = @Content(schema = @Schema(implementation = GlobalResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Người dùng chưa xác thực", content = @Content)
    })
    @GetMapping(Endpoint.User.ME)
    public ResponseEntity<GlobalResponse<UserResponse>> findCurrentUser(
            @AuthenticationPrincipal Jwt jwt
            // OK ✔ Spring Security tự inject JWT
    ) {
        return ResponseEntity.ok(userService.findCurrentUser(jwt));
    }

    @Operation(
            summary = "Cập nhật thông tin người dùng",
            description = "API này cho phép cập nhật thông tin người dùng hiện tại. Có thể truyền thêm ID của địa chỉ cần cập nhật."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật thông tin thành công",
                    content = @Content(schema = @Schema(implementation = GlobalResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "401", description = "Người dùng chưa xác thực", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ cần cập nhật", content = @Content)
    })
    @PutMapping
    public ResponseEntity<GlobalResponse<UserResponse>> updateCurrentUser(
            @RequestParam(name = "addressId", required = false) Integer addressId,
            // NOTE: dùng Integer là OK, chỉ là nếu DB dùng BIGINT thì nên đổi Long
            @RequestBody @Valid UserRequest request,
            // OK ✔ @Valid ở đây đã đúng
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(userService.updateUser(addressId, request, jwt));
    }

    @PutMapping(Endpoint.User.UPLOAD)
    public ResponseEntity<?> uploadAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "avatar") MultipartFile avatar
            // OK ✔ MultipartFile đúng cách
            // NOTE: ResponseEntity<?> không sai, chỉ là Swagger không rõ trả về gì
    ) {
        return ResponseEntity.ok(userService.uploadAvatar(jwt, avatar));
    }
}


