package com.SmartShop.MicroTech_SmartShop.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRequestDto {

    @NotBlank(message = "Client name is required")
    private String name;
    
    @NotBlank 
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Username is required for the client account")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
}
