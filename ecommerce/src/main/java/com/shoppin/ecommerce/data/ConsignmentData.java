package com.shoppin.ecommerce.data;

import java.util.List;

import com.shoppin.ecommerce.model.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsignmentData {
	
	private Integer consignmentId;
	
	private String orderNumber;

	private List<String> products;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	private String orderPlacedDate;
	
	private AddressData paymentAddress;
	
	private AddressData deliveryAddress;
	
}
