import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'promociones';
const BASE_ENDPOINT = 'promociones';

export const useGetPromocion = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetPromociones = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useCreatePromocion = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useDeletePromocion = createDeleteMutationHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useVerificarCupon = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/check`,
  queryKey: QUERY_KEY,
});