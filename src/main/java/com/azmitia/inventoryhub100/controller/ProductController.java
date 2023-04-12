package com.azmitia.inventoryhub100.controller;

import com.azmitia.inventoryhub100.dto.ProductDTO;
import com.azmitia.inventoryhub100.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping(value = "/products/create")
    public ResponseEntity create(@RequestBody ProductDTO product){
        return new ResponseEntity(service.createProduct(product), HttpStatus.OK);
    }
}
