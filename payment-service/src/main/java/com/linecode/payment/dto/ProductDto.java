package com.linecode.payment.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;

import com.linecode.payment.entity.Product;
import com.linecode.linecodeframework.dto.MapperToEntity;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder=true)
@ToString
public class ProductDto implements Serializable, MapperToEntity<Product> {
    
    private static final long serialVersionUID = 1L;

    @Min(value = 1L, message = "Invalid product id.")
    private long id;
    private int amount;

    @Override
    public Product convertToEntity() {
        return new ModelMapper().map(this, Product.class);
    }
}
