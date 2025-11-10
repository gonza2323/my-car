import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import VehiculoCreateForm from './vehiculo-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Vehículos', href: paths.dashboard.management.vehiculos.root },
  { label: 'Nuevo Vehículo' },
];

export default function VehiculoCreatePage() {
  return (
    <Page title="Nuevo vehículo">
      <PageHeader title="Nuevo vehículo" breadcrumbs={breadcrumbs} />
      <VehiculoCreateForm />
    </Page>
  );
}
