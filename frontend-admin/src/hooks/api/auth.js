import { z } from 'zod';
import { notifications } from '@mantine/notifications';
import { client, clearAccessToken, setAccessToken } from '@/api/axios';
import { LoginRequestSchema } from '@/api/dtos';
import { createGetQueryHook, createPostMutationHook } from '@/api/helpers';
import { useAuth } from '../use-auth';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const QUERY_KEY = 'auth-status';

export const useGetAuthStatus = createGetQueryHook({
  endpoint: '/auth/me',
  queryKey: QUERY_KEY
})

export const useLogin = () => {
  const { setAuth } = useAuth();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ variables }) => {
      const res = await client.post('/auth/login', variables);
      return res.data;
    },
    onSuccess: (data) => {
      setAccessToken(data.token.value, new Date(data.token.expiryDate));
      setAuth({
        isAuthenticated: true,
        userId: data.user.userId,
        roles: data.user.roles || []
      });
      queryClient.invalidateQueries(['auth-status'], { refetchActive: true });
      notifications.show({ title: 'Welcome back!', message: 'You have successfully logged in' });
    },
    onError: (err) => {
      notifications.show({ message: err.message, color: 'red' });
    },
  });
};

export const useLogout = () => {
  const { setAuth } = useAuth();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ variables }) => {
      const res = await client.post('/auth/logout', variables);
      return res.data;
    },
    onSuccess: () => {
      clearAccessToken();
      setAuth({
        isAuthenticated: false,
        userId: undefined,
        roles: [],
      });
      queryClient.invalidateQueries(['auth-status'], { refetchActive: true });
      notifications.show({ title: 'Goodbye!', message: 'You have successfully logged out' });
    },
    onError: (err) => {
      notifications.show({ message: err.message, color: 'red' });
    },
  });
};