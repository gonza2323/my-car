import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import AutorCreateForm from './autor-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Autores', href: paths.dashboard.management.autores.root },
  { label: 'Nuevo Autor' },
];

export default function AutorCreatePage() {
  return (
    <Page title="Nuevo autor">
      <PageHeader title="Nuevo autor" breadcrumbs={breadcrumbs} />
      <AutorCreateForm />
    </Page>
  );
}
