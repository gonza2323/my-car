import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import EmpleadoCreateForm from './empleado-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Empleados', href: paths.dashboard.management.empleados.root },
  { label: 'Nuevo Empleado' },
];

export default function EmpleadoCreatePage() {
  return (
    <Page title="Nuevo empleado">
      <PageHeader title="Nuevo empleado" breadcrumbs={breadcrumbs} />
      <EmpleadoCreateForm />
    </Page>
  );
}
