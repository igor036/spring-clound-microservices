package com.linecode.payment.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.linecode.payment.dto.ProductSaleDto;
import com.linecode.payment.dto.SaleDto;
import com.linecode.payment.factory.SaleDtoFactory;
import com.linecode.payment.repository.SaleRepository;
import com.linecode.payment.service.SaleService;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class SaleControllerIntegrationTest extends IntegrationTest {
 
    private static final String SALE_CONTROLLER_URL = "/sale";
    private static final SaleDtoFactory SALE_DTO_FACTORY = new SaleDtoFactory();

    @Resource
    private SaleRepository saleRepository;

    @Test
    public void testCreateWithSuccess() {

        var saleDto    = SALE_DTO_FACTORY.buildFakeInstance();
        var httpEntity = new HttpEntity<>(saleDto);
        var response   = restTemplate.exchange(SALE_CONTROLLER_URL, HttpMethod.PUT, httpEntity, SaleDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saleDto.getTotal(), response.getBody().getTotal());
        assertSaleProductList(saleDto.getProducts(), response.getBody().getProducts());
    }

    @Test
    public void testCreateWithInvalidTotal() {

        var saleDto = SALE_DTO_FACTORY.buildFakeInstance();
        saleDto.setTotal(saleDto.getTotal() + 10d);

        var httpEntity = new HttpEntity<>(saleDto);
        var response   = restTemplate.exchange(SALE_CONTROLLER_URL, HttpMethod.PUT, httpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(SaleService.SALE_TOTAL_PRICE_ERROR_MESSAGE, response.getBody());
    }

    @Test
    public void testFindByIdNonExistingSale() {

        var response = restTemplate.exchange(SALE_CONTROLLER_URL+"/100", HttpMethod.GET, null, String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(SaleService.SALE_NOT_FOUND_ERROR_MESSAGE, response.getBody());
    }

    @Test
    public void testFindByIdWithSuccess() {

        var created  = saleRepository.save(SALE_DTO_FACTORY.buildFakeInstance().convertToEntity()).convertToDto();
        var response = restTemplate.exchange(SALE_CONTROLLER_URL+"/"+created.getId(), HttpMethod.GET, null, SaleDto.class);
        
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(created.getId(), response.getBody().getId());
        assertEquals(created.getTotal(), response.getBody().getTotal());
    }

    @Test
    public void testPaginatedSearch() {

        createSalesForPaginatedSearchTest(12);

        var firstPageResponse = restTemplate.exchange(SALE_CONTROLLER_URL, HttpMethod.GET, null, Map.class);
        assertEquals(HttpStatus.FOUND, firstPageResponse.getStatusCode());

        var firstPage = firstPageResponse.getBody();
        assertEquals(2, firstPage.get("totalPages"));
        assertEquals(12, firstPage.get("totalElements"));
        assertEquals(10, firstPage.get("size"));
        assertEquals(10, firstPage.get("numberOfElements"));
        assertEquals(true, firstPage.get("first"));
        assertEquals(false, firstPage.get("last"));

        var lastPageResponse = restTemplate.exchange(SALE_CONTROLLER_URL+"?page=1", HttpMethod.GET, null, Map.class);
        assertEquals(HttpStatus.FOUND, lastPageResponse.getStatusCode());

        var lastPage = lastPageResponse.getBody();
        assertEquals(2, lastPage.get("totalPages"));
        assertEquals(12, lastPage.get("totalElements"));
        assertEquals(10, firstPage.get("size"));
        assertEquals(2, lastPage.get("numberOfElements"));
        assertEquals(false, lastPage.get("first"));
        assertEquals(true, lastPage.get("last"));

        var noElementsFoundResponse = restTemplate.exchange(SALE_CONTROLLER_URL+"?page=4", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.NOT_FOUND, noElementsFoundResponse.getStatusCode());
        assertEquals(SaleService.ELEMENTS_NOT_FOUND_ERROR_MESSAGE, noElementsFoundResponse.getBody());

    }


    private void createSalesForPaginatedSearchTest(int amount) {
        //@formatter:off
        SALE_DTO_FACTORY
            .buildFakeInstance(amount)
            .stream()
            .map(SaleDto::convertToEntity)
            .collect(Collectors.toList())
            .stream()
            .forEach(saleRepository::save);
        //@formatter:off
    }
    
    
    private void assertSaleProductList(List<ProductSaleDto> expected, List<ProductSaleDto> actual) {
        assertEquals(expected.size(), actual.size());
        expected.forEach(productSale -> assertTrue(actual.contains(productSale)));
    }
}
