package com.linecode.payment.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;

import javax.annotation.Resource;

import com.linecode.payment.dto.ProductDto;
import com.linecode.payment.factory.ProductDtoFactory;
import com.linecode.payment.repository.ProductRepository;
import com.linecode.payment.service.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceIntegrationTest extends IntegrationTest {

    private static final ProductDtoFactory PRODUCT_DTO_FACTORY = new ProductDtoFactory();

    @Autowired
    private ProductService productService;

    @Resource
    private ProductRepository productRepository;

    @Before
    public void clearProducts() {
        productRepository.deleteAll();
    }

    @Test
    public void testCreateWithSuccess() {

        var expectedProduct = productService.create(PRODUCT_DTO_FACTORY.buildFakeInstance());
        var createdProduct  = productRepository.findById(expectedProduct.getId());

        assertTrue(createdProduct.isPresent());
        assertEquals(expectedProduct, createdProduct.get().convertToDto());
    }

    @Test
    public void testUpdateWithSuccess() {

        var product = PRODUCT_DTO_FACTORY.buildFakeInstance().convertToEntity();
        product.setAmount(10);
        product = productRepository.save(product);
        
        //@formatter:off
        var productDto = ProductDto.builder()
            .id(product.getId())
            .amount(0)
            .build();   
        //@formatter:on 

        productService.update(productDto);
        var updatedProduct = productRepository.findById(product.getId());

        assertTrue(updatedProduct.isPresent());
        assertEquals(0, updatedProduct.get().getAmount());
    }

    @Test
    public void testUpdateNonExistingProduct() {
        try {
            productService.update(PRODUCT_DTO_FACTORY.buildFakeInstance());
            fail(FAIL_MESSAGE);
        } catch (NoSuchElementException ex) {
            assertEquals(ProductService.ERROR_UPDATE_NON_EXISTING_PRODUCT, ex.getMessage());
        }
    }

    @Test
    public void testDeleteWithSuccess() {

        var product = PRODUCT_DTO_FACTORY.buildFakeInstance().convertToEntity();
        productRepository.save(product);

        productService.delete(product.getId());
        var deletedProduct = productRepository.findById(product.getId());

        assertTrue(deletedProduct.isEmpty());
    }

    @Test
    public void testDeleteNonExistingProduct() {
        try {
            productService.delete(1L);
            fail(FAIL_MESSAGE);
        } catch (NoSuchElementException ex) {
            assertEquals(ProductService.ERROR_DELETE_NON_EXISTING_PRODUCT, ex.getMessage());
        }
    }
}
