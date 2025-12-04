package com.SmartShop.MicroTech_SmartShop.controller;


import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    private void checkLogin(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) throw new BusinessException("Unauthorized");
    }

    private void checkAdmin(HttpSession session) {
        checkLogin(session);
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        if (role != UserRole.ADMIN) throw new BusinessException("Admin only");
    }
}
