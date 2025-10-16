package com.shoppin.ecommerce.service;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public boolean emailOtp(String toEmail, String subject, String body){
		
		try {
			
			LOGGER.info("Sending Email to Customer: "+toEmail);
			
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			
			mailMessage.setFrom(fromEmail);
			mailMessage.setTo("dhineshanddhinesh2002@gmail.com");
			mailMessage.setSubject(subject);
			mailMessage.setText(body);
			
			mailSender.send(mailMessage);
			
			LOGGER.info("Email Sent to "+toEmail);
			
			return true;
			
		}catch (Error e) {
			throw new RuntimeErrorException(e);
		}
	}
}
