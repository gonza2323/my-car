import docs from '@/pages/docs/paths';

export const paths = {
  docs,
  auth: {
    root: '/auth',
    login: '/auth/login',
    register: '/auth/register',
    forgotPassword: '/auth/forgot-password',
    resetPassword: '/auth/reset-password',
    otp: '/auth/otp',
    terms: '/auth/terms',
    privacy: '/auth/privacy',
  },

  dashboard: {
    root: '/dashboard',
    home: '/dashboard/home',
    management: {
      root: '/dashboard/management',
      localidades: {
        root: '/dashboard/management/localidades',
        list: '/dashboard/management/localidades/list',
        view: (localidadId: number) => `/dashboard/management/localidades/${localidadId}`,
        edit: (localidadId: number) => `/dashboard/management/localidades/${localidadId}/edit`,
        add: '/dashboard/management/localidades/add',
      },
      autores: {
        root: '/dashboard/management/autores',
        list: '/dashboard/management/autores/list',
        view: (autorId: number) => `/dashboard/management/autores/${autorId}`,
        edit: (autorId: number) => `/dashboard/management/autores/${autorId}/edit`,
        add: '/dashboard/management/autores/add',
      },
      personas: {
        root: '/dashboard/management/personas',
        list: '/dashboard/management/personas/list',
        view: (personaId: number) => `/dashboard/management/personas/${personaId}`,
        edit: (personaId: number) => `/dashboard/management/personas/${personaId}/edit`,
        add: '/dashboard/management/personas/add',
      },
      libros: {
        root: '/dashboard/management/libros',
        list: '/dashboard/management/libros/list',
        view: (libroId: number) => `/dashboard/management/libros/${libroId}`,
        edit: (libroId: number) => `/dashboard/management/libros/${libroId}/edit`,
        add: '/dashboard/management/libros/add',
      },
      proveedores: {
        root: '/dashboard/management/proveedores',
        list: '/dashboard/management/proveedores/list',
        view: (proveedorId: number) => `/dashboard/management/proveedores/${proveedorId}`,
        edit: (proveedorId: number) => `/dashboard/management/proveedores/${proveedorId}/edit`,
        add: '/dashboard/management/proveedores/add',
      },
      articulos: {
        root: '/dashboard/management/articulos',
        list: '/dashboard/management/articulos/list',
        view: (articuloId: number) => `/dashboard/management/articulos/${articuloId}`,
        edit: (articuloId: number) => `/dashboard/management/articulos/${articuloId}/edit`,
        add: '/dashboard/management/articulos/add',
      },
      customers: {
        root: '/dashboard/management/customers',
        list: '/dashboard/management/customers/list',
        view: (customerId: string) => `/dashboard/management/customers/${customerId}`,
      },
    },
    apps: {
      root: '/dashboard/apps',
      kanban: '/dashboard/apps/kanban',
    },
    widgets: {
      root: '/dashboard/widgets',
      metrics: '/dashboard/widgets/metrics',
      charts: '/dashboard/widgets/charts',
      tables: '/dashboard/widgets/tables',
    },
  },
};
