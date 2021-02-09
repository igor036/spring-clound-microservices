package com.linecode.product.factory;

import com.github.javafaker.Faker;
import com.linecode.product.dto.ProductDto;

public class ProductDtoFactory implements FakeFactory<ProductDto> {

    @Override
    public ProductDto buildFakeInstance() {
        
        var faker   = new Faker();
        var request = new ProductDto();

        request.setName(faker.name().nameWithMiddle());
        request.setAmount(faker.number().randomDigit());
        request.setPrice(faker.number().randomDouble(2, 200, 300));

        return request;
    }
}
