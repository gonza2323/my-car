import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'modelos';
const BASE_ENDPOINT = 'modelos';

export const useGetModelo = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetModelos = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useGetModelosDisponibles = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT + '/available',
  queryKey: QUERY_KEY + '/available',
});

export const useCreateModelo = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
  axiosOptions: {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }
});

export const useDeleteModelo = createDeleteMutationHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetModeloDisponible = createGetQueryHook({
  endpoint: `modelos/:id/available`,
  queryKey: QUERY_KEY,
});