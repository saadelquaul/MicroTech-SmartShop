package com.SmartShop.MicroTech_SmartShop.service;


import com.SmartShop.MicroTech_SmartShop.entity.Client;
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

    public void updateClientTier(Client client) {
        int orders = client.getTotalOrders();
        BigDecimal spent = client.getTotalSpent();

        if (orders > 20 || spent.compareTo(new BigDecimal("15000")) > 0) {
            client.setTier(CustomerTier.PLATINUM);
        } else if (orders > 10 || spent.compareTo(new BigDecimal("5000")) > 0) {
            client.setTier(CustomerTier.GOLD);
        } else if (orders > 3 || spent.compareTo(new BigDecimal("1000")) > 0) {
            client.setTier(CustomerTier.SILVER);
        } else {
            client.setTier(CustomerTier.BASIC);
        }
    }
}
