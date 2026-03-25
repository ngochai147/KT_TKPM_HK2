import React, { useEffect, useState } from 'react';
import { restaurantService, dishService } from '../services/apiService';
import './RestaurantList.css';

function RestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchRestaurants();
  }, []);

  const fetchRestaurants = async () => {
    try {
      setLoading(true);
      const response = await restaurantService.getAllRestaurants();
      setRestaurants(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading restaurants...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="restaurant-list">
      <h2>Available Restaurants</h2>
      <div className="restaurants-grid">
        {restaurants.map((restaurant) => (
          <div key={restaurant.id} className="restaurant-card">
            <h3>{restaurant.name}</h3>
            <p>{restaurant.description}</p>
            <p><strong>Address:</strong> {restaurant.address}</p>
            <p><strong>Phone:</strong> {restaurant.phone}</p>
            <p><strong>Rating:</strong> {restaurant.rating || 'N/A'}</p>
            <button>View Menu</button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default RestaurantList;
