package com.SmartShop.MicroTech_SmartShop.controller;


import com.SmartShop.MicroTech_SmartShop.dto.request.PaymentRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.PaymentResponseDto;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping
    public ResponseEntity<PaymentResponseDto> recordPayment(
            @Valid @RequestBody PaymentRequestDto dto,
            HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.recordPayment(dto));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrder(
            @PathVariable Long orderId,
            HttpSession session) {
        checkLogin(session);

        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId));
    }

    @PutMapping("/confirm/{paymentId}")
    public ResponseEntity<PaymentResponseDto> confirmPayment(@PathVariable Long paymentId, HttpSession session)
    {
        checkAdmin(session);
        return ResponseEntity.ok(paymentService.confirmPayment(paymentId));
    }

    @PutMapping("/reject/{paymentId}")
    public ResponseEntity<PaymentResponseDto> rejectPayment(@PathVariable Long paymentId, HttpSession session)
    {
        checkAdmin(session);
        return ResponseEntity.ok(paymentService.rejectPayment(paymentId));
    }

    private void checkLogin(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) throw new BusinessException("Unauthorized");
    }

    private void checkAdmin(HttpSession session) {
        checkLogin(session);
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        if (role != UserRole.ADMIN) throw new BusinessException("Admin only");
    }
}
