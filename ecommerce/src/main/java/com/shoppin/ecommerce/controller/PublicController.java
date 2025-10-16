package com.shoppin.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppin.ecommerce.dto.AuthRequest;
import com.shoppin.ecommerce.dto.ConsignmentDto;
import com.shoppin.ecommerce.dto.CustomerRegisterDto;
import com.shoppin.ecommerce.model.OrderStatus;
import com.shoppin.ecommerce.repo.CustomerRepository;
import com.shoppin.ecommerce.repo.ProductRepository;
import com.shoppin.ecommerce.service.PublicService;


@RestController
@RequestMapping("public")
public class PublicController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	PublicService publicService;
	
	@Autowired
	CustomerRepository customerRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@GetMapping("products")
	public ResponseEntity<Object> getAllProducts(){
		return ResponseEntity.ok(productRepository.findAll());
	}
	
	@GetMapping("products/{productId}")	
	public ResponseEntity<Object> getProduct(@PathVariable Integer productId){
		return ResponseEntity.ok(productRepository.findById(productId));
	}
	
	@PostMapping("register")
	public ResponseEntity<Object> registerNewUser(@RequestBody CustomerRegisterDto customer){
		return publicService.registerNewUser(customer);
	}
	
	@PostMapping("login")
	public ResponseEntity<Object> loginUser(@RequestBody AuthRequest authRequest){
		return publicService.loginUserWithCredentials(authRequest);
	}
	
	@GetMapping("path")
	public boolean getMethodName(@RequestBody ConsignmentDto data) {
		
		OrderStatus status = data.getStatus();
		
		if(data.getOtp().length()!=6) {
			LOGGER.info("OTP is null");
		}
		
		if(OrderStatus.DELIVERED==status) {
			return true;
		}
		return false;
	}
	
}
