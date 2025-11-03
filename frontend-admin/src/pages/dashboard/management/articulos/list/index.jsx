import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { ArticulosTable } from './articulos-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Artículos', href: paths.dashboard.management.articulos.root },
  { label: 'Lista' },
];

export default function ListArticulosPage() {
  return (
    <Page title="Lista articulos">
      <PageHeader title="Lista articulos" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <ArticulosTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
