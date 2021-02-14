package com.linecode.payment.service;

import com.linecode.payment.dto.SaleDto;
import com.linecode.payment.entity.Sale;
import com.linecode.payment.exception.RestException;
import com.linecode.payment.repository.SaleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.linecode.payment.util.ValidatorUtil.assertNotNull;
import static com.linecode.payment.util.ValidatorUtil.assertConstraints;

@Service
public class SaleService {
 

    public static final String ELEMENTS_NOT_FOUND_ERROR_MESSAGE = "No elements found.";
    public static final String SALE_NOT_FOUND_ERROR_MESSAGE = "Sale not found.";
    public static final String SALE_OBJECT_NULL_ERROR_MESSAGE = "Please enter a sale data.";
    public static final String SALE_TOTAL_PRICE_ERROR_MESSAGE = "The total price sale is not valid, please, check products price and amount.";

    @Autowired
    private SaleRepository saleRepository;

    public SaleDto create(SaleDto saleDto) {

        assertNotNull(saleDto, SALE_OBJECT_NULL_ERROR_MESSAGE);
        assertConstraints(saleDto);
        assertSaleTotalPrice(saleDto);

        var createdSale = saleRepository.save(saleDto.convertToEntity());
        return createdSale.convertToDto();
    }

    public Page<SaleDto> findAll(int page, int limit, String direction) { 
        
        var pageable = gePageable(page, limit, direction);
        var sales    = saleRepository.findAll(pageable).map(Sale::convertToDto);

        if (sales.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND, ELEMENTS_NOT_FOUND_ERROR_MESSAGE);
        }

        return sales;
    }


    public SaleDto findById(long id) {

        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND, SALE_NOT_FOUND_ERROR_MESSAGE);
        }

        return sale.get().convertToDto();
    }

    private void assertSaleTotalPrice(SaleDto saleDto) {

        //@formatter:off
        var totalProducts = saleDto
            .getProducts().stream()
            .reduce(0.0, (partialResult, productSale) -> partialResult + productSale.getTotal(), Double::sum);
        //@formatter:on
        
        if (Double.compare(totalProducts, saleDto.getTotal()) != 0) {
            throw new RestException(HttpStatus.BAD_REQUEST, SALE_TOTAL_PRICE_ERROR_MESSAGE);
        }   
    }

    private Pageable gePageable(int page, int limit, String direction) {
        return PageRequest.of(page, limit, Sort.by(Direction.fromString(direction), "id"));
    }
}
