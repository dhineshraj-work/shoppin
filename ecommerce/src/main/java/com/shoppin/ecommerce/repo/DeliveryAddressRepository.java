package com.shoppin.ecommerce.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppin.ecommerce.model.DeliveryAddressModel;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddressModel, Integer>{

	Optional<DeliveryAddressModel> findByUserEmailAndAddressLine1AndAddressLine2AndTownAndBuildingAndPostalCodeAndFirstNameAndLastNameAndContactNumber(
			String username, 
			String addressLine1, 
			String addressLine2, 
			String town, 
			String building, 
			String postalCode,
			String firstName, 
			String lastName, 
			String contactNumber);

	List<DeliveryAddressModel> findByUserEmail(String username);
}
