package com.linecode.product.amqp;

import com.linecode.product.dto.ProductMessageDto;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductProducer {

    public static final String CREATE_LOG_MESSAGE = "Send create product amqp message!";
    public static final String UPDATE_LOG_MESSAGE = "Send update product amqp message!";
    public static final String DELETE_LOG_MESSAGE = "Send delete product amqp message!";

    @Value("${product.amqp.exchange}")
    private String exchange;

    @Value("${product.amqp.routingkey.create}")
    private String createProductRoutingKey;

    @Value("${product.amqp.routingkey.update}")
    private String updateProductRoutingKey;

    @Value("${product.amqp.routingkey.delete}")
    private String deleteProductRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendCreateProductMessage(ProductMessageDto productMessageDto) {
        rabbitTemplate.convertAndSend(exchange, createProductRoutingKey, productMessageDto);
        log.info(CREATE_LOG_MESSAGE);
    }

    public void sendUpdateProductMessage(ProductMessageDto productMessageDto) {
        rabbitTemplate.convertAndSend(exchange, updateProductRoutingKey, productMessageDto);
        log.info(UPDATE_LOG_MESSAGE);
    }

    public void sendDeleteProductMessage(long productId) {
        rabbitTemplate.convertAndSend(exchange, deleteProductRoutingKey, productId);
        log.info(DELETE_LOG_MESSAGE);
    }
}
