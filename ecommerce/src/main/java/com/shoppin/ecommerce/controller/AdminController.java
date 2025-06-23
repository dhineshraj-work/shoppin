package com.shoppin.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppin.ecommerce.model.AddressModel;
import com.shoppin.ecommerce.repo.CustomerRepository;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {
	
	@Autowired
	CustomerRepository customerRepository;

	@GetMapping("body/{email}")
	public ResponseEntity<Object> getBodyByEmail(@PathVariable String email){
		return ResponseEntity.ok(customerRepository.findByEmail(email));
	}
	
	@GetMapping("body/address")
	public ResponseEntity<Object> getBody(){
		return ResponseEntity.ok(new AddressModel());
	}
}
