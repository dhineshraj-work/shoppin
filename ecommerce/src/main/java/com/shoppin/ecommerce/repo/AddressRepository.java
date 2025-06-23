package com.shoppin.ecommerce.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppin.ecommerce.model.AddressModel;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, Integer>{

	List<AddressModel> findByUserEmail(String email);

	Optional<AddressModel> findByUserEmailAndAddressLine1AndAddressLine2AndTownAndBuildingAndPostalCodeAndFirstNameAndLastNameAndContactNumber(
			String username, String addressLine1, String addressLine2, String town, String building, String postalCode,
			String firstName, String lastName, String contactNumber);

	Optional<AddressModel> findByUserPkAndAddressLine1AndFirstNameAndContactNumber(Integer pk, String addressLine1,
			String firstName, String contactNumber);
}
