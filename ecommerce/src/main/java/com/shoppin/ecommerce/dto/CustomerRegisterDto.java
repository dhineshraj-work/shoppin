package com.shoppin.ecommerce.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.shoppin.ecommerce.model.Role;

import lombok.Data;

@Component
@Data
public class CustomerRegisterDto {
	
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String contactNumber;
	private LocalDate DOB;
	private Role role;

}
