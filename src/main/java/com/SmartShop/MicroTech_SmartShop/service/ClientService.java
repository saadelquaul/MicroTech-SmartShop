package com.SmartShop.MicroTech_SmartShop.service;


import com.SmartShop.MicroTech_SmartShop.dto.request.ClientRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ClientResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Client;
import com.SmartShop.MicroTech_SmartShop.entity.User;
import com.SmartShop.MicroTech_SmartShop.enums.CustomerTier;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.MicroTech_SmartShop.mapper.ClientMapper;
import com.SmartShop.MicroTech_SmartShop.repository.ClientRepository;
import com.SmartShop.MicroTech_SmartShop.repository.UserRepository;
import com.SmartShop.MicroTech_SmartShop.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponseDto createClient(ClientRequestDto dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email already exists");
        }


        User user = User.builder()
                .username(dto.getUsername())
                .password(PasswordUtil.hash(dto.getPassword()))
                .role(UserRole.CLIENT)
                .build();
        userRepository.save(user);


        Client client = clientMapper.toEntity(dto);
        client.setUser(user);
        client.setTier(CustomerTier.BASIC);
        client.setTotalSpent(BigDecimal.ZERO);
        client.setTotalOrders(0);

        Client savedClient = clientRepository.save(client);

        return clientMapper.toResponse(savedClient);
    }

    @Transactional
    public ClientResponseDto updateClient(Long id, ClientRequestDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));


        client.setName(dto.getName());


        if (!client.getEmail().equals(dto.getEmail()) && clientRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email already taken");
        }
        client.setEmail(dto.getEmail());

        return clientMapper.toResponse(clientRepository.save(client));
    }

    
}
