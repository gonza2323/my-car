import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { AlquileresTable } from './alquileres-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Alquileres', href: paths.dashboard.management.alquileres.root },
  { label: 'Lista' },
];

export default function ListAlquileresPage() {
  return (
    <Page title="Lista alquileres">
      <PageHeader title="Lista alquileres" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <AlquileresTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
