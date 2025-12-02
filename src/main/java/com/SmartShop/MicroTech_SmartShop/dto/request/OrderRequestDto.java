package com.SmartShop.MicroTech_SmartShop.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderRequestDto {

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotEmpty(message = "Order must contain at least one product")
    private List<OrderItemRequestDto> items;

    @Pattern(regexp = "^PROMO-[A-Z0-9]{4}$", message = "Promo code is not valid or it does not follow the correct format PROMO-XXXX")
    private String promoCode;
}
