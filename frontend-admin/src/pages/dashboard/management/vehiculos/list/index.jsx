import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { VehiculosTable } from './vehiculos-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Vehículos', href: paths.dashboard.management.vehiculos.root },
  { label: 'Lista' },
];

export default function ListVehiculosPage() {
  return (
    <Page title="Lista vehículos">
      <PageHeader title="Lista vehículos" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <VehiculosTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
