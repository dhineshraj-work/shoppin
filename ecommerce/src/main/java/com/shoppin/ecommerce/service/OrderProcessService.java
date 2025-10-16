package com.shoppin.ecommerce.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppin.ecommerce.model.ConsignmentModel;
import com.shoppin.ecommerce.model.OrderEntryModel;
import com.shoppin.ecommerce.model.OrderModel;
import com.shoppin.ecommerce.model.ProductModel;
import com.shoppin.ecommerce.repo.OrderRepository;
import com.shoppin.ecommerce.repo.ProductRepository;

@Service
public class OrderProcessService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(OrderProcessService.class);

	public void afterPlacelOrderProcess(String orderNumber) {
		
		LOGGER.info("Order Process Started "+ orderNumber);
		
		OrderModel orderModel = orderRepository.findByOrderNumber(orderNumber).orElse(null);
		
		updateStocks(orderModel.getOrderEntries());
		
		triggerMailToSeller(orderModel.getConsignments());
		
		triggerMailToCustomer(orderModel);
		
		LOGGER.info("Order Process Completed "+orderNumber);
		
		
	}

	public void afterCancelOrderProcess(String orderNo) {
		// TODO Auto-generated method stub
	}
	
	private void triggerMailToCustomer(OrderModel orderModel) {
		
		LOGGER.info("Mail Sent To Customer "+orderModel.getUser().getEmail()+"  For the Order "+orderModel.getOrderNumber());
		
	}

	
	private void updateStocks(List<OrderEntryModel> orderEntries) {
		
		for(OrderEntryModel orderEntryModel:orderEntries) {
			
			ProductModel product = productRepository.findBySkuCode(orderEntryModel.getProduct().getSkuCode()).orElse(null);
		
			product.setStock(product.getStock()-orderEntryModel.getQuantity());
			
			productRepository.save(product);
		}
		
		LOGGER.info("Stock Updated for the Products");
	}
	
	private void triggerMailToSeller(List<ConsignmentModel> consignments) {
		
//		for(ConsignmentModel consignment:consignments) {
//			LOGGER.info("Mail Sent To Customer "+customer.getEmail());
//		}
		
		LOGGER.info("Mail Sent To Sellers ");
		
	}
}
