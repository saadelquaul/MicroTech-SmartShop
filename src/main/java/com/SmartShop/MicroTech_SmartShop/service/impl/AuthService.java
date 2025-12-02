package com.SmartShop.MicroTech_SmartShop.service.impl;


import com.SmartShop.MicroTech_SmartShop.dto.request.LoginRequest;
import com.SmartShop.MicroTech_SmartShop.entity.User;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.repository.UserRepository;
import com.SmartShop.MicroTech_SmartShop.utils.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User login(LoginRequest request, HttpSession session) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!PasswordUtil.check(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        session.setAttribute("USER_ID", user.getId());
        session.setAttribute("USER_ROLE", user.getRole());

        if (user.getRole() == UserRole.CLIENT && user.getClient() != null) {
            session.setAttribute("CLIENT_ID", user.getClient().getId());
        }

        return user;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
