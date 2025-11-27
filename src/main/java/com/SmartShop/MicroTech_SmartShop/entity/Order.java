package com.SmartShop.MicroTech_SmartShop.entity;

import com.SmartShop.MicroTech_SmartShop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal; // HT

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount; // Remise

    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount; // TVA

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount; // TTC

    @Column(precision = 10, scale = 2)
    private BigDecimal remainingAmount; // Reste à payer

    private String promoCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
}
