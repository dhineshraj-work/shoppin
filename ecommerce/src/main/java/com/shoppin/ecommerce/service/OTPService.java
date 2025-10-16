package com.shoppin.ecommerce.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

	public String generateOTP() {
		
		Random random = new Random();
		int otp = 100000 + random.nextInt(899999);
		return String.valueOf(otp);
	}
}
