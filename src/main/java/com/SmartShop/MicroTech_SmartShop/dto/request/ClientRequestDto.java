package com.SmartShop.MicroTech_SmartShop.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ClientRequestDto {

    private Long id ;

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
