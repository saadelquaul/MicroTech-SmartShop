package com.SmartShop.MicroTech_SmartShop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class OrderItemResponseDto {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalLine;
}
