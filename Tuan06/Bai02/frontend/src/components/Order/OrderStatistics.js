import React, { useState, useEffect } from 'react';
import { orderAPI } from '../../services/api';

const OrderStatistics = () => {
  const userId = localStorage.getItem('userId');
  const [statistics, setStatistics] = useState(null);
  const [totalSpent, setTotalSpent] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadStatistics();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  const loadStatistics = async () => {
    if (!userId) return;

    setLoading(true);
    try {
      const [statsResponse, spentResponse] = await Promise.all([
        orderAPI.getOrderStatistics(userId),
        orderAPI.getTotalSpent(userId),
      ]);

      setStatistics(statsResponse.data.statistics);
      setTotalSpent(spentResponse.data.totalSpent);
    } catch (error) {
      console.error('Error loading statistics:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-xl text-gray-600">Loading statistics...</div>
      </div>
    );
  }

  if (!statistics) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-xl text-gray-600">No statistics available</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900">Order Statistics</h2>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-blue-500 rounded-md p-3">
              <svg className="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
              </svg>
            </div>
            <div className="ml-5">
              <dl>
                <dt className="text-sm font-medium text-gray-500 truncate">Total Orders</dt>
                <dd className="text-3xl font-semibold text-gray-900">{statistics.totalOrders || 0}</dd>
              </dl>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-yellow-500 rounded-md p-3">
              <svg className="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div className="ml-5">
              <dl>
                <dt className="text-sm font-medium text-gray-500 truncate">Pending</dt>
                <dd className="text-3xl font-semibold text-gray-900">{statistics.pendingOrders || 0}</dd>
              </dl>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-green-500 rounded-md p-3">
              <svg className="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div className="ml-5">
              <dl>
                <dt className="text-sm font-medium text-gray-500 truncate">Delivered</dt>
                <dd className="text-3xl font-semibold text-gray-900">{statistics.deliveredOrders || 0}</dd>
              </dl>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center">
            <div className="flex-shrink-0 bg-purple-500 rounded-md p-3">
              <svg className="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div className="ml-5">
              <dl>
                <dt className="text-sm font-medium text-gray-500 truncate">Total Spent</dt>
                <dd className="text-3xl font-semibold text-gray-900">${totalSpent}</dd>
              </dl>
            </div>
          </div>
        </div>
      </div>

      {/* Detailed Statistics */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Detailed Breakdown</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="border-l-4 border-blue-500 pl-4">
            <p className="text-sm text-gray-600">Processing Orders</p>
            <p className="text-2xl font-bold text-gray-900">{statistics.processingOrders || 0}</p>
          </div>
          <div className="border-l-4 border-purple-500 pl-4">
            <p className="text-sm text-gray-600">Shipped Orders</p>
            <p className="text-2xl font-bold text-gray-900">{statistics.shippedOrders || 0}</p>
          </div>
          <div className="border-l-4 border-red-500 pl-4">
            <p className="text-sm text-gray-600">Cancelled Orders</p>
            <p className="text-2xl font-bold text-gray-900">{statistics.cancelledOrders || 0}</p>
          </div>
          <div className="border-l-4 border-green-500 pl-4">
            <p className="text-sm text-gray-600">Average Order Value</p>
            <p className="text-2xl font-bold text-gray-900">
              ${statistics.totalOrders > 0 ? (totalSpent / statistics.totalOrders).toFixed(2) : '0.00'}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderStatistics;
