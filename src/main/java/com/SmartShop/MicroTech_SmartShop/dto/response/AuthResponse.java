package com.SmartShop.MicroTech_SmartShop.dto.response;



import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthResponse {

    private Long userId;
    private String username;
    private UserRole role;
    private String message;

}
