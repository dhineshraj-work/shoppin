//package com.shoppin.ecommerce.model;
//
//import java.math.BigDecimal;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotNull;
//
//@Entity
//@Table(name = "payment")
//public class PaymentTransaction {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer pk;
//	
//	private String orderNumber;
//	
//	@NotNull
//	private String transactionNumber;
//	
//	@NotNull
//	private BigDecimal amount;
//	
//	@NotNull
//	private String currency;
//
//}
