package com.linecode.payment.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.linecode.payment.entity.Sale;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SaleDto implements Serializable , MapperToEntity<Sale> {

    private static final long serialVersionUID = 1L;
    
    private long id;

    @NotEmpty(message = "The sale must have at least one product.")
    @NotNull(message = "The sale must have at least one product.")
    private List<ProductSaleDto> products;

    @Min(value = 10, message = "The minimum total sale value is 10.")
    private double total;

    @Override
    public Sale convertToEntity() {
        
        var saleEntity = new ModelMapper().map(this, Sale.class);

        //@formatter:off
        var productsEntityList = products
            .stream()
            .map(ProductSaleDto::convertToEntity)
            .collect(Collectors.toList());
        //@formatter:on

        saleEntity.setProducts(productsEntityList);
        return saleEntity;
    }
    
}
