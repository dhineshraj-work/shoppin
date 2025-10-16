package com.shoppin.ecommerce.dto;

import org.springframework.stereotype.Component;
import lombok.Data;


@Component
@Data
public class OTPRequest {
	
	private String email;
	private String otp;
	private String password;
}
