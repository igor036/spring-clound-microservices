package com.linecode.payment.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import com.linecode.payment.exception.RestException;
import com.linecode.payment.exception.UnprocessableEntityException;
import org.springframework.http.HttpStatus;

public abstract class UnitTest {

    protected static final String FAIL_MESSAGE = "Didn't throw expected exception";
    

    protected void assertRestExceptionStatusCode(RestException ex, HttpStatus status) {
        var exceptionCode = ex.getStatus().value();
        var expectedCode  = status.value();
        assertEquals(exceptionCode, expectedCode);
    }

    protected void assertRestExceptionMessage(RestException ex, String message) {
        assertEquals(ex.getMessage(), message);
    }

    protected void assertUnprocessableEntityExceptionInvalidFields(UnprocessableEntityException ex, Collection<String> invalidFields) {
        var errors = ex.getErrors();
        invalidFields.stream().forEach(field -> {
            assertTrue(errors.containsKey(field));
            assertTrue(!errors.get(field).isEmpty());
        });
    }
}
