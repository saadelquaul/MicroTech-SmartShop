package com.SmartShop.MicroTech_SmartShop.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = false")
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
