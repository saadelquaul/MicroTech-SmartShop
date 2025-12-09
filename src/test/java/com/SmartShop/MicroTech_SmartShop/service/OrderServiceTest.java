package com.SmartShop.MicroTech_SmartShop.service;


import com.SmartShop.MicroTech_SmartShop.dto.request.OrderItemRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.request.OrderRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.OrderResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Client;
import com.SmartShop.MicroTech_SmartShop.entity.Order;
import com.SmartShop.MicroTech_SmartShop.entity.OrderItem;
import com.SmartShop.MicroTech_SmartShop.entity.Product;
import com.SmartShop.MicroTech_SmartShop.enums.CustomerTier;
import com.SmartShop.MicroTech_SmartShop.enums.OrderStatus;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.mapper.OrderMapper;
import com.SmartShop.MicroTech_SmartShop.repository.ClientRepository;
import com.SmartShop.MicroTech_SmartShop.repository.OrderRepository;
import com.SmartShop.MicroTech_SmartShop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private LoyaltyService loyaltyService;

    @InjectMocks
    private OrderService orderService;


    private Client client;
    private Product product;
    private OrderRequestDto orderRequestDto;


    @BeforeEach
    void setUp() {
        client = Client.builder().id(1L)
                .name("Saad EL")
                .tier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(BigDecimal.ZERO)
                .build();

        product = Product.builder().id(100L)
                .name("Laptop X122")
                .unitPrice(new BigDecimal("1000.00"))
                .stockQuantity(10)
                .isDeleted(false)
                .build();

        OrderItemRequestDto itemDto = OrderItemRequestDto.builder()
                .quantity(2)
                .productId(100L).build();

        orderRequestDto = OrderRequestDto.builder()
                .items(Collections.singletonList(itemDto))
                .clientId(1L).build();

    }

    @Test
    @DisplayName("Should create order successfully with correct calculations")
    void createOrder_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        when(loyaltyService.getLoyaltyDiscountRate(any(), any())).thenReturn(BigDecimal.ZERO);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponseDto());

        orderService.createOrder(orderRequestDto);

        assertThat(product.getStockQuantity()).isEqualTo(8);
        verify(productRepository).save(product);

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order savedOrder = orderArgumentCaptor.getValue();

        assertThat(savedOrder.getSubTotal()).isEqualByComparingTo("2000.00");
        assertThat(savedOrder.getTaxAmount()).isEqualByComparingTo("400.00");
        assertThat(savedOrder.getTotalAmount()).isEqualByComparingTo("2400.00");
        assertThat(savedOrder.getRemainingAmount()).isEqualByComparingTo("2400.00");
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Should apply promo code correctly")
    void createOrder_WithPromoCode() {
        orderRequestDto.setPromoCode("PROMO-1111");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(loyaltyService.getLoyaltyDiscountRate(any(), any())).thenReturn(BigDecimal.ZERO);
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        orderService.createOrder(orderRequestDto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());
        Order savedOrder = captor.getValue();

        assertThat(savedOrder.getDiscountAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Should throw exception if stock is insufficient")
    void createOrder_InsufficientStock() {
        product.setStockQuantity(1);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                orderService.createOrder(orderRequestDto)
        );
        assertThat(ex.getMessage()).contains("Insufficient stock");
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if product is deleted")
    void createOrder_ProductDeleted() {

        product.setIsDeleted(true);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                orderService.createOrder(orderRequestDto)
        );
        assertThat(ex.getMessage()).contains("no longer available");
    }

    @Test
    @DisplayName("Should confirm order if fully paid")
    void confirmOrder_Success() {
        Order pendingOrder = Order.builder()
                .id(55L)
                .status(OrderStatus.PENDING)
                .remainingAmount(BigDecimal.ZERO)
                .totalAmount(new BigDecimal("2400.00"))
                .client(client)
                .build();

        when(orderRepository.findById(55L)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any())).thenReturn(pendingOrder);

        orderService.confirmOrder(55L);

        assertThat(pendingOrder.getStatus()).isEqualTo(OrderStatus.CONFIRMED);

        assertThat(client.getTotalOrders()).isEqualTo(1);
        assertThat(client.getTotalSpent()).isEqualByComparingTo("2400.00");

        verify(loyaltyService).updateClientTier(client);
    }

    @Test
    @DisplayName("Should fail to confirm if money remains")
    void confirmOrder_NotPaid() {

        Order unpaidOrder = Order.builder()
                .id(55L)
                .status(OrderStatus.PENDING)
                .remainingAmount(new BigDecimal("100.00"))
                .build();

        when(orderRepository.findById(55L)).thenReturn(Optional.of(unpaidOrder));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                orderService.confirmOrder(55L)
        );
        assertThat(ex.getMessage()).contains("not fully paid");
    }

    @Test
    @DisplayName("Should fail to confirm if status is not PENDING")
    void confirmOrder_WrongStatus() {
        Order canceledOrder = Order.builder()
                .id(55L)
                .status(OrderStatus.CANCELED)
                .build();

        when(orderRepository.findById(55L)).thenReturn(Optional.of(canceledOrder));

        assertThrows(BusinessException.class, () -> orderService.confirmOrder(55L));
    }

    @Test
    @DisplayName("Should cancel order and restore stock")
    void cancelOrder_Success() {

        product.setStockQuantity(8);

        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();

        Order orderToCancel = Order.builder()
                .id(55L)
                .status(OrderStatus.PENDING)
                .orderItems(List.of(item))
                .build();

        when(orderRepository.findById(55L)).thenReturn(Optional.of(orderToCancel));

        orderService.cancelOrder(55L);


        assertThat(orderToCancel.getStatus()).isEqualTo(OrderStatus.CANCELED);


        assertThat(product.getStockQuantity()).isEqualTo(10);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Should throw exception when cancelling non-pending order")
    void cancelOrder_AlreadyConfirmed() {

        Order confirmedOrder = Order.builder()
                .id(55L)
                .status(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.findById(55L)).thenReturn(Optional.of(confirmedOrder));


        assertThrows(BusinessException.class, () -> orderService.cancelOrder(55L));
    }
}