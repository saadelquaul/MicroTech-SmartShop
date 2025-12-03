package com.SmartShop.MicroTech_SmartShop.dto.request;

import com.SmartShop.MicroTech_SmartShop.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class PaymentRequestDto {

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    private String reference;
}
