package com.fooddelivery.monolithic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for Monolithic Architecture
 *
 * This is Phase 1 of the Food Delivery System Evolution:
 * - All features (User, Restaurant, Order) in one application
 * - Single database: food_delivery_db
 * - Direct method calls between services (no HTTP)
 * - Runs on port 8080
 */
@SpringBootApplication
public class MonolithicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonolithicApplication.class, args);
        System.out.println("===========================================");
        System.out.println("Food Delivery Monolithic App is running...");
        System.out.println("Port: 8080");
        System.out.println("API Base: http://localhost:8080/api");
        System.out.println("===========================================");
    }
}
