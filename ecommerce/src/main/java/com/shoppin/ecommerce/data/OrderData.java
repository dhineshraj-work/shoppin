package com.shoppin.ecommerce.data;

import java.util.List;

import com.shoppin.ecommerce.model.DeliveryAddressModel;
import com.shoppin.ecommerce.model.PaymentAddressModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderData {

	private String orderNumber;
	private DeliveryAddressModel deliveryAddress;
	private PaymentAddressModel paymentAddress;
	private List<OrderEntryData> orderEntries;
	private List<ConsignmentData> consignments;	
	private double discount;	
	private double totalPrice;
		
}
