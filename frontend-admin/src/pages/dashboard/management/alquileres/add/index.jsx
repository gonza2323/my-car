import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { ModelosList } from './modelos-available-list';
import { Title } from '@mantine/core';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Alquileres', href: paths.dashboard.management.alquileres.root },
  { label: 'Nuevo Alquiler' },
];

export default function AlquilerCreatePage() {
  return (
    <Page title="Nuevo alquiler">
      <PageHeader title="Nuevo alquiler" breadcrumbs={breadcrumbs} />
      {/* <AlquilerCreateForm /> */}

      <Title>Seleccione el modelo a alquilar</Title>
      <ModelosList />
    </Page>
  );
}
