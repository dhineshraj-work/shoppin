package com.shoppin.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoppin.ecommerce.dto.CheckoutAddressDTO;
import com.shoppin.ecommerce.dto.CheckoutCartDTO;
import com.shoppin.ecommerce.model.AddressModel;
import com.shoppin.ecommerce.model.CartModel;
import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.repo.AddressRepository;
import com.shoppin.ecommerce.repo.CartRepository;
import com.shoppin.ecommerce.repo.CustomerRepository;

@Service
public class CheckoutService {
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired 
	CustomerRepository customerRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	OrderService orderService;

	public ResponseEntity<Object> addAddress(String username, CheckoutAddressDTO checkout) {
		try {
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cart==null) {
				return ResponseEntity.status(404).body("Cart Not Found");
			}
			
			AddressModel deliveryAddress = addressRepository.findByUserPkAndAddressLine1AndFirstNameAndContactNumber(customer.getPk() ,checkout.getDeliveryAddress().getAddressLine1()
																, checkout.getDeliveryAddress().getFirstName(), checkout.getDeliveryAddress().getContactNumber()).orElse(null);
			
			if(deliveryAddress==null) {
				return ResponseEntity.status(404).body("Delivery Address is missing");
			}
			
			AddressModel paymentAddress = null;
			if(checkout.getDeliveryAddress()==checkout.getPaymentAddress()) {
				paymentAddress = deliveryAddress;
			}else {
				paymentAddress = addressRepository.findByUserPkAndAddressLine1AndFirstNameAndContactNumber(customer.getPk() ,checkout.getDeliveryAddress().getAddressLine1()
						, checkout.getDeliveryAddress().getFirstName(), checkout.getDeliveryAddress().getContactNumber()).orElse(null);
				if(paymentAddress==null) {
					return ResponseEntity.status(404).body("Payment Address is missing");
				}
			}
			cart.setDeliveryAddress(deliveryAddress);
			cart.setPaymentAddress(paymentAddress);
			
			cartRepository.save(cart);
			
			return ResponseEntity.status(200).body("Address saved");
			
		}catch(Exception e) {
			return ResponseEntity.status(500).body("Something went wrong while adding address"+e);
		}
	}

	public ResponseEntity<Object> cartCheckout(String username, CheckoutCartDTO checkout) {
		try {
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cart==null) {
				return ResponseEntity.status(404).body("Cart Not Found");
			}
			
			cart.setPaymentMethod(checkout.getPaymentMethod());
			cart.setPaymentCompleted(checkout.isPaymentCompleted());
			
			cartRepository.save(cart);
			
			if(cart.isPaymentCompleted()) {
				return orderService.placeOrder(username, cart);
			}
			
			return ResponseEntity.status(402).body("Order not placed due to payment error");
			
		}catch(Exception e) {
			return ResponseEntity.status(500).body("Something went wrong while adding address"+e);
		}
	}

}
