package com.fooddelivery.restaurant.repository;

import com.fooddelivery.restaurant.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByRestaurantId(Long restaurantId);
    List<Dish> findByRestaurantIdAndAvailableTrue(Long restaurantId);
    List<Dish> findByCategory(String category);
}
