package com.SmartShop.MicroTech_SmartShop.mapper;


import com.SmartShop.MicroTech_SmartShop.dto.request.PaymentRequestDto;
import com.SmartShop.MicroTech_SmartShop.dto.response.PaymentResponseDto;
import com.SmartShop.MicroTech_SmartShop.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDto toResponse(Payment payment) {
        if(payment == null) return null;

        return PaymentResponseDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .paymentNumber(payment.getPaymentNumber())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .encashmentDate(payment.getEncashmentDate())
                .build();
    }
}
