package com.linecode.payment.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;

import com.linecode.payment.entity.ProductSale;
import com.linecode.linecodeframework.dto.MapperToEntity;

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
@Builder(toBuilder=true)
public class ProductSaleDto implements Serializable, MapperToEntity<ProductSale> {

    private static final long serialVersionUID = 1L;

    private long id;

    @Min(value = 1L, message = "Invalid product id.")
    private long productId;

    @Min(value = 1, message = "Invalid product amount.")
    private int amount;

    @Min(value = 10, message = "The minimum price is 10.")
    private double price;


    public double getTotal() {
        return price * amount;
    }

    @Override
    public ProductSale convertToEntity() {
        return new ModelMapper().map(this, ProductSale.class);
    }

}
