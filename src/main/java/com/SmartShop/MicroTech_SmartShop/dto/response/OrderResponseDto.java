package com.SmartShop.MicroTech_SmartShop.dto.response;

import com.SmartShop.MicroTech_SmartShop.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Setter
@Getter
public class OrderResponseDto {

    private Long id;
    private LocalDateTime dateCreation;
    private String clientName;
    private Long clientId;
    private OrderStatus status;
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal remainingAmount;
    private List<OrderItemResponseDto> items;

}
