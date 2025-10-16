package com.shoppin.ecommerce.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shoppin.ecommerce.model.CartEntryModel;
import com.shoppin.ecommerce.model.CartModel;

@Service
public class CartCalculationService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(CartCalculationService.class);

	public CartModel calculateCart(CartModel cartModel) {
		
		LOGGER.info("In CartCalculationService: "+cartModel.getCustomer().getEmail());

		try {
			
			for(CartEntryModel cartEntry : cartModel.getCartEntries()) {
				
				if(cartEntry.getPricePerUnit()!=cartEntry.getProduct().getPrice()) {
					cartEntry.setPricePerUnit(cartEntry.getProduct().getPrice());
				}
				if(cartEntry.getDiscountPerUnit()!=cartEntry.getProduct().getDiscount()) {
					cartEntry.setDiscountPerUnit(cartEntry.getProduct().getDiscount());
				}
				
				cartEntry.setTotalPrice((cartEntry.getPricePerUnit() - cartEntry.getDiscountPerUnit())*cartEntry.getQuantity());
			}
			
			cartModel.setTotalPrice(cartModel.updateTotalPrice(cartModel.getCartEntries()));
			cartModel.setCartDiscountPrice(cartModel.updateCartDiscountPrice(cartModel.getCartEntries()));
			
			LOGGER.info("Cart Calculated for {} with amount {}", cartModel.getCustomer().getFirstName(), cartModel.getTotalPrice());
			
			return cartModel;
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong :"+e.getMessage());
			return null;
		}
		
	}

}
