import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import LibroCreateForm from './libro-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Libros', href: paths.dashboard.management.libros.root },
  { label: 'Nuevo Libro' },
];

export default function LibroCreatePage() {
  return (
    <Page title="Nuevo libro">
      <PageHeader title="Nuevo libro" breadcrumbs={breadcrumbs} />
      <LibroCreateForm />
    </Page>
  );
}
