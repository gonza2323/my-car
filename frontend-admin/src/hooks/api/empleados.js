import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'empleados';
const BASE_ENDPOINT = 'empleados';

export const useGetEmpleado = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetEmpleados = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useCreateEmpleado = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useDeleteEmpleado = createDeleteMutationHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});
