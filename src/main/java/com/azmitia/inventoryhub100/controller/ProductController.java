package com.azmitia.inventoryhub100.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @GetMapping(value = "/test")
    public String greet(){
        return "Product 1";
    }
}
