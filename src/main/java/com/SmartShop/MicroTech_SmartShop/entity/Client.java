package com.SmartShop.MicroTech_SmartShop.entity;


import com.SmartShop.MicroTech_SmartShop.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CustomerTier tier = CustomerTier.BASIC;

    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Builder.Default
    private Integer totalOrders = 0;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


}
