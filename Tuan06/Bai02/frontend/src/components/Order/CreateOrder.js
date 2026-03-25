import React, { useState } from 'react';
import { orderAPI } from '../../services/api';
import { useNavigate } from 'react-router-dom';

const CreateOrder = () => {
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId');
  const [formData, setFormData] = useState({
    totalAmount: '',
    shippingAddress: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!userId) {
      alert('Please login first');
      navigate('/login');
      return;
    }

    if (parseFloat(formData.totalAmount) <= 0) {
      setError('Total amount must be greater than 0');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await orderAPI.createOrder(
        userId,
        parseFloat(formData.totalAmount),
        formData.shippingAddress
      );

      if (response.data.success) {
        alert(`Order created successfully! Order code: ${response.data.orderCode}`);
        setFormData({
          totalAmount: '',
          shippingAddress: '',
        });
        navigate('/orders');
      }
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to create order');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-gray-900 mb-6">Create New Order</h2>

        <form onSubmit={handleSubmit} className="space-y-6">
          {error && (
            <div className="bg-red-50 border border-red-400 text-red-700 px-4 py-3 rounded relative">
              {error}
            </div>
          )}

          <div>
            <label htmlFor="totalAmount" className="block text-sm font-medium text-gray-700 mb-2">
              Total Amount ($)
            </label>
            <input
              type="number"
              id="totalAmount"
              name="totalAmount"
              step="0.01"
              min="0.01"
              required
              value={formData.totalAmount}
              onChange={handleChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-primary-500 focus:border-primary-500"
              placeholder="Enter total amount"
            />
          </div>

          <div>
            <label htmlFor="shippingAddress" className="block text-sm font-medium text-gray-700 mb-2">
              Shipping Address
            </label>
            <textarea
              id="shippingAddress"
              name="shippingAddress"
              rows="4"
              required
              value={formData.shippingAddress}
              onChange={handleChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-primary-500 focus:border-primary-500"
              placeholder="Enter complete shipping address"
            />
          </div>

          <div className="flex gap-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Creating...' : 'Create Order'}
            </button>
            <button
              type="button"
              onClick={() => navigate('/orders')}
              className="flex-1 py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateOrder;
