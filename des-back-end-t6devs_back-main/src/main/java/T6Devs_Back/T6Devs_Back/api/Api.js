import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // URL do seu backend
});

// Interceptor para adicionar o token JWT
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;