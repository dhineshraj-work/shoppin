package com.shoppin.ecommerce.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseAddress {

	@NotNull
	private String email;
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	@NotNull
	@Size(max=50)
	private String addressLine1;
	
	@NotNull
	@Size(max=50)
	private String addressLine2;
	
	@NotNull
	@Size(max=20)
	private String town;
	
	@NotNull
	@Size(max=30)
	private String building;
	
	@NotNull
	@Size(max=10)
	private String postalCode;
	
	@NotNull
	@Size(max=20)
	private String landMark;
	
	@NotNull
	@Size(max=10)
	private String contactNumber;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AddressType addressType;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private DeliveryAddressType deliveryAddressType;
	
	@CreatedDate
	@JsonIgnore
	private LocalDateTime createdTime;
	
	@LastModifiedDate
	@JsonIgnore
	private LocalDateTime lastUpdatedTime;
	
	@CreatedBy
	@JsonIgnore
	private String createdBy;
	
	@LastModifiedBy
	@JsonIgnore
	private String lastModifiedBy;
}
