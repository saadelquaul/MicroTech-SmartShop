package com.SmartShop.MicroTech_SmartShop.dto.response;


import com.SmartShop.MicroTech_SmartShop.enums.PaymentMethod;
import com.SmartShop.MicroTech_SmartShop.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class PaymentResponseDto {

   private Long id;
   private Long orderId;
   private Integer paymentNumber;

   private BigDecimal amount;
   private PaymentMethod method;
   private PaymentStatus status;

   private LocalDateTime paymentDate;
   private LocalDateTime encashmentDate;
}
