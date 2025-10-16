package com.shoppin.ecommerce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoppin.ecommerce.converter.DataConverter;
import com.shoppin.ecommerce.dto.CheckoutAddressDTO;
import com.shoppin.ecommerce.dto.CheckoutCartDTO;
import com.shoppin.ecommerce.model.CartModel;
import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.model.DeliveryAddressModel;
import com.shoppin.ecommerce.model.PaymentAddressModel;
import com.shoppin.ecommerce.repo.CartRepository;
import com.shoppin.ecommerce.repo.CustomerRepository;
import com.shoppin.ecommerce.repo.DeliveryAddressRepository;
import com.shoppin.ecommerce.repo.PaymentAddressRepository;

@Service
public class CheckoutService {
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired 
	CustomerRepository customerRepository;
	
	@Autowired
	PaymentAddressRepository paymentAddressRepository;
	
	@Autowired
	DeliveryAddressRepository deliveryAddressRepository;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	DataConverter converter;
	
	private final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

	public ResponseEntity<Object> addAddress(String username, CheckoutAddressDTO checkout) {
		
		try {
			
			LOGGER.info("Customer add address to cart : User : "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cart==null) {
				
				LOGGER.info("Cart Not Found : User : "+username);
				
				return ResponseEntity.status(404).body("Cart Not Found");
			}
			
			DeliveryAddressModel deliveryAddress = getDeliveryAddress(customer, checkout.getDeliveryAddress());
			
			PaymentAddressModel paymentAddress = null;
			
			if(checkout.getPaymentAddress()==null && checkout.isSameAddress()) {
				
				paymentAddress = paymentAddressRepository.save(converter.convertPaymentAddress(deliveryAddress));
				
			}else {
				
				paymentAddress = getPaymentAddress(customer, checkout.getPaymentAddress());
				
			}
			
			cart.setDeliveryAddress(deliveryAddress);
			cart.setPaymentAddress(paymentAddress);
			
			cartRepository.save(cart);
			
			LOGGER.info("Cart Address saved successfully : User : "+username);
			
			return ResponseEntity.status(200).body("Address saved");
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while adding checkout address\n"+e.getMessage()+"\n"+username);
			
			return ResponseEntity.status(500).body("Something went wrong while adding checkout address\n"+e);
		}
	}

	public ResponseEntity<Object> cartCheckout(String username, CheckoutCartDTO checkout) {
		try {
			
			LOGGER.info("Customer checkout the cart : User : "+username);
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cart==null) {
				
				LOGGER.info("Cart Not Found : User : "+username);
				
				return ResponseEntity.status(404).body("Cart Not Found");
			}
			
			cart.setPaymentMethod(checkout.getPaymentMethod());
			cart.setPaymentCompleted(checkout.isPaymentCompleted());
			
			cartRepository.save(cart);
			
			if(cart.isPaymentCompleted()) {
				
				LOGGER.info("Payment Completed : User : "+username);
				
				return orderService.placeOrder(username, cart);
			}
			
			LOGGER.info("Payment Not Completed : User : "+username);
			
			return ResponseEntity.status(402).body("Order not placed due to payment error");
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while cart checkout\n"+e.getMessage()+"\n"+username);
			
			return ResponseEntity.status(500).body("Something went wrong while checkout\n"+e.getMessage());
		}
	}
	
	private DeliveryAddressModel getDeliveryAddress(CustomerModel customer, DeliveryAddressModel address) {
		
		DeliveryAddressModel addressModel = deliveryAddressRepository.
												findByUserEmailAndAddressLine1AndAddressLine2AndTownAndBuildingAndPostalCodeAndFirstNameAndLastNameAndContactNumber(
														customer.getEmail(), 
														address.getAddressLine1(), 
														address.getAddressLine2(), 
														address.getTown(),
														address.getBuilding(),
														address.getPostalCode(), 
														address.getFirstName(), 
														address.getLastName(), 
														address.getContactNumber()
														).orElse(null);
		
		if(addressModel!=null) {
			
			return addressModel;
		}
		
		address.setUser(customer);
		
		DeliveryAddressModel savedAddress = deliveryAddressRepository.save(address);
		
		return savedAddress;
	}		
	
	private PaymentAddressModel getPaymentAddress(CustomerModel customer, PaymentAddressModel address) {
		
		PaymentAddressModel addressModel = paymentAddressRepository.
												findByUserEmailAndAddressLine1AndAddressLine2AndTownAndBuildingAndPostalCodeAndFirstNameAndLastNameAndContactNumber(
														customer.getEmail(), 
														address.getAddressLine1(), 
														address.getAddressLine2(), 
														address.getTown(),
														address.getBuilding(),
														address.getPostalCode(), 
														address.getFirstName(), 
														address.getLastName(), 
														address.getContactNumber()
														).orElse(null);
		
		
		
		if(addressModel!=null) {

			return addressModel;
		}
		
		address.setUser(customer);
		
		PaymentAddressModel savedAddress = paymentAddressRepository.save(address);
		
		return savedAddress;
	}
	
	

}
