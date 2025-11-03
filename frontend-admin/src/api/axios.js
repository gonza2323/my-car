import axios from 'axios';
import { app } from '@/config';

export const client = axios.create({
  baseURL: app.apiBaseUrl,
  headers: {
    'Content-type': 'application/json',
    Accept: 'application/json',
  },
});

export function setClientAccessToken(token) {
  if (!token) {
    removeClientAccessToken();
    return;
  }
  client.defaults.headers.common.authorization = `Bearer ${token}`;
}

export function removeClientAccessToken() {
  delete client.defaults.headers.common.authorization;
}

export function loadAccessToken() {
  if (token) {
    setClientAccessToken(token);
  } else {
    removeClientAccessToken();
  }
}
