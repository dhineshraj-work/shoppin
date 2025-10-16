package com.shoppin.ecommerce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shoppin.ecommerce.dto.AuthRequest;
import com.shoppin.ecommerce.dto.CustomerRegisterDto;
import com.shoppin.ecommerce.dto.JwtResponse;
import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.repo.CustomerRepository;

@Service
public class PublicService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	OTPService otpService;
	
	private final Logger LOGGER = LoggerFactory.getLogger(PublicService.class);

	public ResponseEntity<Object> registerNewUser(CustomerRegisterDto customer) {
		
		try {
			
			LOGGER.info("New User register process started");
			
			CustomerModel customerModel = new CustomerModel();
			
			customerModel.setEmail(customer.getEmail());
			customerModel.setPassword(passwordEncoder.encode(customer.getPassword()));
			customerModel.setFirstName(customer.getFirstName());
			customerModel.setLastName(customer.getLastName());
			customerModel.setDOB(customer.getDOB());
			customerModel.setRole(customer.getRole());
			customerModel.setContactNumber(customer.getContactNumber());
			customerModel.setAge(getAge(customer.getDOB()));
			
			customerRepository.save(customerModel);
			
			LOGGER.info("New User register process ended");
			LOGGER.info("New User registered "+customer.getEmail());
			
			return ResponseEntity.status(201).body("User registered  "+customer.getEmail());
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while registering new user\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while registering new user\n"+e.getMessage());
			
		}
	}

	private int getAge(LocalDate dob) {
		LocalDate now = LocalDate.now();
		
		Period agePeriod = Period.between(dob, now);
		return agePeriod.getYears();
	}

	public ResponseEntity<Object> loginUserWithCredentials(AuthRequest authRequest) throws UsernameNotFoundException{
		
		try {
			
			LOGGER.info("User Login "+authRequest.getEmail());
			
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
			
			if(authentication.isAuthenticated()) {
				
				CustomerModel customerModel = customerRepository.findByEmail(authRequest.getEmail()).orElseThrow();
				
				customerModel.setLastLogin(LocalDateTime.now());
				
				customerRepository.save(customerModel);
				
				String jwt = jwtService.generatetoken(authRequest.getEmail());
				
				LOGGER.info("User Logged in successfully "+authRequest.getEmail());
				
				JwtResponse jwtResponse = new JwtResponse(200, jwt);
				
				return ResponseEntity.ok(jwtResponse);
			}
			
			LOGGER.info("User entered wrong credentials "+authRequest.getEmail());
			
			return ResponseEntity.status(401).body("Bad Credentials");
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while User login\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while User login\n"+e.getMessage());
			
		}		
	}	
	
	public ResponseEntity<Object> getPasswordResetOTP(String email) {
		try {
			
			CustomerModel user = customerRepository.findByEmail(email).orElse(null);
			
			if(user==null) {
				return ResponseEntity.status(404).body("User not found for the email provided");
			}
			
			String otp = otpService.generateOTP();
			
			user.setOtp(otp);
			
			customerRepository.save(user);
			
			String subject = "JobApp password assistance";
			String body = "To authenticate, please use the following One Time Password (OTP)  :  "+otp;
			
			return ResponseEntity.ok(body);
			
			//return emailService.emailOtp(email, subject, body);
			
		}catch(Exception e) {
			return ResponseEntity.status(500).body("Something went wrong  "+e.getMessage());
		}
	}
	
}
