package com.linecode.product.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.linecode.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
