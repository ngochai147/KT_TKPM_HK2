import React, { useState, useEffect } from 'react';
import { userAPI } from '../../services/api';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState('ACTIVE');
  const [loading, setLoading] = useState(false);
  const [searchEmail, setSearchEmail] = useState('');
  const [searchResult, setSearchResult] = useState(null);

  useEffect(() => {
    loadUsers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedStatus]);

  const loadUsers = async () => {
    setLoading(true);
    try {
      const response = await userAPI.getUsersByStatus(selectedStatus);
      setUsers(response.data.data || []);
    } catch (error) {
      console.error('Error loading users:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchEmail = async () => {
    if (!searchEmail) return;

    try {
      const response = await userAPI.getUserByEmail(searchEmail);
      setSearchResult(response.data);
    } catch (error) {
      alert('User not found');
      setSearchResult(null);
    }
  };

  const handleUpdateStatus = async (userId, newStatus) => {
    try {
      await userAPI.updateStatus(userId, newStatus);
      alert('Status updated successfully');
      loadUsers();
    } catch (error) {
      alert('Failed to update status');
    }
  };

  const handleDeleteUser = async (userId) => {
    if (!window.confirm('Are you sure you want to delete this user?')) return;

    try {
      await userAPI.deleteUser(userId);
      alert('User deleted successfully');
      loadUsers();
    } catch (error) {
      alert('Failed to delete user');
    }
  };

  return (
    <div className="space-y-6">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-gray-900 mb-6">User Management</h2>

        {/* Search Section */}
        <div className="mb-6">
          <h3 className="text-lg font-semibold mb-3">Search User by Email</h3>
          <div className="flex gap-2">
            <input
              type="email"
              value={searchEmail}
              onChange={(e) => setSearchEmail(e.target.value)}
              placeholder="Enter email"
              className="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:ring-primary-500 focus:border-primary-500"
            />
            <button
              onClick={handleSearchEmail}
              className="px-6 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
            >
              Search
            </button>
          </div>

          {searchResult && (
            <div className="mt-4 p-4 bg-green-50 border border-green-200 rounded-md">
              <p><strong>User ID:</strong> {searchResult.userId}</p>
              <p><strong>Email:</strong> {searchResult.email}</p>
              <p><strong>Status:</strong> {searchResult.status}</p>
            </div>
          )}
        </div>

        {/* Filter Section */}
        <div className="mb-6">
          <h3 className="text-lg font-semibold mb-3">Filter by Status</h3>
          <div className="flex gap-2">
            {['ACTIVE', 'INACTIVE', 'SUSPENDED'].map((status) => (
              <button
                key={status}
                onClick={() => setSelectedStatus(status)}
                className={`px-4 py-2 rounded-md ${
                  selectedStatus === status
                    ? 'bg-primary-600 text-white'
                    : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                }`}
              >
                {status}
              </button>
            ))}
          </div>
        </div>

        {/* Users List */}
        {loading ? (
          <div className="text-center py-8">Loading...</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    User ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Created At
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {users.map((user) => (
                  <tr key={user.userId}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {user.userId}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {user.email}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        user.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                        user.status === 'INACTIVE' ? 'bg-gray-100 text-gray-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {user.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {new Date(user.createdAt).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                      <select
                        onChange={(e) => handleUpdateStatus(user.userId, e.target.value)}
                        className="text-primary-600 hover:text-primary-900 border border-gray-300 rounded px-2 py-1"
                      >
                        <option value="">Change Status</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="INACTIVE">INACTIVE</option>
                        <option value="SUSPENDED">SUSPENDED</option>
                      </select>
                      <button
                        onClick={() => handleDeleteUser(user.userId)}
                        className="text-red-600 hover:text-red-900"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {users.length === 0 && (
              <div className="text-center py-8 text-gray-500">
                No users found with status: {selectedStatus}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default UserManagement;
