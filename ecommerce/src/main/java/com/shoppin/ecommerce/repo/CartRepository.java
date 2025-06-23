package com.shoppin.ecommerce.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppin.ecommerce.model.CartModel;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartModel, Integer>{

	Optional<CartModel> findByCustomerEmail(String username);

	@Transactional
	void deleteByCustomerEmail(String username);

}
