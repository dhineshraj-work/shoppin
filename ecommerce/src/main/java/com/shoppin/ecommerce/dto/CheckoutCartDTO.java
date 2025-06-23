package com.shoppin.ecommerce.dto;

import org.springframework.stereotype.Component;

import com.shoppin.ecommerce.model.PaymentMethods;

import lombok.Data;

@Component
@Data
public class CheckoutCartDTO {

	private PaymentMethods paymentMethod;
	private boolean isPaymentCompleted;
}
