package com.linecode.payment.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.linecode.payment.dto.SaleDto;
import com.linecode.payment.util.DateUtil;
import com.linecode.linecodeframework.entity.MapperToDto;

import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sale")
public class Sale implements Serializable, MapperToDto<SaleDto> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = {CascadeType.REFRESH})
    private List<ProductSale> products;

    @Column(name = "total", nullable = false, length = 10)
    private double total;

    @CreatedDate
    @Column(name = "created_at")
    @DateTimeFormat(pattern = DateUtil.DEFAULT_FORMMAT)
    private Date createdAt;

    @Override
    public SaleDto convertToDto() {
        
        var saleDto = new ModelMapper().map(this, SaleDto.class);

        //@formatter:off
        var productDtoList = products
            .stream()
            .map(ProductSale::convertToDto)
            .collect(Collectors.toList());
        //@formatter:on

        saleDto.setProducts(productDtoList);
        return saleDto;
    }
    
}
