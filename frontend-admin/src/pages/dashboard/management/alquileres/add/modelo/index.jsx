import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { Title } from '@mantine/core';
import { useParams } from 'react-router-dom';
import ModeloRentForm from './model-rent-form';



export default function AlquilerCreatePage() {
  const { modelId } = useParams();

  const breadcrumbs = [
    { label: 'Dashboard', href: paths.dashboard.root },
    { label: 'Management', href: paths.dashboard.management.root },
    { label: 'Alquileres', href: paths.dashboard.management.alquileres.root },
    { label: 'Nuevo Alquiler', href: paths.dashboard.management.alquileres.add.root },
    { label: modelId },
  ];

  return (
    <Page title="Alquilar Modelo">
      <PageHeader title="Alquilar Modelo" breadcrumbs={breadcrumbs} />
      {/* <AlquilerCreateForm /> */}

      <ModeloRentForm />
    </Page>
  );
}
