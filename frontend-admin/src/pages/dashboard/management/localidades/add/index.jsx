import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import LocalidadCreateForm from './localidad-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Localidades', href: paths.dashboard.management.localidades.root },
  { label: 'Nueva Localidad' },
];

export default function LocalidadCreatePage() {
  return (
    <Page title="Nueva localidad">
      <PageHeader title="Nueva localidad" breadcrumbs={breadcrumbs} />
      <LocalidadCreateForm />
    </Page>
  );
}
