package com.fooddelivery.restaurant.dto;

import java.util.List;

public class RestaurantDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private Double rating;
    private Long ownerId;
    private Boolean active;
    private List<DishDTO> dishes;

    // Constructors and Getters/Setters
    public RestaurantDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<DishDTO> getDishes() { return dishes; }
    public void setDishes(List<DishDTO> dishes) { this.dishes = dishes; }
}
