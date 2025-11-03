import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'localidades';
const BASE_ENDPOINT = 'localidades';

export const useGetLocalidad = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  queryKey: QUERY_KEY,
});

export const useGetLocalidades = createPaginationQueryHook({
  endpoint: BASE_ENDPOINT,
  queryKey: QUERY_KEY,
});

export const useCreateLocalidad = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
});

export const useDeleteLocalidad = createDeleteMutationHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  onSuccess: (data, variables, context, queryClient) => {
    queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
    notifications.show({
      title: 'Borrado',
      message: 'La localidad fue borrada con éxito',
      color: 'green',
    });
  },
  onError: (error) => {
    notifications.show({
      title: 'Error',
      message: error.message || 'No se pudo borrar la localidad',
      color: 'red',
    });
  },
});
