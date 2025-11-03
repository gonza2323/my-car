import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { PersonasTable } from './persona-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Personas', href: paths.dashboard.management.personas.root },
  { label: 'Lista' },
];

export default function ListPersonasPage() {
  return (
    <Page title="Lista personas">
      <PageHeader title="Lista personas" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <PersonasTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
