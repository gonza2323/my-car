import { createGetQueryHook } from '@/api/helpers';

export const useGetAccountInfo = createGetQueryHook({
  endpoint: '/me/account',
  queryKey: 'account',
});
