package com.linecode.product.factory;

import com.linecode.product.dto.ProductDto;

public class ProductDtoFactory implements FakeFactory<ProductDto> {

    @Override
    public ProductDto buildFakeInstance() {
        return ProductDto
            .builder()
            .name(FAKER.name().nameWithMiddle())
            .amount(FAKER.number().randomDigit())
            .price(FAKER.number().randomDouble(2, 200, 300))
            .build();
    }
}
