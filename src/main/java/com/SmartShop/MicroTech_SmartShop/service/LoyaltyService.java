package com.SmartShop.MicroTech_SmartShop.service;


import com.SmartShop.MicroTech_SmartShop.enums.CustomerTier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoyaltyService {

    public BigDecimal getLoyaltyDiscountRate(CustomerTier tier, BigDecimal subTotal) {
        if (tier == null || subTotal == null) return BigDecimal.ZERO;

        switch (tier) {
            case SILVER:
                if (subTotal.compareTo(new BigDecimal("500")) > 0) return new BigDecimal("0.05");
                break;
            case GOLD:
                if (subTotal.compareTo(new BigDecimal("800")) > 0) return new BigDecimal("0.10");
                break;
            case PLATINUM:
                if (subTotal.compareTo(new BigDecimal("1200")) > 0) return new BigDecimal("0.15");
                break;
            case BASIC:
            default:
                return BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }
}
