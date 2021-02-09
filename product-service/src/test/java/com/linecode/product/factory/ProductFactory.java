package com.linecode.product.factory;

import com.github.javafaker.Faker;
import com.linecode.product.entity.Product;

public class ProductFactory implements FakeFactory<Product> {

    @Override
    public Product buildFakeInstance() {
        
        var faker   = new Faker();
        var request = new Product();

        request.setName(faker.name().nameWithMiddle());
        request.setAmount(faker.number().randomDigit());
        request.setPrice(faker.number().randomDouble(2, 200, 300));

        return request;
    }    
}
