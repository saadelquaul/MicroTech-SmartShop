package com.SmartShop.MicroTech_SmartShop.mapper;


import com.SmartShop.MicroTech_SmartShop.dto.response.OrderItemResponseDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.OrderResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Order;
import com.SmartShop.MicroTech_SmartShop.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponseDto toResponse (Order order)
    {
        if (order == null) return null;

        List<OrderItemResponseDto> items = order.getOrderItems() == null ? Collections.emptyList() :
                order.getOrderItems().stream().map(this::toItemDto)
                        .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .id(order.getId())
                .dateCreation(order.getDateCreation())
                .clientName(order.getClient().getName() == null ? null : order.getClient().getName())
                .status(order.getStatus())
                .subTotal(order.getSubTotal())
                .discountAmount(order.getDiscountAmount())
                .taxAmount(order.getTaxAmount())
                .totalAmount(order.getTotalAmount())
                .remainingAmount(order.getRemainingAmount())
                .items(items)
                .clientId(order.getClient() == null ? null : order.getClient().getId())
                .build();
    }


    private OrderItemResponseDto toItemDto(OrderItem item) {
        if (item == null) return null;


        return OrderItemResponseDto.builder()
                .productName(item.getProduct() == null ? null : item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalLine(item.getTotalPrice())
                .build();
    }
}
