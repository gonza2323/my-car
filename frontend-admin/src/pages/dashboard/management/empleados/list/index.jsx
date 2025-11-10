import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { EmpleadosTable } from './empleados-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Empleados', href: paths.dashboard.management.empleados.root },
  { label: 'Lista' },
];

export default function ListEmpleadosPage() {
  return (
    <Page title="Lista empleados">
      <PageHeader title="Lista empleados" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <EmpleadosTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
