import { z } from 'zod';
import { notifications } from '@mantine/notifications';
import { client, removeClientAccessToken, setClientAccessToken } from '@/api/axios';
import { LoginRequestSchema } from '@/api/dtos';
import { createGetQueryHook, createPostMutationHook } from '@/api/helpers';
import { useAuth } from '../use-auth';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const QUERY_KEY = 'auth-status';

export const useGetAuthStatus = createGetQueryHook({
  endpoint: '/auth/status',
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
      setClientAccessToken(data.token.value);
      setAuth(data.status);
      queryClient.invalidateQueries(['auth-status'], { refetchActive: true });
      notifications.show({ title: 'Welcome back!', message: 'You have successfully logged in' });
    },
    onError: (err) => {
      notifications.show({ message: err.message, color: 'red' });
    },
  });
};

export const useLogout = createPostMutationHook({
  endpoint: '/auth/logout',
  queryKey: QUERY_KEY,
  onSuccess: () => {
    removeClientAccessToken();
    notifications.show({ title: 'Goodbye!', message: 'You have successfully logged out' });
  },
  onError: (error) => {
    notifications.show({ message: error.message, color: 'red' });
  },
});
