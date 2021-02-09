package com.linecode.product.controller;

import com.linecode.product.dto.ProductDto;
import com.linecode.product.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PutMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productRequest) {
        var productResponse = productService.create(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @PostMapping("{id}")
    public ResponseEntity<ProductDto> update(@PathVariable long id, @RequestBody ProductDto productRequest) {
        var productResponse = productService.update(id, productRequest);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProductDto> delete(@PathVariable long id) {
        var productResponse = productService.delete(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        var products = productService.findAll(page, limit, direction);
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(product);
    }
}
