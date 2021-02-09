package com.linecode.payment.controller;

import com.linecode.payment.dto.SaleDto;
import com.linecode.payment.service.SaleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sale")
public class SaleController {
    
    @Autowired
    private SaleService saleService;

    @PutMapping
    public ResponseEntity<SaleDto> create(@RequestBody SaleDto saleDto) {
        var saleResponse = saleService.create(saleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResponse);
    }

    @GetMapping
    public ResponseEntity<Page<SaleDto>> findAll(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        var sales = saleService.findAll(page, limit, direction);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("{id}")
    public ResponseEntity<SaleDto> findAll(@PathVariable long id) {
        var sale = saleService.findById(id);
        return ResponseEntity.ok(sale);
    }
}
