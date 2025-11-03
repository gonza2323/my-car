import { createGetQueryHook } from '@/api/helpers';

export const useGetNotifications = createGetQueryHook({
  endpoint: '/notifications',
  queryKey: 'notifications',
});
