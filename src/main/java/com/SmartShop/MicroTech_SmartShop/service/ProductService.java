package com.SmartShop.MicroTech_SmartShop.service;

import com.SmartShop.MicroTech_SmartShop.dto.request.ProductRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.ProductResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Product;
import com.SmartShop.MicroTech_SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.MicroTech_SmartShop.mapper.ProductMapper;
import com.SmartShop.MicroTech_SmartShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(dto.getName());
        product.setUnitPrice(dto.getPrice());
        product.setStockQuantity(dto.getStock());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setIsDeleted(true);
        productRepository.save(product);

    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper.toResponse(product);
    }
}
