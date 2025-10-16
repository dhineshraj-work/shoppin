package com.shoppin.ecommerce.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.model.DeliveryAddressModel;
import com.shoppin.ecommerce.repo.CustomerRepository;
import com.shoppin.ecommerce.repo.DeliveryAddressRepository;

@Service
public class AddressService {

	@Autowired
	DeliveryAddressRepository delRepo;
	
	@Autowired
	CustomerRepository customerRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);
	
	public ResponseEntity<Object> getAddress(String username) {
		
		try {
			
			List<DeliveryAddressModel> address = delRepo.findByUserEmail(username);
			
			if(address.isEmpty()) {
				return ResponseEntity.status(404).body("No Address Found for this customer");
			}
			
			LOGGER.info("User get Address: "+username);
			
			return ResponseEntity.ok(address);
			
		}catch(Exception e) {

			LOGGER.info("Something went wrong while get Address : "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong \n"+e.getMessage());
		}
	}

	public ResponseEntity<Object> addAddress(String username, DeliveryAddressModel address) {
		try {
			
			LOGGER.info("User request to add new Address: "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			DeliveryAddressModel addressModel = getDeliveryAddress(username, address);
			
			if(addressModel==null) {
				
				address.setUser(customer);
				
				delRepo.save(address);
				
				LOGGER.info("User Address Saved: "+username);
				
				return ResponseEntity.ok(address);
			}
			
			return ResponseEntity.status(409).body("Address Already Added");
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while saving Address : "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong \n"+e.getMessage());
		}
	}

	private DeliveryAddressModel getDeliveryAddress(String username, DeliveryAddressModel address) {
		
		DeliveryAddressModel addressModel = delRepo.
												findByUserEmailAndAddressLine1AndAddressLine2AndTownAndBuildingAndPostalCodeAndFirstNameAndLastNameAndContactNumber(
														username, 
														address.getAddressLine1(), 
														address.getAddressLine2(), 
														address.getTown(),
														address.getBuilding(),
														address.getPostalCode(), 
														address.getFirstName(), 
														address.getLastName(), 
														address.getContactNumber()
														).orElse(null);
		
		return address==null?null:addressModel;
	}
}
