package com.SmartShop.MicroTech_SmartShop.mapper;


import com.SmartShop.MicroTech_SmartShop.dto.request.ClientRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ClientResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponseDto toResponse (Client client) {
        if (client == null) return null;

        return ClientResponseDto.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .tier(client.getTier())
                .totalSpent(client.getTotalSpent())
                .totalOrders(client.getTotalOrders())
                .build();
    }

    public Client toEntity(ClientRequestDto dto)
    {
        if (dto == null) return null;

        return Client.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
