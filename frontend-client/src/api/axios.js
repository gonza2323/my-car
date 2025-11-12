import { app } from '@/config';
import axios from 'axios';

// In-memory token storage
let accessToken = null;
let tokenExpiryDate = null;
let refreshTimeoutId = null;

// Track ongoing refresh to prevent race conditions
let refreshPromise = null;

export const setAccessToken = (token, expiryDate) => {
  accessToken = token;
  tokenExpiryDate = expiryDate;

  // Schedule auto-refresh
  if (token && expiryDate) {
    scheduleTokenRefresh(expiryDate);
  }
};

const getAccessToken = () => accessToken;

export const clearAccessToken = () => {
  accessToken = null;
  tokenExpiryDate = null;
  
  // Cancel any pending refresh
  if (refreshTimeoutId) {
    clearTimeout(refreshTimeoutId);
    refreshTimeoutId = null;
  }
};

// Schedule token refresh 5 minutes before expiry
const scheduleTokenRefresh = (expiryDate) => {
  // Clear existing timeout
  if (refreshTimeoutId) {
    clearTimeout(refreshTimeoutId);
  }

  const expiryTime = expiryDate.getTime();
  const now = Date.now();
  const timeUntilExpiry = expiryTime - now;
  
  // Refresh 5 minutes (300000ms) before expiry
  const refreshBuffer = 5 * 60 * 1000;
  const timeUntilRefresh = timeUntilExpiry - refreshBuffer;

  // If token expires in less than 5 minutes, refresh immediately
  if (timeUntilRefresh <= 0) {
    refreshAccessToken();
    return;
  }

  // Schedule refresh
  refreshTimeoutId = setTimeout(() => {
    refreshAccessToken();
  }, timeUntilRefresh);

  const refreshTime = new Date(expiryDate.getTime());
  refreshTime.setMinutes(refreshTime.getMinutes() - 5);
};

// Single axios client for all requests
export const client = axios.create({
  baseURL: app.apiBaseUrl || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

// Request interceptor - adds token if available
client.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - handles 401 and token refresh
client.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If 401 and we haven't retried yet and we have a token to refresh
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        // If refresh is already in progress, wait for it
        if (refreshPromise) {
          const newToken = await refreshPromise;
          originalRequest.headers.Authorization = `Bearer ${newToken}`;
          return client(originalRequest);
        }

        // Start new refresh
        refreshPromise = refreshAccessToken();
        
        const newToken = await refreshPromise;
        
        // Retry original request with new token
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return client(originalRequest);
      } catch (refreshError) {
        // Refresh failed - clear everything and redirect to login
        clearAccessToken();
        return Promise.reject(refreshError);
      } finally {
        refreshPromise = null;
      }
    }

    return Promise.reject(error);
  }
);

// Refresh token function
async function refreshAccessToken() {
  try {
    const response = await axios.post(
      `${client.defaults.baseURL}/auth/refresh`,
      {},
      { withCredentials: true }
    );
    
    const { value, expiryDate } = response.data;
    setAccessToken(value, new Date(expiryDate));
    
    return value;
  } catch (error) {
    throw error;
  }
}