package com.linecode.payment.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class SaleDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private long id;

    @NotEmpty(message = "The sale must have at least one product.")
    @NotNull(message = "The sale must have at least one product.")
    private List<ProductSaleDto> products;

    @Min(value = 10, message = "The minimum total sale value is 10.")
    private double total;
    
}
