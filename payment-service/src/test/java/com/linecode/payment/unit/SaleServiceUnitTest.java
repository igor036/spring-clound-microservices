package com.linecode.payment.unit;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.linecode.payment.dto.ProductSaleDto;
import com.linecode.payment.dto.SaleDto;
import com.linecode.payment.exception.RestException;
import com.linecode.payment.exception.UnprocessableEntityException;
import com.linecode.payment.factory.SaleDtoFactory;
import com.linecode.payment.repository.SaleRepository;
import com.linecode.payment.service.SaleService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SaleServiceUnitTest extends UnitTest {
    
    @InjectMocks
    private SaleService saleService = new SaleService();

    @Mock(name = "saleRepository")
    private SaleRepository saleRepository;

    @BeforeMethod
    private void initMock() {
        MockitoAnnotations.openMocks(this);
        saleRepository = mock(SaleRepository.class);
        ReflectionTestUtils.setField(saleService, "saleRepository",saleRepository);
    }

    @Test
    public void testCreateWithNullObject() {
        try {
            saleService.create(null);
            fail(FAIL_MESSAGE);
        } catch(RestException ex) {
            assertException(ex, HttpStatus.UNPROCESSABLE_ENTITY, SaleService.SALE_OBJECT_NULL_ERROR_MESSAGE);
        }
    }

    @Test(dataProvider = "testCreateWithInvalidSaleDtoDataProvider")
    public void testCreateWithInvalidSaleDtoDataProvider(SaleDto sale, Collection<String> invalidFields) {
        try {
            saleService.create(sale);
            fail(FAIL_MESSAGE);
        } catch (UnprocessableEntityException ex) {
            assertUnprocessableEntityExceptionInvalidFields(ex, invalidFields);
        } catch (RestException ex) {
            assertException(ex, HttpStatus.BAD_REQUEST, SaleService.SALE_TOTAL_PRICE_ERROR_MESSAGE);
        }
    }

    @Test
    public void testFindByIdNonExistingSale() {

        when(saleRepository.findById(1L))
            .thenReturn(Optional.empty());

        try {
            saleService.findById(1L);
        } catch(RestException ex) {
            assertException(ex, HttpStatus.NOT_FOUND, SaleService.SALE_NOT_FOUND_ERROR_MESSAGE);

        }
    }

    @DataProvider(name = "testCreateWithInvalidSaleDtoDataProvider")
    public Object[][] testCreateWithInvalidSaleDtoDataProvider() {

        var saleDtoFactory = new SaleDtoFactory();

        var validProductSaleList        = Arrays.asList(new ProductSaleDto(1L, 1L, 2, 10d));
        var invalidProductSaleAmoutList = Arrays.asList(new ProductSaleDto(1L, 1L, 0, 10d));
        var invalidProductSalePriceList = Arrays.asList(new ProductSaleDto(1L, 1L, 1, 0d));
        
        var nullProducts         = saleDtoFactory.buildFakeInstance().toBuilder().products(null).build();
        var emptyProducts        = saleDtoFactory.buildFakeInstance().toBuilder().products(Collections.emptyList()).build();
        var invalidMinTotal      = saleDtoFactory.buildFakeInstance().toBuilder().total(2d).build();
        var invalidTotal         = saleDtoFactory.buildFakeInstance().toBuilder().products(validProductSaleList).total(10d).build();
        var invalidProductAmount = saleDtoFactory.buildFakeInstance().toBuilder().products(invalidProductSaleAmoutList).build();
        var invalidProductPrice  = saleDtoFactory.buildFakeInstance().toBuilder().products(invalidProductSalePriceList).build();

        //@formatter:off
        return new Object[][] {
            {nullProducts,         Arrays.asList("products")},
            {emptyProducts,        Arrays.asList("products")},
            {invalidMinTotal,      Arrays.asList("total")},
            {invalidTotal,         Arrays.asList("total")},
            {invalidProductAmount, Arrays.asList("amount")},
            {invalidProductPrice,  Arrays.asList("price")}
        };
        //@formatter:on
    }

    private void assertException(RestException ex, HttpStatus expectedStatus, String expectedMessage) {
        assertRestExceptionStatusCode(ex, expectedStatus);
        assertRestExceptionMessage(ex, expectedMessage);
    }
}
