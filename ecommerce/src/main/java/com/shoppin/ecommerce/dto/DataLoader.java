package com.shoppin.ecommerce.dto;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.model.Role;
import com.shoppin.ecommerce.repo.CustomerRepository;

@Component
public class DataLoader implements CommandLineRunner{
	
	@Autowired
	CustomerRepository custRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		loadUserDate();		
	}

	private void loadUserDate() {
		
		CustomerModel customerModel = new CustomerModel();
		
		customerModel.setEmail("dhineshraje2002@gmail.com");
		customerModel.setPassword(passwordEncoder.encode("Dhinesh@2"));
		customerModel.setFirstName("Dhinesh");
		customerModel.setLastName("raj");
		customerModel.setDOB(LocalDate.parse("2002-08-15"));
		customerModel.setRole(Role.ADMIN);
		customerModel.setContactNumber("8565945236");
		customerModel.setAge(22);
		custRepo.save(customerModel);
		
		CustomerModel customerModel1 = new CustomerModel();
		
		customerModel1.setEmail("Aaron@gmail.com");
		customerModel1.setPassword(passwordEncoder.encode("Dhinesh@2"));
		customerModel1.setFirstName("Aaron");
		customerModel1.setLastName("paul");
		customerModel1.setDOB(LocalDate.parse("2008-08-15"));
		customerModel1.setRole(Role.CONSUMER);
		customerModel1.setContactNumber("8254785126");
		customerModel1.setAge(17);
		custRepo.save(customerModel1);	
		
		CustomerModel customerModel3 = new CustomerModel();
		
		customerModel3.setEmail("Ajith@gmail.com");
		customerModel3.setPassword(passwordEncoder.encode("Dhinesh@2"));
		customerModel3.setFirstName("Ajith");
		customerModel3.setLastName("kumar");
		customerModel3.setDOB(LocalDate.parse("2008-06-15"));
		customerModel3.setRole(Role.CONSUMER);
		customerModel3.setContactNumber("6598562158");
		customerModel3.setAge(17);
		custRepo.save(customerModel3);
		
		CustomerModel customerModel2 = new CustomerModel();
		
		customerModel2.setEmail("Nareesh@gmail.com");
		customerModel2.setPassword(passwordEncoder.encode("Dhinesh@2"));
		customerModel2.setFirstName("Nareesh");
		customerModel2.setLastName("hump");
		customerModel2.setDOB(LocalDate.parse("2006-08-15"));
		customerModel2.setRole(Role.SELLER);
		customerModel2.setContactNumber("9656487895");
		customerModel2.setAge(19);
		custRepo.save(customerModel2);	
		
		CustomerModel customerModel4 = new CustomerModel();
		
		customerModel4.setEmail("Hareesh@gmail.com");
		customerModel4.setPassword(passwordEncoder.encode("Dhinesh@2"));
		customerModel4.setFirstName("Hareesh");
		customerModel4.setLastName("Khan");
		customerModel4.setDOB(LocalDate.parse("2003-08-15"));
		customerModel4.setRole(Role.SELLER);
		customerModel4.setContactNumber("8265456322");
		customerModel4.setAge(22);
		custRepo.save(customerModel4);
		
		System.out.println("User Data Loaded");
		
	}
	
	

}
