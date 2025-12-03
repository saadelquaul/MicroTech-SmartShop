package com.SmartShop.MicroTech_SmartShop.controller;


import com.SmartShop.MicroTech_SmartShop.dto.request.ProductRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ProductResponseDto;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private void checkLogin(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) {
            throw new BusinessException("Unauthorized: Please login");
        }
    }

    private void checkAdmin(HttpSession session) {
        checkLogin(session);
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        if (role != UserRole.ADMIN) {
            throw new BusinessException("Access Denied: Admin privileges required");
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto dto,
            HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto dto,
            HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
