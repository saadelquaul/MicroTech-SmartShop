package com.SmartShop.MicroTech_SmartShop.controller;

import com.SmartShop.MicroTech_SmartShop.dto.request.ClientRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ClientResponseDto;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.service.ClientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientRequestDto dto, HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getAllClients(HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable Long id, HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    


    private void checkAdmin(HttpSession session) {
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        if (role != UserRole.ADMIN) {
            throw new BusinessException("Access Denied: Admin privileges required");
        }
    }

}
