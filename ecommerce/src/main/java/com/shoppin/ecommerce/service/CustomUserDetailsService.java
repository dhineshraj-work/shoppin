package com.shoppin.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.model.UserPrincipal;
import com.shoppin.ecommerce.repo.CustomerRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomerModel user = customerRepository.findByEmail(username).orElseThrow();
		return new UserPrincipal(user);
	}

}
