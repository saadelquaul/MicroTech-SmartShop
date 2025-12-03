package com.SmartShop.MicroTech_SmartShop.mapper;

import com.SmartShop.MicroTech_SmartShop.dto.request.ProductRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ProductResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponseDto toDto (Product product) {
        if (product == null) return null;

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getUnitPrice())
                .stock(product.getStockQuantity())
                .isDeleted(product.getIsDeleted())
                .build();
    }

    public Product toEntity (ProductRequestDto dto) {
        if ( dto == null) return null;

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .unitPrice(dto.getPrice())
                .stockQuantity(dto.getStock())
                .isDeleted(false)
                .build();
    }
}
