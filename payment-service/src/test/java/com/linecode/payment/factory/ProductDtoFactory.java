package com.linecode.payment.factory;

import com.linecode.payment.dto.ProductDto;

public class ProductDtoFactory implements FakeFactory<ProductDto> {

    @Override
    public ProductDto buildFakeInstance() {
        //@formatter:off
        return ProductDto.builder()
            .id(FAKER.random().nextLong())
            .amount(FAKER.random().nextInt(20))
            .build();
        //@formatter:on
    }
    
}
