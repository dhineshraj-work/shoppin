package com.shoppin.ecommerce.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppin.ecommerce.model.CartProductModel;

import jakarta.transaction.Transactional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProductModel, Integer>{

	Optional<CartProductModel> findByCartCustomerPkAndProductSkuCode(Integer pk, String sku);

	List<CartProductModel> findByCartPk(Integer pk);

	boolean existsByCartCustomerPkAndProductSkuCode(Integer pk, String sku);

	@Transactional
	void deleteByCartCustomerPkAndProductSkuCode(Integer pk, String sku);

}
