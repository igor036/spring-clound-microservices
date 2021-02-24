package com.linecode.payment.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.linecode.payment.dto.ProductDto;
import com.linecode.payment.util.DateUtil;

import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "product")
@Builder
public class Product implements Serializable, MapperToDto<ProductDto> {
    
    private static final long serialVersionUID = 1L;

    @Id
    private long id;

    @Column(name = "amount", nullable = false, length = 10)
    private int amount;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @DateTimeFormat(pattern = DateUtil.DEFAULT_FORMMAT)
    private Date lastModifiedDate;

    @Override
    public ProductDto convertToDto() {
        return new ModelMapper().map(this, ProductDto.class);
    }
}
