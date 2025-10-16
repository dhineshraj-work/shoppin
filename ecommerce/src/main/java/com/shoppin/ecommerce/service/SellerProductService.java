package com.shoppin.ecommerce.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppin.ecommerce.dto.ProductModelDto;
import com.shoppin.ecommerce.model.CategoryModel;
import com.shoppin.ecommerce.model.CustomerModel;
import com.shoppin.ecommerce.model.ProductModel;
import com.shoppin.ecommerce.repo.CategoryRepository;
import com.shoppin.ecommerce.repo.CustomerRepository;
import com.shoppin.ecommerce.repo.ProductRepository;

@Service
public class SellerProductService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(SellerProductService.class);
	
	public ResponseEntity<?> getProductBySeller(String username) {
		
		try {
			
			LOGGER.info("Get Seller Produts "+username);
			
			List<ProductModel> products = productRepository.findBySellerEmail(username);
			
			if(products.isEmpty()|| products==null) {
				
				LOGGER.info("No Produts Found "+username);
				
				return ResponseEntity.status(404).body("No Produts Found");
			}
			
			LOGGER.info("Seller Produts returned successfully "+username);
			
			return ResponseEntity.ok(products);
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while getting prouct "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while getting proucts\n"+e.getMessage());
		}
	}
	
	public ResponseEntity<Object> getProduct(String username, String sku) {
		try {
			
			LOGGER.info("Get Seller Produt by SKU code "+sku+" Seller "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			ProductModel product = productRepository.findBySkuCodeAndSellerPk(sku, customer.getPk()).orElse(null);
			
			if(product==null) {
				
				LOGGER.info("No Produts Found "+username);
				
				return ResponseEntity.status(404).body("Product Not Found");
			}
			
			LOGGER.info("Seller Produt returned successfully "+sku+" Seller "+username);
			
			return ResponseEntity.ok(product);
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while getting prouct "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body(null);
		}
	}

	public ResponseEntity<Object> addProduct(String username, String productDto, MultipartFile image) {
		
		try {
			
			LOGGER.info("Seller add new prdouct process started "+username);
			
			ProductModelDto product = convertStringToJson(productDto);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			ProductModel productModel = new ProductModel();
			
			if(productRepository.existsBySellerPkAndProductName(customer.getPk(), product.getProductName())) {
				
				LOGGER.info("Product already available in seller account "+username);
				
				return ResponseEntity.status(409).body("Product already added on your account");
			}
			
			CategoryModel category = categoryRepository.findByCategoryName(product.getCategoryName()).orElse(null);
			
			if(category==null) {
				category = new CategoryModel(product.getCategoryName());
				categoryRepository.save(category);
			}
			
			productModel.setProductName(product.getProductName());
			productModel.setAvailable(product.isAvailable());
			productModel.setBrand(product.getBrand());
			productModel.setDescription(product.getDescription());
			productModel.setDiscount(product.getDiscount());
			productModel.setPrice(product.getPrice());
			productModel.setStock(product.getStock());
			productModel.setCategory(category);
			productModel.setSeller(customer);
			productModel.setSkuCode(generateNewProductSku());
			productModel.setImagename(image.getOriginalFilename());
			productModel.setImageType(image.getContentType());
			productModel.setImage(image.getBytes());
			
			productRepository.save(productModel);
			
			LOGGER.info("Seller add new product process ended "+username);
			
			LOGGER.info("Seller add new product successfully "+productModel.getSkuCode()+" Seller "+username);
			
			return ResponseEntity.status(201).body(productRepository.findByProductName(product.getProductName()).orElse(null));
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while adding new prouct "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while adding prouct"+e.getMessage());
		}
	}

	
	public ResponseEntity<Object> updateProduct(String username, ProductModelDto product, String sku) {
		try {
			
			LOGGER.info("Seller update prdouct process started "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			ProductModel productModel = productRepository.findBySkuCodeAndSellerPk(sku, customer.getPk()).orElse(null);
			
			if(productModel==null) {
				
				LOGGER.info("Product not found on your account "+username);
				
				return ResponseEntity.status(404).body("Product not found on your account");
			}
			
			CategoryModel category = categoryRepository.findByCategoryName(product.getCategoryName()).orElse(null);
			
			if(category==null) {
				category = new CategoryModel(product.getCategoryName());
				categoryRepository.save(category);
			}
			
			productModel.setCategory(category);
			productModel.setBrand(product.getBrand());
			productModel.setAvailable(product.isAvailable());
			productModel.setStock(product.getStock());
			productModel.setDescription(product.getDescription());
			productModel.setDiscount(product.getDiscount());
			productModel.setPrice(product.getPrice());
			productModel.setProductName(product.getProductName());
			
			productModel = productRepository.save(productModel);
			
			LOGGER.info("Seller update product process ended "+username);
			
			LOGGER.info("Seller update product successfully "+productModel.getSkuCode()+" Seller "+username);
			
			return ResponseEntity.ok(productModel);
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while updating prouct "+sku+" "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while update prouct\n"+sku+" "+e.getMessage());
		}
	}
	
	public ResponseEntity<Object> updateProductImage(String username, MultipartFile image, String sku) {
		try {
			
			LOGGER.info("Seller update prdouct image process started "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			ProductModel productModel = productRepository.findBySkuCodeAndSellerPk(sku, customer.getPk()).orElse(null);
			
			if(productModel==null) {
				
				LOGGER.info("Product not found on your account "+username);
				
				return ResponseEntity.status(404).body("Product already added on your account");
			}
			productModel.setImagename(image.getOriginalFilename());
			productModel.setImageType(image.getContentType());
			productModel.setImage(image.getBytes());
			
			productModel = productRepository.save(productModel);
			
			LOGGER.info("Seller update product image process ended "+username);
			
			LOGGER.info("Seller update product image successfully "+productModel.getSkuCode()+" Seller "+username);
			
			return ResponseEntity.ok(productModel);
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while updating prouct image "+sku+" "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while update prouct iamge\n"+sku+" "+e.getMessage());
		}
	}
	
	public ResponseEntity<Object> deleteProduct(String username, String sku) {
		try {
			
			LOGGER.info("Seller delete prdouct process started "+username);
			
			CustomerModel customer = customerRepository.findByEmail(username).orElse(null);
			
			ProductModel productModel = productRepository.findBySkuCodeAndSellerPk(sku, customer.getPk()).orElse(null);
			
			if(productModel==null) {
				
				LOGGER.info("Product not found on your account "+username);
				
				return ResponseEntity.status(404).body("Product already added on your account");
			}
			
			productRepository.deleteBySkuCodeAndSellerPk(sku, customer.getPk());
			
			LOGGER.info("Seller delete product image process ended "+username);
			
			LOGGER.info("Seller delete product image successfully "+sku+" Seller "+username);
			
			return ResponseEntity.ok("Product Deleted :"+sku);
			
		}catch(Exception e) {
			
			LOGGER.info("Something went wrong while deleting prouct "+sku+" "+username+"\n"+e.getMessage());
			
			return ResponseEntity.status(500).body("Something went wrong while deleting prouct\n"+sku+" "+e);
		}
	}

	private String generateNewProductSku() {
		Integer lastProductInDB = productRepository.findMaxSkuCodeAsInt();
		
		int sku = (lastProductInDB==null)?10001:lastProductInDB+1;
		
		return String.valueOf(sku);
	}
	
	private ProductModelDto convertStringToJson(String productDto) throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ProductModelDto product = new ProductModelDto();
		
		product = mapper.readValue(productDto, ProductModelDto.class);
		
		return product;
	}
	


}
