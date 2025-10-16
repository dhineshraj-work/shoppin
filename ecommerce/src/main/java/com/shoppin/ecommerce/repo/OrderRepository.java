package com.shoppin.ecommerce.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shoppin.ecommerce.model.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Integer>{

	@Query("SELECT o.orderNumber FROM OrderModel o WHERE o.createdTime>= :today ORDER BY o.createdTime DESC")
	List<String> findTopOrderNumberByOrderByCreatedTimeDesc(@Param("today") LocalDateTime today);

	Optional<String> findTopOrderNumberByOrderByCreatedTimeDesc();
	
	List<OrderModel> findByUserEmail(String username);

	Optional<OrderModel> findByUserEmailAndOrderNumber(String username, String orderNo);

	Optional<OrderModel> findByOrderNumber(String orderNumber);

}
