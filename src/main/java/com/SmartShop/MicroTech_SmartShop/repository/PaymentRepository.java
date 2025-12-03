package com.SmartShop.MicroTech_SmartShop.repository;

import com.SmartShop.MicroTech_SmartShop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
}