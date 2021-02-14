package com.linecode.payment.factory;

import com.linecode.payment.dto.ProductSaleDto;

public class ProductSaleDtoFactory implements FakeFactory<ProductSaleDto> {

    @Override
    public ProductSaleDto buildFakeInstance() {
        //@formatter:off
        return ProductSaleDto
            .builder()
            .id(FAKER.random().nextLong())
            .amount(FAKER.random().nextInt(10))
            .price(FAKER.random().nextInt(20, 100))
            .build();
        //@formatter:on
    }    
}
