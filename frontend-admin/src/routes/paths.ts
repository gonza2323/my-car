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
      alquileres: {
        root: '/dashboard/management/alquileres',
        list: '/dashboard/management/alquileres/list',
        view: (alquilerId: number) => `/dashboard/management/alquileres/${alquilerId}`,
        edit: (alquilerId: number) => `/dashboard/management/alquileres/${alquilerId}/edit`,
        add: '/dashboard/management/alquileres/add',
      },
      clientes: {
        root: '/dashboard/management/clientes',
        list: '/dashboard/management/clientes/list',
        view: (clienteId: number) => `/dashboard/management/clientes/${clienteId}`,
        edit: (clienteId: number) => `/dashboard/management/clientes/${clienteId}/edit`,
        add: '/dashboard/management/clientes/add',
      },
      reportes: { // no sé todavía bien las páginas
        root: '/dashboard/management/reportes',
        list: '/dashboard/management/reportes/list',
      },
      modelos: {
        root: '/dashboard/management/modelos',
        list: '/dashboard/management/modelos/list',
        view: (modeloId: number) => `/dashboard/management/modelos/${modeloId}`,
        edit: (modeloId: number) => `/dashboard/management/modelos/${modeloId}/edit`,
        add: '/dashboard/management/modelos/add',
      },
      vehiculos: {
        root: '/dashboard/management/vehiculos',
        list: '/dashboard/management/vehiculos/list',
        view: (vehiculoId: number) => `/dashboard/management/vehiculos/${vehiculoId}`,
        edit: (vehiculoId: number) => `/dashboard/management/vehiculos/${vehiculoId}/edit`,
        add: '/dashboard/management/vehiculos/add',
      },
      empleados: {
        root: '/dashboard/management/empleados',
        list: '/dashboard/management/empleados/list',
        view: (empleadoId: number) => `/dashboard/management/empleados/${empleadoId}`,
        edit: (empleadoId: number) => `/dashboard/management/empleados/${empleadoId}/edit`,
        add: '/dashboard/management/empleados/add',
      },
      empresa: {
        root: '/dashboard/management/empresa',
        edit: '/dashboard/management/empresa/edit',
      },
      configMail: {
        root: '/dashboard/management/config-mail',
        edit: '/dashboard/management/config-mail/edit',
      },
      localidades: {
        root: '/dashboard/management/localidades',
        list: '/dashboard/management/localidades/list',
        view: (localidadId: number) => `/dashboard/management/localidades/${localidadId}`,
        edit: (localidadId: number) => `/dashboard/management/localidades/${localidadId}/edit`,
        add: '/dashboard/management/localidades/add',
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
