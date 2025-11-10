import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { ModelosTable } from './modelos-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Modelos', href: paths.dashboard.management.modelos.root },
  { label: 'Lista' },
];

export default function ListModelosPage() {
  return (
    <Page title="Lista modelos">
      <PageHeader title="Lista modelos" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <ModelosTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
