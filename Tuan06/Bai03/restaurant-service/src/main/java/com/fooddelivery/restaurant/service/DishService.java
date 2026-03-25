package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.DishDTO;
import com.fooddelivery.restaurant.entity.Dish;
import com.fooddelivery.restaurant.entity.Restaurant;
import com.fooddelivery.restaurant.repository.DishRepository;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public DishDTO getDishById(Long id) {
        Optional<Dish> dish = dishRepository.findById(id);
        return dish.map(this::convertToDTO).orElse(null);
    }

    public List<DishDTO> getDishesByRestaurant(Long restaurantId) {
        return dishRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DishDTO> getAvailableDishesByRestaurant(Long restaurantId) {
        return dishRepository.findByRestaurantIdAndAvailableTrue(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DishDTO> getDishesByCategory(String category) {
        return dishRepository.findByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DishDTO createDish(DishDTO dishDTO) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(dishDTO.getRestaurantId());
        if (restaurant.isPresent()) {
            Dish dish = convertToEntity(dishDTO);
            dish.setRestaurant(restaurant.get());
            Dish savedDish = dishRepository.save(dish);
            return convertToDTO(savedDish);
        }
        return null;
    }

    public DishDTO updateDish(Long id, DishDTO dishDTO) {
        Optional<Dish> existingDish = dishRepository.findById(id);
        if (existingDish.isPresent()) {
            Dish dish = existingDish.get();
            dish.setName(dishDTO.getName());
            dish.setDescription(dishDTO.getDescription());
            dish.setPrice(dishDTO.getPrice());
            dish.setCategory(dishDTO.getCategory());
            dish.setImage(dishDTO.getImage());
            dish.setAvailable(dishDTO.getAvailable());
            Dish updatedDish = dishRepository.save(dish);
            return convertToDTO(updatedDish);
        }
        return null;
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    private DishDTO convertToDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setCategory(dish.getCategory());
        dto.setImage(dish.getImage());
        dto.setRestaurantId(dish.getRestaurant().getId());
        dto.setAvailable(dish.getAvailable());
        return dto;
    }

    private Dish convertToEntity(DishDTO dto) {
        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setCategory(dto.getCategory());
        dish.setImage(dto.getImage());
        dish.setAvailable(true);
        return dish;
    }
}
