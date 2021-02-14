package com.linecode.product.factory;

import com.linecode.product.entity.Product;

public class ProductFactory implements FakeFactory<Product> {

    @Override
    public Product buildFakeInstance() {
        return Product
            .builder()
            .name(FAKER.name().nameWithMiddle())
            .amount(FAKER.number().randomDigit())
            .price(FAKER.number().randomDouble(2, 200, 300))
            .build();
    }    
}
