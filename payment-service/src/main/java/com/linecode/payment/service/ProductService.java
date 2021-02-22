package com.linecode.payment.service;

import com.linecode.payment.dto.ProductDto;
import com.linecode.payment.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductService {

    public static final String ERROR_PRODUCT_OBJECT_NULL = "Received Null product object.";
    public static final String ERROR_UPDATE_NON_EXISTING_PRODUCT = "Error when try update a non existing product.";
    public static final String ERROR_DELETE_NON_EXISTING_PRODUCT = "Error when try delete a non existing product.";
    
    @Autowired
    private ProductRepository productRepository;

    public ProductDto create(ProductDto productDto) {
        
        assertProductDto(productDto);

        var product = productDto.convertToEntity();
        product = productRepository.save(product);

        return product.convertToDto();
    }

    public ProductDto update(ProductDto productDto) {
        
        assertProductDto(productDto);
        assertExistingProduct(productDto.getId(), ERROR_UPDATE_NON_EXISTING_PRODUCT);

        var product = productDto.convertToEntity();
        product = productRepository.save(product);

        return product.convertToDto();
    }

    public void delete(long id) {
        assertExistingProduct(id, ERROR_DELETE_NON_EXISTING_PRODUCT);
        productRepository.deleteById(id);
    }

    private void assertExistingProduct(long id, String notFoundErrorMessage) {
        var product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NoSuchElementException(notFoundErrorMessage);
        }
    }

    private void assertProductDto(ProductDto productDto) {
        if (productDto == null) {
            throw new IllegalArgumentException(ERROR_PRODUCT_OBJECT_NULL);
        }
    }
}
