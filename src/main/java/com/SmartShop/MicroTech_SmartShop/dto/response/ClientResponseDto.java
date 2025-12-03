package com.SmartShop.MicroTech_SmartShop.dto.response;

import com.SmartShop.MicroTech_SmartShop.enums.CustomerTier;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class ClientResponseDto {

    private Long id;
    private String name;
    private String email;
    private CustomerTier tier;
    private BigDecimal totalSpent;
    private Integer totalOrders;
}
