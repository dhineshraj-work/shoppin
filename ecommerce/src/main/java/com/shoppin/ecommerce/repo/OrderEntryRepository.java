package com.shoppin.ecommerce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppin.ecommerce.model.OrderEntryModel;

@Repository
public interface OrderEntryRepository extends JpaRepository<OrderEntryModel, Integer>{

}
