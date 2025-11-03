import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { LibrosTable } from './libros-list';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Libros', href: paths.dashboard.management.libros.root },
  { label: 'Lista' },
];

export default function ListLibrosPage() {
  return (
    <Page title="Lista libros">
      <PageHeader title="Lista libros" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <LibrosTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
