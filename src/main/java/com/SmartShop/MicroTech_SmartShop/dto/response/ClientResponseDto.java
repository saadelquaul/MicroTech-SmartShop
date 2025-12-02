package com.SmartShop.MicroTech_SmartShop.dto.response;

import com.SmartShop.MicroTech_SmartShop.enums.CustomerTier;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientResponseDto {

    private Long id;
    private String name;
    private String email;
    private CustomerTier tier;
    private BigDecimal totalSpent;
    private Integer totalOrders;
}
