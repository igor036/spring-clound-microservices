package com.linecode.product.factory;

import java.util.LinkedList;
import java.util.List;

public interface FakeFactory <T> {

    T buildFakeInstance();

    default List<T> buildFakeInstance(int count) {
        var list = new LinkedList<T>();
        for (int i = 0; i < count; i++) 
            list.add(buildFakeInstance());
        return list;
    }
}
