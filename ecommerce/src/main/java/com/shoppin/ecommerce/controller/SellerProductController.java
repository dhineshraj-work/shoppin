package com.shoppin.ecommerce.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shoppin.ecommerce.dto.ProductModelDto;
import com.shoppin.ecommerce.service.SellerProductService;

@RestController
@RequestMapping("/auth/se")
public class SellerProductController {
	
	@Autowired
	SellerProductService productService;
	
	@GetMapping("product/get")
	public ResponseEntity<?> getProducts(Principal principal) {
		String username = principal.getName();
		return productService.getProductBySeller(username);
	}
	
	@GetMapping("product/get/{sku}")
	public ResponseEntity<Object> getProduct(Principal principal, @PathVariable String sku) {
		String username = principal.getName();
		return productService.getProduct(username, sku);
	}
	
	@PostMapping(value = "product/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addProduct(Principal principal, @RequestPart("product") String product, @RequestPart("image") MultipartFile image){
		String username = principal.getName();
		return productService.addProduct(username, product, image);
	}
	
	@PutMapping(value = "product/update/image/{sku}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> updateProductImage(Principal principal, @RequestPart("image") MultipartFile image, @PathVariable String sku){
		String username = principal.getName();
		return productService.updateProductImage(username, image, sku);
	}
	
	@PutMapping("product/update/{sku}")
	public ResponseEntity<Object> updateProduct(Principal principal, @RequestBody ProductModelDto product, @PathVariable String sku){
		String username = principal.getName();
		return productService.updateProduct(username, product, sku);
	}
	
	@DeleteMapping("product/delete/{sku}")
	public ResponseEntity<Object> updateProduct(Principal principal, @PathVariable String sku){
		String username = principal.getName();
		return productService.deleteProduct(username, sku);
	}
}
