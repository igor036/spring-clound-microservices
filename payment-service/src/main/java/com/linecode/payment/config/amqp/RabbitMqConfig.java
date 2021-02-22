package com.linecode.payment.config.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    
    @Value("${product.amqp.exchange}")
    private String exchange;

    @Value("${product.amqp.routingkey.create}")
    private String createProductRoutingKey;

    @Value("${product.amqp.routingkey.update}")
    private String updateProductRoutingKey;

    @Value("${product.amqp.routingkey.delete}")
    private String deleteProductRoutingKey;

    @Bean(name = "productTopicExchange")
    public TopicExchange productTopicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean(name = "createProductQueue")
    public Queue createProductQueue() {
        return new Queue("create", false);
    }

    @Bean(name = "createProductBinding")
    public Binding createProductBinding(
        @Qualifier("createProductQueue") Queue queue, 
        @Qualifier("productTopicExchange") TopicExchange topic) {
        return BindingBuilder.bind(queue).to(topic).with(createProductRoutingKey);
    }

    @Bean(name = "updateProductQueue")
    public Queue updateProductQueue() {
        return new Queue("update", false);
    }

    @Bean(name = "updateProductBinding")
    public Binding updateProductBinding(
        @Qualifier("updateProductQueue") Queue queue, 
        @Qualifier("productTopicExchange") TopicExchange topic) {
        return BindingBuilder.bind(queue).to(topic).with(updateProductRoutingKey);
    }

    @Bean(name = "deleteProductQueue")
    public Queue deleteProductQueue() {
        return new Queue("delete", false);
    }

    @Bean(name = "deleteProductBinding")
    public Binding deleteProductBinding(
        @Qualifier("deleteProductQueue") Queue queue, 
        @Qualifier("productTopicExchange") TopicExchange topic) {
        return BindingBuilder.bind(queue).to(topic).with(deleteProductRoutingKey);
    }

    @Bean("jsonMessageConverter")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
