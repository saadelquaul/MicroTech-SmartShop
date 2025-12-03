package com.SmartShop.MicroTech_SmartShop.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private boolean isDeleted;
}
