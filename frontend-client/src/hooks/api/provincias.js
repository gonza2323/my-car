import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'provincias';
const BASE_ENDPOINT = 'provincias';

export const useGetProvincia = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetProvincias = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useCreateProvincia = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useDeleteProvincia = createDeleteMutationHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});
