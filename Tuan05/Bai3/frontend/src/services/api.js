import axios from 'axios';

const API_LOGIN_URL = 'http://localhost:8081/auth';
const API_REGISTER_URL = 'http://localhost:8080/register';

export const loginUser = async (username, password) => {
  try {
    const response = await axios.post(`${API_LOGIN_URL}/login`, {
      username,
      password
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Login failed';
  }
};

export const registerUser = async (username, password) => {
  try {
    const response = await axios.post(API_REGISTER_URL, {
      username,
      password
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Registration failed';
  }
};
