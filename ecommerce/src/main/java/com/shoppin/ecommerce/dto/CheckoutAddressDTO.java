package com.shoppin.ecommerce.dto;

import org.springframework.stereotype.Component;

import com.shoppin.ecommerce.model.DeliveryAddressModel;
import com.shoppin.ecommerce.model.PaymentAddressModel;

import lombok.Data;

@Component
@Data
public class CheckoutAddressDTO {
	
	private PaymentAddressModel paymentAddress;
	private DeliveryAddressModel deliveryAddress;
	private boolean isSameAddress;
}
