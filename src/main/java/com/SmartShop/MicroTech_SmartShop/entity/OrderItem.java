package com.SmartShop.MicroTech_SmartShop.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice; // Prix unitaire au moment de la commande

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice; // Prix total pour cette ligne (unitPrice * quantity)

}
