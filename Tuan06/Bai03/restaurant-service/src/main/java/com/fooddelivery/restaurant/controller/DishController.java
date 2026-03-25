package com.fooddelivery.restaurant.controller;

import com.fooddelivery.restaurant.dto.DishDTO;
import com.fooddelivery.restaurant.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@CrossOrigin(origins = "*")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDish(@PathVariable Long id) {
        DishDTO dish = dishService.getDishById(id);
        if (dish != null) {
            return ResponseEntity.ok(dish);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<DishDTO>> getDishesByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(dishService.getDishesByRestaurant(restaurantId));
    }

    @GetMapping("/restaurant/{restaurantId}/available")
    public ResponseEntity<List<DishDTO>> getAvailableDishesByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(dishService.getAvailableDishesByRestaurant(restaurantId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<DishDTO>> getDishesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(dishService.getDishesByCategory(category));
    }

    @PostMapping
    public ResponseEntity<DishDTO> createDish(@RequestBody DishDTO dishDTO) {
        DishDTO createdDish = dishService.createDish(dishDTO);
        if (createdDish != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishDTO> updateDish(@PathVariable Long id, @RequestBody DishDTO dishDTO) {
        DishDTO updatedDish = dishService.updateDish(id, dishDTO);
        if (updatedDish != null) {
            return ResponseEntity.ok(updatedDish);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
