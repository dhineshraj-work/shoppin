package com.shoppin.ecommerce.data;

import com.shoppin.ecommerce.model.CategoryModel;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductData {

	private String productName;
	private CategoryModel category;
	private double price;
	private int stock;
	private String description;
	private boolean isAvailable;
    private String skuCode;
	private double discount;
	private String brand;
	private String imagename;
	private String imageType;
	@Lob
	private byte[] image;
}
