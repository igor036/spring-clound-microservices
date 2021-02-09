package com.linecode.product.service;

import com.linecode.product.dto.ProductDto;
import com.linecode.product.entity.Product;
import com.linecode.product.exception.RestException;
import com.linecode.product.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.linecode.product.util.ValidatorUtil.assertConstraints;
import static com.linecode.product.util.ValidatorUtil.assertNotNull;

import java.util.Optional;

@Service
public class ProductService {

    public static final String PRODUCT_OBJECT_NULL_ERROR_MESSAGE = "Please enter a product.";
    public static final String PRODUCT_NAME_ALREADY_EXISTING = "%s already existing!";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    
    @Autowired
    private ProductRepository productRepository;

    public ProductDto create(ProductDto productDto) {

        assertNotNull(productDto, PRODUCT_OBJECT_NULL_ERROR_MESSAGE);
        assertConstraints(productDto);

        var product = save(productDto.convertToProduct());

        return product.convertToProductDTO();
    }

    public ProductDto update(long id, ProductDto productDto) {
        
        assertNotNull(productDto, PRODUCT_OBJECT_NULL_ERROR_MESSAGE);

        var product = get(id);
        assertConstraints(productDto);

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setAmount(productDto.getAmount());

        product = save(product);
        return product.convertToProductDTO();
    }

    public ProductDto delete(long id) { 
        var product = get(id);
        productRepository.delete(product);
        return product.convertToProductDTO();
    }

    public ProductDto findById(long id) {
        var product = get(id);
        return product.convertToProductDTO();
    }

    public Page<ProductDto> findAll(int page, int limit, String direction) {
        var pageable = gePageable(page, limit, direction);
        return productRepository
            .findAll(pageable)
            .map(Product::convertToProductDTO);

    }

    private Product get(long id) {

        Optional<Product> product = productRepository.findById(id);
        
        if (product.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND);
        }

        return product.get();
    }

    private Product save(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            final var message = String.format(PRODUCT_NAME_ALREADY_EXISTING, product.getName());
            throw new RestException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private Pageable gePageable(int page, int limit, String direction) {
        return PageRequest.of(page, limit, Sort.by(Direction.fromString(direction), "name"));
    }
}
