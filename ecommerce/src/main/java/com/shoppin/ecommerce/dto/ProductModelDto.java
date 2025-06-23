package com.shoppin.ecommerce.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ProductModelDto {

	private String productName;
	private String categoryName;
	private double price;
	private int stock;
	private String description;
	private double discount;
	private String brand;
	private boolean isAvailable;
}
