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
public class ProductDto implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Min(value = 1L, message = "Invalid product id.")
    private long id;
    private int amount;
}
