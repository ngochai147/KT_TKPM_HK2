import axiosInstance from '../utils/axios';

// ==================== USER APIs ====================
export const userAPI = {
  register: (email, password) =>
    axiosInstance.post('/users/register', { email, password }),

  login: (email, password) =>
    axiosInstance.post('/users/login', { email, password }),

  getUser: (userId) =>
    axiosInstance.get(`/users/${userId}`),

  getUserByEmail: (email) =>
    axiosInstance.get(`/users/email/${email}`),

  getUsersByStatus: (status) =>
    axiosInstance.get(`/users/status/${status}`),

  updateStatus: (userId, status) =>
    axiosInstance.put(`/users/${userId}/status`, { status }),

  changePassword: (userId, oldPassword, newPassword) =>
    axiosInstance.post(`/users/${userId}/change-password`, { oldPassword, newPassword }),

  deleteUser: (userId) =>
    axiosInstance.delete(`/users/${userId}`),
};

// ==================== ORDER APIs ====================
export const orderAPI = {
  createOrder: (userId, totalAmount, shippingAddress) =>
    axiosInstance.post('/orders', { userId, totalAmount, shippingAddress }),

  getUserOrders: (userId) =>
    axiosInstance.get(`/orders/user/${userId}`),

  getUserOrdersByStatus: (userId, status) =>
    axiosInstance.get(`/orders/user/${userId}/status/${status}`),

  getRecentOrders: (userId, limit = 10, status = 'PENDING') =>
    axiosInstance.get(`/orders/user/${userId}/recent`, { params: { limit, status } }),

  getOrderByCode: (orderCode) =>
    axiosInstance.get(`/orders/code/${orderCode}`),

  getOrder: (orderId) =>
    axiosInstance.get(`/orders/${orderId}`),

  getOrdersInRange: (userId, startDate, endDate) =>
    axiosInstance.get(`/orders/user/${userId}/range`, { params: { startDate, endDate } }),

  updateOrderStatus: (orderId, status) =>
    axiosInstance.put(`/orders/${orderId}/status`, { status }),

  cancelOrder: (orderId) =>
    axiosInstance.delete(`/orders/${orderId}/cancel`),

  getOrderStatistics: (userId) =>
    axiosInstance.get(`/orders/user/${userId}/statistics`),

  getTotalSpent: (userId) =>
    axiosInstance.get(`/orders/user/${userId}/spent`),
};

// ==================== USER PROFILE APIs ====================
export const profileAPI = {
  createProfile: (profile) =>
    axiosInstance.post('/profiles', profile),

  getProfile: (userId) =>
    axiosInstance.get(`/profiles/${userId}`),

  updateProfile: (userId, profile) =>
    axiosInstance.put(`/profiles/${userId}`, profile),

  getProfileByPhone: (phoneNumber) =>
    axiosInstance.get(`/profiles/phone/${phoneNumber}`),
};

// ==================== LOGIN HISTORY APIs ====================
export const loginHistoryAPI = {
  recordLogin: (userId, ipAddress, userAgent, device) =>
    axiosInstance.post('/login-history/record', { userId, ipAddress, userAgent, device }),

  recordLogout: (historyId) =>
    axiosInstance.post(`/login-history/${historyId}/logout`),

  getLoginHistory: (userId) =>
    axiosInstance.get(`/login-history/user/${userId}`),

  getRecentLogins: (userId, limit = 10) =>
    axiosInstance.get(`/login-history/user/${userId}/recent`, { params: { limit } }),

  getLoginCountToday: (userId) =>
    axiosInstance.get(`/login-history/user/${userId}/today`),

  getLoginHistoryRange: (userId, startDate, endDate) =>
    axiosInstance.get(`/login-history/user/${userId}/range`, { params: { startDate, endDate } }),

  checkSuspiciousActivity: (userId, days = 7) =>
    axiosInstance.get(`/login-history/user/${userId}/suspicious`, { params: { days } }),
};
