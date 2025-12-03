package com.SmartShop.MicroTech_SmartShop.mapper;

import com.SmartShop.MicroTech_SmartShop.dto.request.ProductRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ProductResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Product toEntity(ProductRequestDto dto);
}
