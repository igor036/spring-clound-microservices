package com.linecode.payment.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductSaleDto implements Serializable {

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

}