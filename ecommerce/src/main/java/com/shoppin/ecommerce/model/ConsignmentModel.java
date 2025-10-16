package com.shoppin.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consignment")
@EntityListeners(AuditingEntityListener.class)
public class ConsignmentModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer pk;
	
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	@JsonBackReference("order_consignments")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private OrderModel parentOrder;
	
	@ManyToOne
	@JoinColumn(name = "seller_id", nullable = false)
	@JsonIgnore
	private CustomerModel seller;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;
	
	@ElementCollection
	private List<String> products;
	
	private boolean isOtpSent;
	
	@Column(nullable = true)
	@JsonIgnore
	private String otp;
	
	private boolean isOtpUsed;
	
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
