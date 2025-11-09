import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'paises';
const BASE_ENDPOINT = 'paises';

export const useGetPais = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetPaises = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useCreatePais = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useDeletePais = createDeleteMutationHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});
