import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'departamentos';
const BASE_ENDPOINT = 'departamentos';

export const useGetDepartamento = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetDepartamentos = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useCreateDepartamento = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useDeleteDepartamento = createDeleteMutationHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});
