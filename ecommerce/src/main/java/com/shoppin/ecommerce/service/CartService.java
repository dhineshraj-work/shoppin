package com.shoppin.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoppin.ecommerce.model.CartEntryModel;
import com.shoppin.ecommerce.model.CartModel;
import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.model.ProductModel;
import com.shoppin.ecommerce.repo.CartEntryRepository;
import com.shoppin.ecommerce.repo.CartRepository;
import com.shoppin.ecommerce.repo.CustomerRepository;
import com.shoppin.ecommerce.repo.ProductRepository;

@Service
public class CartService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CartEntryRepository cartEntryRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CartCalculationService calculationService;	

	private final Logger LOGGER = LoggerFactory.getLogger(CartService.class);
	
	public ResponseEntity<Object> getCart(String username) {
		try {
			
			LOGGER.info("Get Cart Request " + username);
			
			CartModel cartModel = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cartModel==null) {
				
				LOGGER.info("Cart is null " + username);
			
				return ResponseEntity.status(404).body("No Cart Available");
			
			}
			
			CartModel cart = calculationService.calculateCart(cartModel);
			
			cart= cartRepository.save(cartModel);
			
			LOGGER.info("Cart Details Sent " + username);
			
			return ResponseEntity.ok(cart);
		
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while get Cart : "+username+"\n"+e.getMessage());
		
			return ResponseEntity.status(500).body("Something went wrong\n"+e);
		
		}
	
	}

	public ResponseEntity<Object> addProductToCart(String username, String sku) {
		
		try {
		
			LOGGER.info("Customer add product to Cart : "+sku+" User : "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			ProductModel product = productRepository.findBySkuCode(sku).orElse(null);
			
			if(product==null) {
				
				LOGGER.info("Product not found : "+sku+" User : "+username);
				
				return ResponseEntity.status(404).body("Product Not Found");
			}
			
			if(product.getStock()<1) {
				
				LOGGER.info("No Stock : "+sku+" User : "+username);
				
				return ResponseEntity.status(409).body("There is no stock available for this product : "+ sku);
			}
			
			if(cart!=null && cartEntryRepository.existsByCartCustomerPkAndProductSkuCode(customer.getPk(), sku)) {
				
				LOGGER.info("The product {} already added in user cart : {} hence adding the quantity",sku,username);
				
				CartEntryModel cEntryModel = cart.getCartEntries().stream()
													.filter( ce -> ce.getProduct().getSkuCode().equals(sku) )
													.findFirst()
													.orElse(null);
				
				int quantity = cEntryModel.getQuantity()+1;
				
				LOGGER.info("Called update Cart Method for product {} with Quantity {}", sku, quantity);
				
				return updateCart(username, sku, quantity);
			}
			
			if(cart==null) {
				
				cart = new CartModel();				
				cart.setCustomer(customer);
				cart = cartRepository.save(cart);
			}
			
			List<CartEntryModel> cartProducts = cart.getCartEntries();
			
			if(cartProducts==null) {
				cartProducts = new ArrayList<>();
				cart.setCartEntries(cartProducts);
			}
			
			CartEntryModel cartEntryModel = new CartEntryModel();
			cartEntryModel.setCart(cart);
			cartEntryModel.setProduct(product);
			cartEntryModel.setQuantity(1);
			cartEntryModel.setPricePerUnit(product.getPrice());
			cartEntryModel.setDiscountPerUnit(product.getDiscount());
			cartEntryModel.setTotalPrice((cartEntryModel.getPricePerUnit() - cartEntryModel.getDiscountPerUnit())*cartEntryModel.getQuantity());
			 
			cartProducts.add(cartEntryModel);
			cart.setTotalPrice(cart.updateTotalPrice(cart.getCartEntries()));
			cart.setCartDiscountPrice(cart.updateCartDiscountPrice(cart.getCartEntries()));
			cartEntryRepository.save(cartEntryModel);
			
			calculationService.calculateCart(cart);		
			
			cartRepository.save(cart);

			LOGGER.info("The product successfully added in cart : "+sku+" User : "+username);
			
			return ResponseEntity.ok(cart);
			
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while adding product to cart : "+sku+" User : "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong\n"+e.getMessage());
		}
	}

	public ResponseEntity<Object> updateCart(String username, String sku, Integer quantity) {
		
		try {
			
			LOGGER.info("Customer update product to Cart : "+sku+" User : "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cart==null) {
				
				LOGGER.info("No Cart Found : "+sku+" User : "+username);
				
				return ResponseEntity.status(404).body("No Cart Found");
				
			}
			
			CartEntryModel cartProduct = cartEntryRepository.findByCartCustomerPkAndProductSkuCode(customer.getPk(), sku).orElse(null);
			
			if(!cartEntryRepository.existsByCartCustomerPkAndProductSkuCode(customer.getPk(), sku)) {
				
				LOGGER.info("Product not available in Cart : "+sku+" User : "+username);
				
				return ResponseEntity.status(404).body("The Product is not available in your cart");
			}
			
			if(quantity==0) {
				
				LOGGER.info("Customer update product to Cart with 0 quantity : "+sku+" User : "+username);
				
				cartEntryRepository.deleteByCartCustomerPkAndProductSkuCode(customer.getPk(), sku);
				
				LOGGER.info("Product deleted from Cart : "+sku+" User : "+username);
			
				cartRepository.save(cart);
				
				if(cart.getCartEntries().isEmpty()) {

					if(deleteCart(username)) {
						return ResponseEntity.ok("No Product in Cart");
					}
				}
				
				return ResponseEntity.ok("Cart Updated");
			}
			
			if(quantity>cartProduct.getProduct().getStock()) {
				
				LOGGER.info("Quantity is added more than the available stock : "+sku+" User : "+username);
				
				return ResponseEntity.status(409).body("The Product stock available : "+cartProduct.getProduct().getStock());
			}
			
			cartProduct.setQuantity(quantity);
			
			cartEntryRepository.save(cartProduct);
			
			calculationService.calculateCart(cart);
			
			cartRepository.save(cart);
			
			LOGGER.info("Cart successfully updated : "+sku+" User : "+username);
			
			return ResponseEntity.ok(cartRepository.findByCustomerEmail(username));
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while updating product to cart : "+sku+" User : "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while updating cart\n"+e.getMessage());
		}
	}

	public ResponseEntity<Object> removeProductFromCart(String username, String sku) {
		
		try {
			
			LOGGER.info("Customer request to delete a product in cart : "+sku+" User : "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cart==null) {
				
				LOGGER.info("No Cart Found : "+sku+" User : "+username);
				
				return ResponseEntity.status(404).body("No Cart Found");
				
			}
			
			CartEntryModel cartProduct = cartEntryRepository.findByCartCustomerPkAndProductSkuCode(customer.getPk(), sku).orElse(null);
			
			if(cartProduct == null) {
				
				LOGGER.info("Product not found in Cart : "+sku+" User : "+username);
				
				return ResponseEntity.status(404).body("The Product is not available in your cart");
				
			}
			cartEntryRepository.deleteByCartCustomerPkAndProductSkuCode(customer.getPk(), sku);

			cart = calculationService.calculateCart(cart);
			
			cartRepository.save(cart);
			
			if(cart.getCartEntries().isEmpty()) {
				
				if(deleteCart(username)) {
					return ResponseEntity.ok("No Product in Cart");
				}
				
			}
			
			LOGGER.info("Product removed from cart : "+sku+" User : "+username);
			
			return ResponseEntity.ok(cart);
		
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while removing product : "+sku+" User : "+username+"\n"+e.getMessage());
		
			return ResponseEntity.status(500).body("Something went wrong while Deleting cart\n"+e.getMessage());
		
		}
	}
	
	private boolean deleteCart(String username) {
		try {
			
			CartModel cart = cartRepository.findByCustomerEmail(username).orElse(null);
			
			if(cart==null) {
				return false;
			}
			
			cartRepository.deleteByCustomerEmail(username);
			
			LOGGER.info("Cart Deleted: {} {}", username);
			
			return true;
			
		}catch (Exception e) {
			LOGGER.error("Something went wrong while Cart Delete: {} {}", username, e);
			return false;
		}
	}
	
}
