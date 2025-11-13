import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'modelos';
const BASE_ENDPOINT = 'modelos';

export const useGetModelosDisponibles = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT + '/available',
  queryKey: QUERY_KEY + '/available',
});
