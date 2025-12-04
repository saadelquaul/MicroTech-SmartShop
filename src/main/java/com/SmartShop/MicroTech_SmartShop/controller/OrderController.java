package com.SmartShop.MicroTech_SmartShop.controller;

import com.SmartShop.MicroTech_SmartShop.dto.request.OrderRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.OrderResponseDto;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto dto, HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
    }


    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDto> confirmOrder(@PathVariable Long id, HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id, HttpSession session) {
        checkAdmin(session);
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id, HttpSession session) {
        checkLogin(session);

        OrderResponseDto order = orderService.getOrderById(id);


        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        Long clientId = (Long) session.getAttribute("CLIENT_ID");

        if (role == UserRole.CLIENT) {
            if (clientId == null || !clientId.equals(order.getClientId())) {
                throw new BusinessException("Access Denied");
            }
        }

        return ResponseEntity.ok(order);
    }


    private void checkLogin(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) throw new BusinessException("Unauthorized");
    }

    private void checkAdmin(HttpSession session) {
        checkLogin(session);
        if (session.getAttribute("USER_ROLE") != UserRole.ADMIN) throw new BusinessException("Admin only");
    }
}
