import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import ClienteCreateForm from './cliente-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Clientes', href: paths.dashboard.management.clientes.root },
  { label: 'Nuevo Cliente' },
];

export default function ClienteCreatePage() {
  return (
    <Page title="Nuevo cliente">
      <PageHeader title="Nuevo cliente" breadcrumbs={breadcrumbs} />
      <ClienteCreateForm />
    </Page>
  );
}
