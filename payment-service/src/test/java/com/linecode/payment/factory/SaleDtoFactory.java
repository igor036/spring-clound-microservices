package com.linecode.payment.factory;

import java.util.List;

import com.linecode.payment.dto.ProductSaleDto;
import com.linecode.payment.dto.SaleDto;

public class SaleDtoFactory implements FakeFactory<SaleDto> {

    @Override
    public SaleDto buildFakeInstance() {

        var products = buildProducts();
        var total    = calculateTotalPriceOfProducts(products);
        
        //@formatter:off
        return SaleDto
            .builder()
            .products(products)
            .total(total)
            .build();
        //@formatter:on
    }
   
    private List<ProductSaleDto> buildProducts() {
        return new ProductSaleDtoFactory()
            .buildFakeInstance(FAKER.random().nextInt(10));
    }

    private double calculateTotalPriceOfProducts(List<ProductSaleDto> products) {
        //@formatter:off
        return products
            .stream()
            .reduce(0.0, (partialResult, productSale) -> partialResult + productSale.getTotal(), Double::sum);
        //@formatter:on
    }
    
}
