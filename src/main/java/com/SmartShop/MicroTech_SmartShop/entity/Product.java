package com.SmartShop.MicroTech_SmartShop.entity;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    private Integer stockQuantity;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}
