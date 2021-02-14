package com.linecode.payment.factory;

import java.util.LinkedList;
import java.util.List;

import com.github.javafaker.Faker;

public interface FakeFactory <T> {

    static final Faker FAKER = new Faker();

    T buildFakeInstance();

    default List<T> buildFakeInstance(int count) {
        var list = new LinkedList<T>();
        for (int i = 0; i < count; i++) 
            list.add(buildFakeInstance());
        return list;
    }
}
