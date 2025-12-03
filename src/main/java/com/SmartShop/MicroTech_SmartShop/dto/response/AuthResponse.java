package com.SmartShop.MicroTech_SmartShop.dto.response;


import com.SmartShop.MicroTech_SmartShop.entity.User;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import lombok.Data;

@Data
public class AuthResponse {

    private Long userId;
    private String username;
    private UserRole role;
    private String message;

}
