package com.azmitia.inventoryhub100;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.azmitia.inventoryhub100","firebase"})
public class InventoryhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryhubApplication.class, args);
	}

}
