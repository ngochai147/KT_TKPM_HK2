import React, { useState } from 'react';
import { orderService } from '../services/apiService';
import './OrderForm.css';

function OrderForm({ customerId, restaurantId }) {
  const [deliveryAddress, setDeliveryAddress] = useState('');
  const [notes, setNotes] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!deliveryAddress.trim()) {
      setMessage('Please enter delivery address');
      return;
    }

    try {
      setLoading(true);
      const orderData = {
        customerId,
        restaurantId,
        totalPrice: 0, // Should be calculated from cart
        deliveryAddress,
        notes,
        items: [], // Should be populated from cart
      };

      await orderService.createOrder(orderData);
      setMessage('Order placed successfully!');
      setDeliveryAddress('');
      setNotes('');
    } catch (err) {
      setMessage(`Error: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="order-form" onSubmit={handleSubmit}>
      <h3>Delivery Details</h3>

      <div className="form-group">
        <label htmlFor="address">Delivery Address:</label>
        <textarea
          id="address"
          value={deliveryAddress}
          onChange={(e) => setDeliveryAddress(e.target.value)}
          placeholder="Enter your delivery address"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="notes">Special Instructions:</label>
        <textarea
          id="notes"
          value={notes}
          onChange={(e) => setNotes(e.target.value)}
          placeholder="Any special requests?"
        />
      </div>

      <button type="submit" disabled={loading}>
        {loading ? 'Processing...' : 'Place Order'}
      </button>

      {message && <p className="message">{message}</p>}
    </form>
  );
}

export default OrderForm;
