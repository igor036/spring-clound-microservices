package com.linecode.product.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.linecode.product.entity.Product;

import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductDto {

    private long id;

    @NotBlank(message = "The name is required.")
    @Size(min = 4, max = 255, message = "The name must be between 4 and 255 characters long.")
    private String name;

    @Min(value = 0, message = "The minimum quantity is 0.")
    private int amount;

    @Min(value = 10, message = "The minimum price is 10.")
    private double price;

    public Product convertToProduct() {
        return new ModelMapper().map(this, Product.class);
    }

}
