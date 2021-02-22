package com.linecode.payment.amqp;

import com.linecode.payment.dto.ProductDto;
import com.linecode.payment.service.ProductService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductConsumer {

    public static final String CREATE_LOG_MESSAGE = "Receiver create product amqp message!";
    public static final String CREATED_LOG_MESSAGE = "Create product by amqp message!";

    public static final String UPDATE_LOG_MESSAGE = "Receiver update product amqp message!";
    public static final String UPDATED_LOG_MESSAGE = "Updated product by amqp message!";

    public static final String DELETE_LOG_MESSAGE = "Receiver delete product amqp message!";
    public static final String DELETED_LOG_MESSAGE = "Deleted product by amqp message!";

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "#{createProductQueue}")
    public void createProduct(ProductDto productDto) throws InterruptedException {
        log.info(CREATE_LOG_MESSAGE);
        productService.create(productDto);
        log.info(CREATED_LOG_MESSAGE);
    }

    @RabbitListener(queues = "#{updateProductQueue}")
    public void updateProduct(ProductDto productDto) throws InterruptedException {
        log.info(UPDATE_LOG_MESSAGE);
        productService.update(productDto);
        log.info(UPDATED_LOG_MESSAGE);
    }

    @RabbitListener(queues = "#{deleteProductQueue}")
    public void updateProduct(long productId) throws InterruptedException {
        log.info(DELETE_LOG_MESSAGE);
        productService.delete(productId);
        log.info(DELETED_LOG_MESSAGE);
    }
}
