package com.shoppin.ecommerce.dto;

import org.springframework.stereotype.Component;

import com.shoppin.ecommerce.model.OrderStatus;

import lombok.Data;

@Data
@Component
public class ConsignmentDto {

	private String orderNumber;
	private OrderStatus status;
	private String otp;
}
