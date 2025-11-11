import { Grid } from '@mantine/core';
import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import { PromocionesTable } from './promociones-table';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Promociones', href: paths.dashboard.management.promociones.root },
  { label: 'Lista' },
];

export default function ListPromocionesPage() {
  return (
    <Page title="Lista promociones">
      <PageHeader title="Lista promociones" breadcrumbs={breadcrumbs} />

      <Grid>
        {/* <Grid.Col span={12}>
          <ProveedorMetrics />
        </Grid.Col> */}

        <Grid.Col span={12}>
          <PromocionesTable />
        </Grid.Col>
      </Grid>
    </Page>
  );
}
