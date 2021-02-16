package com.linecode.product.amqp;

import com.linecode.product.dto.ProductMessageDto;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductProducer {
    
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
    }

    public void sendUpdateProductMessage(ProductMessageDto productMessageDto) {
        rabbitTemplate.convertAndSend(exchange, updateProductRoutingKey, productMessageDto);
    }

    public void sendDeleteProductMessage(long productId) {
        rabbitTemplate.convertAndSend(exchange, deleteProductRoutingKey, productId);
    }
}
