import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { ClientesTable } from './clientes-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Clientes', href: paths.dashboard.management.clientes.root },
  { label: 'Lista' },
];

export default function ListClientesPage() {
  return (
    <Page title="Lista clientes">
      <PageHeader title="Lista clientes" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <ClientesTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
