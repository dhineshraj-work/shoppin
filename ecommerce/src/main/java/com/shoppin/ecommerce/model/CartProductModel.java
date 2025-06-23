package com.shoppin.ecommerce.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_product")
@EntityListeners(AuditingEntityListener.class)
public class CartProductModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@JsonIgnore
	private Integer pk;
	
	@ManyToOne
	@JoinColumn(name = "cart_id", referencedColumnName = "pk", nullable = false)
	@JsonBackReference
	private CartModel cart;
	
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "pk", nullable = false)
	private ProductModel product;
	
	private int quantity;
	
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
