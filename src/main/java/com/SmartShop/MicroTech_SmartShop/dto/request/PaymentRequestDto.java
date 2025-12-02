package com.SmartShop.MicroTech_SmartShop.dto.request;

import com.SmartShop.MicroTech_SmartShop.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PaymentRequestDto {

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    private String reference;
}
