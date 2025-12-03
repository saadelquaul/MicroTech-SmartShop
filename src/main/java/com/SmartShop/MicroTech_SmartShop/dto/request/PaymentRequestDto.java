package com.SmartShop.MicroTech_SmartShop.dto.request;

import com.SmartShop.MicroTech_SmartShop.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class PaymentRequestDto {

    private  Long id;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private Integer paymentNumber;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    private LocalDateTime paymentDate;
    private LocalDateTime encashmentDate;

    @NotBlank(message = "Payment Reference is required")
    private String reference;
}
