package com.SmartShop.MicroTech_SmartShop.service;


import com.SmartShop.MicroTech_SmartShop.dto.request.PaymentRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.PaymentResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Order;
import com.SmartShop.MicroTech_SmartShop.entity.Payment;
import com.SmartShop.MicroTech_SmartShop.enums.OrderStatus;
import com.SmartShop.MicroTech_SmartShop.enums.PaymentMethod;
import com.SmartShop.MicroTech_SmartShop.enums.PaymentStatus;
import com.SmartShop.MicroTech_SmartShop.exception.BusinessException;
import com.SmartShop.MicroTech_SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.MicroTech_SmartShop.mapper.PaymentMapper;
import com.SmartShop.MicroTech_SmartShop.repository.OrderRepository;
import com.SmartShop.MicroTech_SmartShop.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponseDto recordPayment(PaymentRequestDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));


        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Payments can only be added to PENDING orders.");
        }

        if (dto.getAmount().compareTo(order.getRemainingAmount()) > 0) {
            throw new BusinessException("Payment amount (" + dto.getAmount() +
                    ") exceeds remaining balance (" + order.getRemainingAmount() + ")");
        }


        if (dto.getMethod() == PaymentMethod.ESPECES &&
                dto.getAmount().compareTo(new BigDecimal("20000")) > 0) {
            throw new BusinessException("Cash payments cannot exceed 20,000 DH (Legal Limit).");
        }


        int nextPaymentNumber = paymentRepository.findByOrderId(order.getId()).size() + 1;

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentNumber(nextPaymentNumber);
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setPaymentDate(LocalDateTime.now());


        if (dto.getMethod() == PaymentMethod.ESPECES) {
            payment.setStatus(PaymentStatus.ENCAISSE);
            payment.setEncashmentDate(LocalDateTime.now());
        } else {
            payment.setStatus(PaymentStatus.EN_ATTENTE);
        }

        Payment savedPayment = paymentRepository.save(payment);


        order.setRemainingAmount(order.getRemainingAmount().subtract(dto.getAmount()));
        orderRepository.save(order);

        return paymentMapper.toResponse(savedPayment);
    }

    public List<PaymentResponseDto> getPaymentsByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
