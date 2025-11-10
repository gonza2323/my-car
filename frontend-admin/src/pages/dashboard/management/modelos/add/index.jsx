import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import ModeloCreateForm from './modelo-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Modelos', href: paths.dashboard.management.modelos.root },
  { label: 'Nuevo Modelo' },
];

export default function ModeloCreatePage() {
  return (
    <Page title="Nuevo modelo">
      <PageHeader title="Nuevo modelo" breadcrumbs={breadcrumbs} />
      <ModeloCreateForm />
    </Page>
  );
}
