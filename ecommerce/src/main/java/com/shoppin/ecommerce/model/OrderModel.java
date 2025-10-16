package com.shoppin.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class OrderModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer pk;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "pk", nullable = false)
	@JsonIgnore
	private CustomerModel user;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@JsonManagedReference("order_entries")
	private List<OrderEntryModel> orderEntries;
	
	@ManyToOne
	@JoinColumn(name = "payment_address_id", referencedColumnName = "pk", nullable = false)
	private PaymentAddressModel paymentAddress;	
	
	@ManyToOne
	@JoinColumn(name = "delivery_address_id", referencedColumnName = "pk", nullable = false)
	private DeliveryAddressModel deliveryAddress;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethods paymentMethod;
	
	@Column(unique = true, nullable = false)
	private String orderNumber;
	
	private boolean isPaymentCompleted;
	
	private double discount;
	
	private double totalPrice;
	
	@OneToMany(mappedBy = "parentOrder", cascade = CascadeType.ALL)
	@JsonManagedReference("order_consignments")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private List<ConsignmentModel> consignments;
	
	@CreatedDate
	private LocalDateTime createdTime;
	
	@LastModifiedDate
	private LocalDateTime lastUpdatedTime;
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedBy
	private String lastModifiedBy;
	
}
