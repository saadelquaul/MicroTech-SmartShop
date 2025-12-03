package com.SmartShop.MicroTech_SmartShop.mapper;


import com.SmartShop.MicroTech_SmartShop.dto.response.OrderItemResponseDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.OrderResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Order;
import com.SmartShop.MicroTech_SmartShop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "clientName", source = "client.name")
    @Mapping(target = "items", source = "orderItems")
    OrderResponseDto toResponse(Order order);

    @Mapping(target = "productName", source = "product.name")
    OrderItemResponseDto toItemResponse(OrderItem item);
}
