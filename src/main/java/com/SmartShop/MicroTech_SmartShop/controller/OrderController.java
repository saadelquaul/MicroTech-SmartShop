package com.SmartShop.MicroTech_SmartShop.controller;

import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;




    private void checkLogin(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) throw new BusinessException("Unauthorized");
    }

    private void checkAdmin(HttpSession session) {
        checkLogin(session);
        if (session.getAttribute("USER_ROLE") != UserRole.ADMIN) throw new BusinessException("Admin only");
    }
}
