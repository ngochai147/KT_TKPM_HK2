import axios from 'axios';

const API_BASE_URL = 'http://localhost';

const userService = {
  getUser: (id) => axios.get(`${API_BASE_URL}:8081/api/users/${id}`),
  getUserByEmail: (email) => axios.get(`${API_BASE_URL}:8081/api/users/email/${email}`),
  getAllUsers: () => axios.get(`${API_BASE_URL}:8081/api/users`),
  createUser: (userData) => axios.post(`${API_BASE_URL}:8081/api/users`, userData),
  updateUser: (id, userData) => axios.put(`${API_BASE_URL}:8081/api/users/${id}`, userData),
  deleteUser: (id) => axios.delete(`${API_BASE_URL}:8081/api/users/${id}`),
};

const restaurantService = {
  getRestaurant: (id) => axios.get(`${API_BASE_URL}:8082/api/restaurants/${id}`),
  getAllRestaurants: () => axios.get(`${API_BASE_URL}:8082/api/restaurants`),
  getRestaurantsByOwner: (ownerId) => axios.get(`${API_BASE_URL}:8082/api/restaurants/owner/${ownerId}`),
  createRestaurant: (restaurantData) => axios.post(`${API_BASE_URL}:8082/api/restaurants`, restaurantData),
  updateRestaurant: (id, restaurantData) => axios.put(`${API_BASE_URL}:8082/api/restaurants/${id}`, restaurantData),
  deleteRestaurant: (id) => axios.delete(`${API_BASE_URL}:8082/api/restaurants/${id}`),
};

const dishService = {
  getDish: (id) => axios.get(`${API_BASE_URL}:8082/api/dishes/${id}`),
  getDishesByRestaurant: (restaurantId) => axios.get(`${API_BASE_URL}:8082/api/dishes/restaurant/${restaurantId}`),
  getAvailableDishesByRestaurant: (restaurantId) => axios.get(`${API_BASE_URL}:8082/api/dishes/restaurant/${restaurantId}/available`),
  getDishesByCategory: (category) => axios.get(`${API_BASE_URL}:8082/api/dishes/category/${category}`),
  createDish: (dishData) => axios.post(`${API_BASE_URL}:8082/api/dishes`, dishData),
  updateDish: (id, dishData) => axios.put(`${API_BASE_URL}:8082/api/dishes/${id}`, dishData),
  deleteDish: (id) => axios.delete(`${API_BASE_URL}:8082/api/dishes/${id}`),
};

const orderService = {
  getOrder: (id) => axios.get(`${API_BASE_URL}:8083/api/orders/${id}`),
  getOrdersByCustomer: (customerId) => axios.get(`${API_BASE_URL}:8083/api/orders/customer/${customerId}`),
  getOrdersByRestaurant: (restaurantId) => axios.get(`${API_BASE_URL}:8083/api/orders/restaurant/${restaurantId}`),
  getOrdersByStatus: (status) => axios.get(`${API_BASE_URL}:8083/api/orders/status/${status}`),
  createOrder: (orderData) => axios.post(`${API_BASE_URL}:8083/api/orders`, orderData),
  updateOrder: (id, orderData) => axios.put(`${API_BASE_URL}:8083/api/orders/${id}`, orderData),
  deleteOrder: (id) => axios.delete(`${API_BASE_URL}:8083/api/orders/${id}`),
};

const orderItemService = {
  getOrderItem: (id) => axios.get(`${API_BASE_URL}:8083/api/order-items/${id}`),
  getOrderItems: (orderId) => axios.get(`${API_BASE_URL}:8083/api/order-items/order/${orderId}`),
  createOrderItem: (itemData) => axios.post(`${API_BASE_URL}:8083/api/order-items`, itemData),
  updateOrderItem: (id, itemData) => axios.put(`${API_BASE_URL}:8083/api/order-items/${id}`, itemData),
  deleteOrderItem: (id) => axios.delete(`${API_BASE_URL}:8083/api/order-items/${id}`),
};

export {
  userService,
  restaurantService,
  dishService,
  orderService,
  orderItemService,
};
