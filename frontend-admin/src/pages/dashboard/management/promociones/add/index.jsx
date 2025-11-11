import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import PromocionCreateForm from './promocion-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Promociones', href: paths.dashboard.management.promociones.root },
  { label: 'Nuevo Promocion' },
];

export default function PromocionCreatePage() {
  return (
    <Page title="Nuevo promocion">
      <PageHeader title="Nuevo promocion" breadcrumbs={breadcrumbs} />
      <PromocionCreateForm />
    </Page>
  );
}
