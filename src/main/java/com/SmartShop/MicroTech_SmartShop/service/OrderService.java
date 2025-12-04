package com.SmartShop.MicroTech_SmartShop.service;


import com.SmartShop.MicroTech_SmartShop.dto.request.OrderItemRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.request.OrderRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.OrderResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Client;
import com.SmartShop.MicroTech_SmartShop.entity.Order;
import com.SmartShop.MicroTech_SmartShop.entity.OrderItem;
import com.SmartShop.MicroTech_SmartShop.entity.Product;
import com.SmartShop.MicroTech_SmartShop.enums.OrderStatus;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.MicroTech_SmartShop.mapper.OrderMapper;
import com.SmartShop.MicroTech_SmartShop.repository.ClientRepository;
import com.SmartShop.MicroTech_SmartShop.repository.OrderRepository;
import com.SmartShop.MicroTech_SmartShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;
    private final LoyaltyService loyaltyService;

    private static final BigDecimal TVA_RATE = new BigDecimal("0.20");
    private static final Pattern PROMO_PATTERN = Pattern.compile("^PROMO-[A-Z0-9]{4}$");


    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Order order = new Order();
        order.setClient(client);
        order.setDateCreation(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPromoCode(dto.getPromoCode());
        order.setOrderItems(new ArrayList<>());

        BigDecimal subTotal = BigDecimal.ZERO;


        for (OrderItemRequestDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDto.getProductId()));


            if (product.getIsDeleted()) {
                throw new BusinessException("Product " + product.getName() + " is no longer available.");
            }

            if (product.getStockQuantity() < itemDto.getQuantity()) {
                throw new BusinessException("Insufficient stock for: " + product.getName());
            }


            product.setStockQuantity(product.getStockQuantity() - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getUnitPrice())
                    .totalPrice(product.getUnitPrice().multiply(new BigDecimal(itemDto.getQuantity())))
                    .build();

            order.getOrderItems().add(orderItem);
            subTotal = subTotal.add(orderItem.getTotalPrice());
        }

        order.setSubTotal(subTotal);


        BigDecimal loyaltyRate = loyaltyService.getLoyaltyDiscountRate(client.getTier(), subTotal);


        BigDecimal promoRate = BigDecimal.ZERO;
        if (dto.getPromoCode() != null && PROMO_PATTERN.matcher(dto.getPromoCode()).matches()) {
            promoRate = new BigDecimal("0.05");
        }


        BigDecimal totalDiscountRate = loyaltyRate.add(promoRate);
        BigDecimal discountAmount = subTotal.multiply(totalDiscountRate).setScale(2, RoundingMode.HALF_UP);
        order.setDiscountAmount(discountAmount);


        BigDecimal netHt = subTotal.subtract(discountAmount);

        BigDecimal taxAmount = netHt.multiply(TVA_RATE).setScale(2, RoundingMode.HALF_UP);
        order.setTaxAmount(taxAmount);

        BigDecimal totalAmount = netHt.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
        order.setTotalAmount(totalAmount);
        order.setRemainingAmount(totalAmount);


        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponseDto confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Only PENDING orders can be confirmed.");
        }

        if (order.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("Cannot confirm: Order is not fully paid. Remaining: " + order.getRemainingAmount());
        }

        order.setStatus(OrderStatus.CONFIRMED);

        Client client = order.getClient();
        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent().add(order.getTotalAmount()));

        loyaltyService.updateClientTier(client);
        clientRepository.save(client);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Cannot cancel an order that is already " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELED);

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
    }
}
