package com.linecode.payment.util;

import org.modelmapper.ModelMapper;

public class MapperUtil {
    
    private MapperUtil() {
        // only static method
    }

    public static <T> T convertTo(Object object, Class<T> type) {
        return new ModelMapper().map(object, type);
    }
}
