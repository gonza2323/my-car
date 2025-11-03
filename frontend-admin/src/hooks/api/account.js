import { createGetQueryHook } from '@/api/helpers';

export const useGetAccountInfo = createGetQueryHook({
  endpoint: '/account',
  queryKey: 'account',
});
