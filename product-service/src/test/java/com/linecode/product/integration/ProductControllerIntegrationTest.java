package com.linecode.product.integration;

import com.linecode.product.amqp.ProductProducer;
import com.linecode.product.dto.ProductDto;
import com.linecode.product.entity.Product;
import com.linecode.product.factory.ProductDtoFactory;
import com.linecode.product.factory.ProductFactory;
import com.linecode.product.repository.ProductRepository;
import com.linecode.product.service.ProductService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import static com.linecode.product.util.MapperUtil.convertTo;

public class ProductControllerIntegrationTest extends IntegrationTest {
 
    private static final String PRODUCT_CONTROLLER_URL = "/product";
    private static final ProductFactory PRODUCT_FACTORY = new ProductFactory();
    private static final ProductDtoFactory PRODUCT_DTO_FACTORY = new ProductDtoFactory();

    @Resource
    private ProductRepository productRepository;

    @MockBean(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    
    @Mock
    private Appender<ILoggingEvent> appender;

    @Captor
    private ArgumentCaptor<LoggingEvent> loggingEventCaptor;


    @Before
    public void clearProducts() {
        MockitoAnnotations.openMocks(this);
        productRepository.deleteAll();
        var root = (Logger) LoggerFactory.getLogger(ProductProducer.class);
        root.addAppender(appender);
        root.setLevel(Level.INFO);
    }

    @Test
    public void testCreateProductWithSuccess() {

        var productDto = PRODUCT_DTO_FACTORY.buildFakeInstance();
        var httpEntity = new HttpEntity<>(productDto);
        var response   = restTemplate.exchange(PRODUCT_CONTROLLER_URL, HttpMethod.PUT, httpEntity, ProductDto.class);

        verify(appender, times(1)).doAppend(loggingEventCaptor.capture());
        assertProductProducerLogger(ProductProducer.CREATE_LOG_MESSAGE);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        productDto.setId(response.getBody().getId());
        assertEquals(productDto, response.getBody());
    }    

    @Test
    public void testCreateAlreadyExistingProduct() {

        var productDto = PRODUCT_DTO_FACTORY.buildFakeInstance();
        productRepository.save(convertTo(productDto, Product.class));

        var httpEntity = new HttpEntity<>(productDto);
        var response   = restTemplate.exchange(PRODUCT_CONTROLLER_URL, HttpMethod.PUT, httpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertProductNameAlreadyExisting(productDto.getName(), response.getBody());
    }


    @Test
    public void testUpdateProductWithSuccess() {
        
        var product    = PRODUCT_FACTORY.buildFakeInstance();
        var productId  = productRepository.save(product).getId();
        var updateUrl  = buildUpdateProductUrl(productId);

        var productDto = PRODUCT_DTO_FACTORY.buildFakeInstance();
        var httpEntity = new HttpEntity<>(productDto);
        var response   = restTemplate.exchange(updateUrl, HttpMethod.POST, httpEntity, ProductDto.class);
        
        verify(appender, times(1)).doAppend(loggingEventCaptor.capture());
        assertProductProducerLogger(ProductProducer.UPDATE_LOG_MESSAGE);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        product = productRepository.findById(productId).get();
        productDto.setId(productId);

        assertEquals(productDto, response.getBody());
    
    }

    @Test
    public void testUpdateProductWithAlreadyExistingProductName() {
        
        var existingProductName = productRepository.save(PRODUCT_FACTORY.buildFakeInstance()).getName();
        var updateProductId     = productRepository.save(PRODUCT_FACTORY.buildFakeInstance()).getId();
        var updateUrl           = buildUpdateProductUrl(updateProductId);

        var productDto = PRODUCT_DTO_FACTORY.buildFakeInstance();
        productDto.setName(existingProductName);

        var httpEntity = new HttpEntity<>(productDto);
        var response   = restTemplate.exchange(updateUrl, HttpMethod.POST, httpEntity, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertProductNameAlreadyExisting(existingProductName, response.getBody());
    }

    @Test
    public void testDeleteWithSuccess() {

        var product    = productRepository.save(PRODUCT_FACTORY.buildFakeInstance());
        var updateUrl  = buildUpdateProductUrl(product.getId());
        var response   = restTemplate.exchange(updateUrl, HttpMethod.DELETE, null, ProductDto.class);

        verify(appender, times(1)).doAppend(loggingEventCaptor.capture());
        assertProductProducerLogger(ProductProducer.DELETE_LOG_MESSAGE);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(productRepository.findById(product.getId()).isEmpty());
        
    }

    @Test
    public void testDeleteNonExistingProduct() {

        var updateUrl  = buildUpdateProductUrl(10000L);
        var response   = restTemplate.exchange(updateUrl, HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ProductService.PRODUCT_NOT_FOUND, response.getBody());

    }

    @Test
    public void testFindByIdWithSuccess() {

        var product    = productRepository.save(PRODUCT_FACTORY.buildFakeInstance());
        var updateUrl  = buildUpdateProductUrl(product.getId());
        var response   = restTemplate.exchange(updateUrl, HttpMethod.GET, null, ProductDto.class);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());

        var expectedResponse = product.convertToProductDTO();
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testFindByIdNonExistingProduct() {

        var updateUrl  = buildUpdateProductUrl(10000L);
        var response   = restTemplate.exchange(updateUrl, HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ProductService.PRODUCT_NOT_FOUND, response.getBody());

    }

    @Test
    public void testFindAll() {

        PRODUCT_FACTORY.buildFakeInstance(12).forEach(productRepository::save);
        
        var firstPageResponse = restTemplate.exchange(PRODUCT_CONTROLLER_URL, HttpMethod.GET, null, Map.class);
        assertEquals(HttpStatus.FOUND, firstPageResponse.getStatusCode());

        var firstPage = firstPageResponse.getBody();
        assertEquals(2, firstPage.get("totalPages"));
        assertEquals(12, firstPage.get("totalElements"));
        assertEquals(10, firstPage.get("size"));
        assertEquals(10, firstPage.get("numberOfElements"));
        assertEquals(true, firstPage.get("first"));
        assertEquals(false, firstPage.get("last"));

        var lastPageResponse = restTemplate.exchange(PRODUCT_CONTROLLER_URL+"?page=1", HttpMethod.GET, null, Map.class);
        assertEquals(HttpStatus.FOUND, lastPageResponse.getStatusCode());

        var lastPage = lastPageResponse.getBody();
        assertEquals(2, lastPage.get("totalPages"));
        assertEquals(12, lastPage.get("totalElements"));
        assertEquals(10, firstPage.get("size"));
        assertEquals(2, lastPage.get("numberOfElements"));
        assertEquals(false, lastPage.get("first"));
        assertEquals(true, lastPage.get("last"));

        var noElementsFound = restTemplate.exchange(PRODUCT_CONTROLLER_URL+"?page=10", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.NOT_FOUND, noElementsFound.getStatusCode());
        assertEquals(ProductService.ELEMENTS_NOT_FOUND_ERROR_MESSAGE, noElementsFound.getBody());
    }

    private void assertProductProducerLogger(String message) {
        LoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(0);
        assertEquals(message, loggingEvent.getMessage());
        assertEquals(Level.INFO, loggingEvent.getLevel());
    }

    private String buildUpdateProductUrl(long productId) {
        return new StringBuilder()
        .append(PRODUCT_CONTROLLER_URL)
        .append("/")
        .append(String.valueOf(productId))
        .toString();
    }

    private void assertProductNameAlreadyExisting(String productName, String message) {
        var expectedMessage = String.format(ProductService.PRODUCT_NAME_ALREADY_EXISTING, productName);
        assertEquals(expectedMessage, message);
    }
}

