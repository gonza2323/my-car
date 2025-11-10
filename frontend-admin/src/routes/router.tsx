import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom';
import { AuthGuard } from '@/guards/auth-guard';
import { GuestGuard } from '@/guards/guest-guard';
import { AuthLayout } from '@/layouts/auth';
import { DashboardLayout } from '@/layouts/dashboard';
import docsRoutes from '@/pages/docs/routes';
import { LazyPage } from './lazy-page';
import { paths } from './paths';

const router = createBrowserRouter([
  ...docsRoutes,
  {
    path: '/',
    element: <Navigate to={paths.dashboard.root} replace />,
  },
  {
    path: paths.auth.root,
    element: (
      <GuestGuard>
        <AuthLayout />
      </GuestGuard>
    ),
    children: [
      {
        index: true,
        path: paths.auth.root,
        element: <Navigate to={paths.auth.login} replace />,
      },
      {
        path: paths.auth.login,
        element: LazyPage(() => import('@/pages/auth/login')),
      },
      {
        path: paths.auth.register,
        element: LazyPage(() => import('@/pages/auth/register')),
      },
      {
        path: paths.auth.forgotPassword,
        element: LazyPage(() => import('@/pages/auth/forgot-password')),
      },
      // {
      //   path: routes.auth.resetPassword,
      //   element: LazyPage(() => import('@/pages/auth/reset-password')),
      // },
      {
        path: paths.auth.otp,
        element: LazyPage(() => import('@/pages/auth/otp')),
      },
      // {
      //   path: routes.auth.terms,
      //   element: LazyPage(() => import('@/pages/auth/terms')),
      // },
      // {
      //   path: routes.auth.privacy,
      //   element: LazyPage(() => import('@/pages/auth/privacy')),
      // },
    ],
  },
  {
    path: paths.dashboard.root,
    element: (
      <AuthGuard>
        <DashboardLayout />
      </AuthGuard>
    ),
    children: [
      {
        index: true,
        path: paths.dashboard.root,
        element: <Navigate to={paths.dashboard.home} replace />,
      },
      {
        path: paths.dashboard.home,
        element: LazyPage(() => import('@/pages/dashboard/home')),
      },
      /* ------------------------------- MANAGEMENT ------------------------------- */
      {
        path: paths.dashboard.management.root,
        children: [
          {
            index: true,
            path: paths.dashboard.management.root,
            element: <Navigate to={paths.dashboard.management.empleados.root} replace />,
          },
          {
            path: paths.dashboard.management.clientes.root,
            children: [
              {
                index: true,
                path: paths.dashboard.management.clientes.root,
                element: <Navigate to={paths.dashboard.management.clientes.list} replace />,
              },
              {
                path: paths.dashboard.management.clientes.list,
                element: LazyPage(() => import('@/pages/dashboard/management/clientes/list')),
              },
              {
                path: paths.dashboard.management.clientes.add,
                element: LazyPage(() => import('@/pages/dashboard/management/clientes/add')),
              },
              {
                path: paths.dashboard.management.clientes.root + '/:clienteId',
                element: LazyPage(() => import('@/pages/dashboard/management/clientes/detail')),
              },
            ],
          },
          {
            path: paths.dashboard.management.empleados.root,
            children: [
              {
                index: true,
                path: paths.dashboard.management.empleados.root,
                element: <Navigate to={paths.dashboard.management.empleados.list} replace />,
              },
              {
                path: paths.dashboard.management.empleados.list,
                element: LazyPage(() => import('@/pages/dashboard/management/empleados/list')),
              },
              {
                path: paths.dashboard.management.empleados.add,
                element: LazyPage(() => import('@/pages/dashboard/management/empleados/add')),
              },
              {
                path: paths.dashboard.management.empleados.root + '/:empleadoId',
                element: LazyPage(() => import('@/pages/dashboard/management/empleados/detail')),
              },
            ],
          },
          {
            path: paths.dashboard.management.modelos.root,
            children: [
              {
                index: true,
                path: paths.dashboard.management.modelos.root,
                element: <Navigate to={paths.dashboard.management.modelos.list} replace />,
              },
              {
                path: paths.dashboard.management.modelos.list,
                element: LazyPage(() => import('@/pages/dashboard/management/modelos/list')),
              },
              {
                path: paths.dashboard.management.modelos.add,
                element: LazyPage(() => import('@/pages/dashboard/management/modelos/add')),
              },
              {
                path: paths.dashboard.management.modelos.root + '/:modeloId',
                element: LazyPage(() => import('@/pages/dashboard/management/modelos/detail')),
              },
            ],
          },
                    {
            path: paths.dashboard.management.vehiculos.root,
            children: [
              {
                index: true,
                path: paths.dashboard.management.vehiculos.root,
                element: <Navigate to={paths.dashboard.management.vehiculos.list} replace />,
              },
              {
                path: paths.dashboard.management.vehiculos.list,
                element: LazyPage(() => import('@/pages/dashboard/management/vehiculos/list')),
              },
              {
                path: paths.dashboard.management.vehiculos.add,
                element: LazyPage(() => import('@/pages/dashboard/management/vehiculos/add')),
              },
              {
                path: paths.dashboard.management.vehiculos.root + '/:vehiculoId',
                element: LazyPage(() => import('@/pages/dashboard/management/vehiculos/detail')),
              },
            ],
          },
          {
            path: paths.dashboard.management.localidades.root,
            children: [
              {
                index: true,
                path: paths.dashboard.management.localidades.root,
                element: <Navigate to={paths.dashboard.management.localidades.list} replace />,
              },
              {
                path: paths.dashboard.management.localidades.list,
                element: LazyPage(() => import('@/pages/dashboard/management/localidades/list')),
              },
              {
                path: paths.dashboard.management.localidades.add,
                element: LazyPage(() => import('@/pages/dashboard/management/localidades/add')),
              },
              {
                path: paths.dashboard.management.localidades.root + '/:localidadId',
                element: LazyPage(() => import('@/pages/dashboard/management/localidades/detail')),
              },
            ],
          },
        ],
      },
    ],
  },
]);

export function Router() {
  return <RouterProvider router={router} />;
}
