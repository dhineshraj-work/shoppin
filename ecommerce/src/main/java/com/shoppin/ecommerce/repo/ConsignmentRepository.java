package com.shoppin.ecommerce.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppin.ecommerce.model.ConsignmentModel;

@Repository
public interface ConsignmentRepository extends JpaRepository<ConsignmentModel, Integer>{

	Optional<ConsignmentModel> findBySellerEmailAndParentOrderOrderNumber(String email, String orderNumber);

	List<ConsignmentModel> findBySellerEmail(String username);

}
