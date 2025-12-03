package com.SmartShop.MicroTech_SmartShop.mapper;


import com.SmartShop.MicroTech_SmartShop.dto.request.ClientRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.request.LoginRequest;
import com.SmartShop.MicroTech_SmartShop.dto.response.AuthResponse;
import com.SmartShop.MicroTech_SmartShop.entity.User;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.utils.PasswordUtil;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AuthResponse toResponse (User user) {
        if (user == null) return null;

        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public User toEntity(ClientRequestDto clientRequestDto) {
        if (clientRequestDto == null) return null ;

        return User.builder()
                .username(clientRequestDto.getUsername())
                .password(PasswordUtil.hash(clientRequestDto.getPassword()))
                .role(UserRole.CLIENT)
                .build();
    }
}
