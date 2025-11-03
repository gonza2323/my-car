import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import ProveedorCreateForm from './articulo-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Proveedores', href: paths.dashboard.management.articulos.root },
  { label: 'Nuevo Proveedor' },
];

export default function ProveedorCreatePage() {
  return (
    <Page title="Nuevo articulo">
      <PageHeader title="Nuevo articulo" breadcrumbs={breadcrumbs} />
      <ProveedorCreateForm />
    </Page>
  );
}
