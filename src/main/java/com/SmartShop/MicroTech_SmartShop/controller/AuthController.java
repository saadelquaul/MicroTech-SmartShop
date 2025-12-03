package com.SmartShop.MicroTech_SmartShop.controller;


import com.SmartShop.MicroTech_SmartShop.dto.request.LoginRequest;
import com.SmartShop.MicroTech_SmartShop.dto.response.AuthResponse;
import com.SmartShop.MicroTech_SmartShop.entity.User;
import com.SmartShop.MicroTech_SmartShop.service.impl.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        User user = authService.login(request, session);

        return ResponseEntity.ok(AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("Login successful")
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok("Logged out successfully");
    }
}
