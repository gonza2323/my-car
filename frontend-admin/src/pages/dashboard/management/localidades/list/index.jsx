import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { LocalidadesTable } from './localidades-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Localidades', href: paths.dashboard.management.localidades.root },
  { label: 'Lista' },
];

export default function ListLocalidadesPage() {
  return (
    <Page title="Lista localidades">
      <PageHeader title="Lista localidades" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <LocalidadesTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
