package com.shoppin.ecommerce.data;

import com.shoppin.ecommerce.model.ProductModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntryData {

	private ProductModel product;
	private int quantity;
}
