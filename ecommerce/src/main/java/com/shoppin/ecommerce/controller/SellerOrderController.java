package com.shoppin.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppin.ecommerce.dto.ConsignmentDto;
import com.shoppin.ecommerce.service.SellerOrderService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping("/auth/se")
@RestController
public class SellerOrderController {
	
	@Autowired
	SellerOrderService sellerOrderService;

	@GetMapping("order")
	public ResponseEntity<Object> getOrders(Principal principal) {
		
		String username = principal.getName();
		return sellerOrderService.getOrders(username);
	}
	
	@PutMapping("order")
	public ResponseEntity<Object> updateConsignment(Principal principal, @RequestBody ConsignmentDto consignment) {
		
		String username = principal.getName();
		
		return sellerOrderService.updateConsignment(username, consignment);
	}
	
	
}
