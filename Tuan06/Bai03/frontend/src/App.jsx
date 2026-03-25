import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RestaurantList from './components/RestaurantList';
import Menu from './components/Menu';
import OrderForm from './components/OrderForm';
import './App.css';

function App() {
  const [currentUser, setCurrentUser] = useState({ id: 1, email: 'customer@example.com' });
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);

  return (
    <Router>
      <div className="App">
        <header className="app-header">
          <h1>🍔 Food Delivery Service</h1>
          <p>Current User: {currentUser.email}</p>
        </header>

        <main className="app-main">
          <Routes>
            <Route path="/" element={<RestaurantList />} />
          </Routes>

          {selectedRestaurant && (
            <div className="selected-restaurant">
              <Menu restaurantId={selectedRestaurant.id} />
              <OrderForm customerId={currentUser.id} restaurantId={selectedRestaurant.id} />
            </div>
          )}
        </main>

        <footer className="app-footer">
          <p>&copy; 2026 Food Delivery System. All rights reserved.</p>
        </footer>
      </div>
    </Router>
  );
}

export default App;
