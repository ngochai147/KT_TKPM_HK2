import React, { useEffect, useState } from 'react';
import { dishService } from '../services/apiService';
import './Menu.css';

function Menu({ restaurantId }) {
  const [dishes, setDishes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDishes();
  }, [restaurantId]);

  const fetchDishes = async () => {
    try {
      setLoading(true);
      const response = await dishService.getAvailableDishesByRestaurant(restaurantId);
      setDishes(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = (dish) => {
    console.log('Added to cart:', dish);
    // Implement cart logic
  };

  if (loading) return <div className="loading">Loading menu...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="menu">
      <h2>Menu</h2>
      <div className="dishes-grid">
        {dishes.map((dish) => (
          <div key={dish.id} className="dish-card">
            {dish.image && <img src={dish.image} alt={dish.name} />}
            <h3>{dish.name}</h3>
            <p>{dish.description}</p>
            <p className="category">{dish.category}</p>
            <p className="price">₹{dish.price.toFixed(2)}</p>
            <button onClick={() => handleAddToCart(dish)}>Add to Cart</button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Menu;
