import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { AutoresTable } from './autores-list';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Autores', href: paths.dashboard.management.autores.root },
  { label: 'Lista' },
];

export default function ListAutoresPage() {
  return (
    <Page title="Lista autores">
      <PageHeader title="Lista autores" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <AutoresTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
