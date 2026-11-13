import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:9000",
  headers: {
    "Content-Type": "application/json"
  }
});

// Add JWT token automatically for protected requests.
api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default api;