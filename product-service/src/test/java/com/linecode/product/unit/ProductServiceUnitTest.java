package com.linecode.product.unit;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.linecode.product.amqp.ProductProducer;
import com.linecode.product.dto.ProductDto;
import com.linecode.product.entity.Product;
import com.linecode.product.factory.ProductDtoFactory;
import com.linecode.product.repository.ProductRepository;
import com.linecode.product.service.ProductService;

import com.linecode.linecodeframework.exception.RestException;
import com.linecode.linecodeframework.exception.UnprocessableEntityException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ProductServiceUnitTest extends UnitTest {

    @InjectMocks
    private ProductService productService = new ProductService();

    @Mock(name = "productRepository")
    private ProductRepository productRepository;

    @Mock(name = "productProducer")
    private ProductProducer productProducer;

    @BeforeMethod
    private void initMock() {
        MockitoAnnotations.openMocks(this);
        productRepository = mock(ProductRepository.class);
        productProducer   = mock(ProductProducer.class);
        ReflectionTestUtils.setField(productService, "productRepository", productRepository);
        ReflectionTestUtils.setField(productService, "productProducer", productProducer);
    }

    @Test
    public void testCreateNullProductRequest() {
        try {
            productService.create(null);
            fail(FAIL_MESSAGE);
        } catch (RestException ex) {
            assertUnprocessableNullEntityException(ex);
        }
    }

    @Test
    public void testUpdateNullProductRequest() {
        try {
            productService.update(1L, null);
            fail(FAIL_MESSAGE);
        } catch (RestException ex) {
            assertUnprocessableNullEntityException(ex);
        }
    }

    @Test(dataProvider = "invalidProductRequestDataProvider")
    public void testCreateProductUnprocessedEntity(ProductDto productRequest, Collection<String> invalidFields) {
        try {
            productService.create(productRequest);
            fail(FAIL_MESSAGE);
        } catch (UnprocessableEntityException ex) {
            assertUnprocessableEntityExceptionInvalidFields(ex, invalidFields);
        }
    }


    @Test(dataProvider = "invalidProductRequestDataProvider")
    public void testUpdateProductUnprocessedEntity(ProductDto productRequest, Collection<String> invalidFields) {

        when(productRepository.findById(1L))
            .thenReturn(Optional.of(new Product()));

        try {
            productService.update(1L, productRequest);
            fail(FAIL_MESSAGE);
        } catch (UnprocessableEntityException ex) {
            assertUnprocessableEntityExceptionInvalidFields(ex, invalidFields);
        }
    }


    @Test(dataProvider = "validProductRequestDataProvider")
    public void testCreateExistentProduct(ProductDto productRequest) {

        when(productRepository.save(Mockito.any(Product.class)))
            .thenThrow(DataIntegrityViolationException.class);

        try {
            productService.create(productRequest);
            fail(FAIL_MESSAGE);
        } catch (RestException ex) {
            assertAlreadyExistingProductException(ex, productRequest.getName());
        }
    }

    @Test(dataProvider = "validProductRequestDataProvider")
    public void testUpdateExistentProduct(ProductDto productRequest) {

        when(productRepository.findById(1L))
            .thenReturn(Optional.of(new Product()));

        when(productRepository.save(Mockito.any(Product.class)))
            .thenThrow(DataIntegrityViolationException.class);

        try {
            productService.create(productRequest);
            fail(FAIL_MESSAGE);
        } catch (RestException ex) {
            assertAlreadyExistingProductException(ex, productRequest.getName());
        }
    }

    @Test
    public void testFindNonExistentProduct() {
        
        when(productRepository.findById(1L))
            .thenReturn(Optional.empty());

        try {
            productService.findById(1L);
            fail(FAIL_MESSAGE);
        } catch (RestException ex) {
            assertNotFoundProductException(ex);
        }
    }

    @Test
    public void testDeleteNonExistentProduct() {
        
        when(productRepository.findById(1L))
            .thenReturn(Optional.empty());

        try {
            productService.findById(1L);
            fail(FAIL_MESSAGE);
        } catch (RestException ex) {
            assertNotFoundProductException(ex);
        }
    }

    @DataProvider(name = "invalidProductRequestDataProvider")
    public Object[][] invalidProductRequestDataProvider() {

        var productDtoFactory = new ProductDtoFactory();

        var emptyName     = productDtoFactory.buildFakeInstance().toBuilder().name("").build();
        var nullName      = productDtoFactory.buildFakeInstance().toBuilder().name(null).build();
        var smallName     = productDtoFactory.buildFakeInstance().toBuilder().name("a").build();
        var longName      = productDtoFactory.buildFakeInstance().toBuilder().name("a".repeat(256)).build();
        var invalidamount = productDtoFactory.buildFakeInstance().toBuilder().amount(-1).build();
        var invalidPrice  = productDtoFactory.buildFakeInstance().toBuilder().price(0).build();
        var allInvalid    = productDtoFactory.buildFakeInstance().toBuilder().name("").amount(-1).price(0).build();

        //@formatter:off
        return new Object[][] {
            {nullName,       Arrays.asList("name")},
            {emptyName,      Arrays.asList("name")},
            {longName,       Arrays.asList("name")},
            {smallName,      Arrays.asList("name")},
            {invalidamount,  Arrays.asList("amount")},
            {invalidPrice,   Arrays.asList("price")},
            {allInvalid,     Arrays.asList("name", "amount", "price")}
        };
        //@formatter:on
    }

    @DataProvider(name = "validProductRequestDataProvider")
    public Object[][] validProductRequestDataProvider() { 
        //@formatter:off
        return new Object[][] {{ new ProductDtoFactory().buildFakeInstance() }};
        //@formatter:on
    }

    private void assertNotFoundProductException(RestException ex) {
        
        var expectedStatus   = HttpStatus.NOT_FOUND;
        var expectedMessage  = ProductService.PRODUCT_NOT_FOUND;
        
        assertRestExceptionStatusCode(ex, expectedStatus);
        assertRestExceptionMessage(ex, expectedMessage);
    }

    private void assertAlreadyExistingProductException(RestException ex, String productName){

        var expectedStatus   = HttpStatus.BAD_REQUEST;
        var expectedMessage  = String.format(ProductService.PRODUCT_NAME_ALREADY_EXISTING, productName);

        assertRestExceptionStatusCode(ex, expectedStatus);
        assertRestExceptionMessage(ex, expectedMessage);
    }

    private void assertUnprocessableNullEntityException(RestException ex) {

        var expectedStatus   = HttpStatus.UNPROCESSABLE_ENTITY;
        var expectedMessage  = ProductService.PRODUCT_OBJECT_NULL_ERROR_MESSAGE;

        assertRestExceptionStatusCode(ex, expectedStatus);
        assertRestExceptionMessage(ex, expectedMessage);
    }
}
