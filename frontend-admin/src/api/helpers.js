import { useState } from 'react';
import {
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
import { isAxiosError } from 'axios';
import { client } from './axios';

/**
 * Create a URL with query parameters and route parameters
 */
export function createUrl(base, queryParams, routeParams) {
  let url = base;

  // Replace route params like :id
  if (routeParams) {
    Object.entries(routeParams).forEach(([key, value]) => {
      url = url.replaceAll(`:${key}`, String(value));
    });
  }

  // Add query params
  if (queryParams) {
    const query = new URLSearchParams();
    Object.entries(queryParams).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        query.append(key, String(value));
      }
    });
    const queryString = query.toString();
    if (queryString) {
      url = `${url}?${queryString}`;
    }
  }

  return url;
}

function getQueryKey(mainKey, route = {}, query = {}) {
  return [mainKey, { ...route, ...query }];
}

/** Handle request errors */
function handleRequestError(error) {
  if (isAxiosError(error)) {
    throw error.response?.data || error;
  }
  throw error;
}

/* ----------------------------------- GET ---------------------------------- */

/**
 * Create a custom hook for GET requests
 * 
 * @example
 * const useGetUser = createGetQueryHook({
 *   endpoint: '/api/users/:id',
 *   queryKey: 'user',
 * });
 * 
 * const { data } = useGetUser({ route: { id: 1 } });
 */
export function createGetQueryHook({
  endpoint,
  queryKey,
  queryOptions = {},
  axiosOptions = {}
}) {
  return (params) => {
    const url = createUrl(endpoint, params?.query, params?.route);

    return useQuery({
      queryKey: getQueryKey(queryKey, params?.route, params?.query),
      queryFn: async () => {
        const response = await client.get(url, axiosOptions);
        return response.data;
      },
      ...queryOptions,
    });
  };
}

/* ---------------------------------- POST ---------------------------------- */

/**
 * Create a custom hook for POST requests
 * 
 * @example
 * const useCreateUser = createPostMutationHook({
 *   endpoint: '/api/users',
 *   onSuccess: (data, variables, queryClient) => {
 *     queryClient.invalidateQueries({ queryKey: ['users'] });
 *   },
 * });
 */
export function createPostMutationHook({
  endpoint,
  queryKey,
  onSuccess,
  onError,
  mutationOptions = {},
  axiosOptions = {},
}) {
  return (params) => {
    const queryClient = useQueryClient();
    const baseUrl = createUrl(endpoint, params?.query, params?.route);

    return useMutation({
      mutationFn: async (data) => {
        const url = createUrl(baseUrl, data.query, data.route);
        const body = data.variables || data;

        const response = await client.post(url, body, axiosOptions);
        return response.data;
      },
      onSuccess: (data, variables, context) => {
        if (queryKey)
          queryClient.invalidateQueries({ queryKey: [queryKey], refetchActive: true });
        onSuccess?.(data, variables, queryClient, context);
      },
      onError: (error, variables, context) => {
        onError?.(error, variables, queryClient, context);
      },
      ...mutationOptions,
    });
  };
}

/* ----------------------------------- PUT ---------------------------------- */

/**
 * Create a custom hook for PUT requests
 */
export function createPutMutationHook({
  endpoint,
  queryKey,
  onSuccess,
  onError,
  mutationOptions = {},
  axiosOptions = {},
}) {
  return (params) => {
    const queryClient = useQueryClient();
    const baseUrl = createUrl(endpoint, params?.query, params?.route);

    return useMutation({
      mutationFn: async (data) => {
        const url = createUrl(baseUrl, data.query, data.route);
        const body = data.variables || data;

        const response = await client.put(url, body, axiosOptions);
        return response.data;
      },
      onSuccess: (data, variables, context) => {
        if (queryKey)
          queryClient.invalidateQueries({ queryKey: [queryKey] });
        onSuccess?.(data, variables, queryClient, context);
      },
      onError: (error, variables, context) => {
        onError?.(error, variables, queryClient, context);
      },
      ...mutationOptions,
    });
  };
}

/* --------------------------------- DELETE --------------------------------- */

/**
 * Create a custom hook for DELETE requests
 */
export function createDeleteMutationHook({
  endpoint,
  queryKey,
  onSuccess,
  onError,
  mutationOptions = {},
  axiosOptions = {},
}) {
  return (params) => {
    const queryClient = useQueryClient();
    const baseUrl = createUrl(endpoint, params?.query, params?.route);

    return useMutation({
      mutationFn: async (data = {}) => {
        const url = createUrl(baseUrl, data.query, data.route);
        await client.delete(url, axiosOptions);
      },
      onSuccess: (data, variables, context) => {
        if (queryKey)
          queryClient.invalidateQueries({ queryKey: [queryKey] });
        onSuccess?.(data, variables, queryClient, context);
      },
      onError: (error, variables, context) => {
        onError?.(error, variables, queryClient, context);
      },
      ...mutationOptions,
    });
  };
}

/* ------------------------------- PAGINATION ------------------------------- */

export function usePagination({ page = 0, size = 25 } = {}) {
  const [currentPage, setPage] = useState(page);
  const [currentSize, setSize] = useState(size);

  const onChangeSize = (value) => {
    setSize(value);
    setPage(0); // Reset to first page when size changes
  };

  return {
    page: currentPage,
    size: currentSize,
    setPage,
    setSize: onChangeSize
  };
}

/**
 * Create a custom hook for paginated GET requests (Spring Boot style)
 * 
 * @example
 * const useGetUsers = createPaginationQueryHook({
 *   endpoint: '/api/users',
 *   queryKey: 'users',
 * });
 * 
 * const { data } = useGetUsers({ query: { page: 0, size: 25 } });
 * // data = { data: [...], meta: { totalElements, totalPages, size, number } }
 */
export function createPaginationQueryHook({
  endpoint,
  queryKey,
  queryOptions = {},
  axiosOptions = {}
}) {
  return (params) => {
    const query = { page: 0, size: 25, ...params?.query };
    const route = params?.route || {};
    const url = createUrl(endpoint, query, route);

    return useQuery({
      queryKey: getQueryKey(queryKey, route, query),
      queryFn: async () => {
        const response = await client.get(url, axiosOptions);
        return response.data;
      },
      select: (data) => ({
        data: data.content,
        meta: {
          totalElements: data.totalElements,
          totalPages: data.totalPages,
          size: data.size,
          number: data.number,
        },
      }),
      ...queryOptions,
    });
  };
}