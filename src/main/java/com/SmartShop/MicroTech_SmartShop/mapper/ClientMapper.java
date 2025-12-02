package com.SmartShop.MicroTech_SmartShop.mapper;


import com.SmartShop.MicroTech_SmartShop.dto.request.ClientRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ClientResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponseDto toResponse(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tier", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "totalOrders", ignore = true)
    @Mapping(target = "user", ignore = true)
    Client toEntity(ClientRequestDto dto);
}
