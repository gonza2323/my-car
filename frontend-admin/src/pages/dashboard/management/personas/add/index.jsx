import { Page } from '@/components/page';
import { PageHeader } from '@/components/page-header';
import { paths } from '@/routes';
import PersonaCreateForm from './persona-create-form';

const breadcrumbs = [
  { label: 'Dashboard', href: paths.dashboard.root },
  { label: 'Management', href: paths.dashboard.management.root },
  { label: 'Personas', href: paths.dashboard.management.personas.root },
  { label: 'Nueva Persona' },
];

export default function PersonaCreatePage() {
  return (
    <Page title="Nueva persona">
      <PageHeader title="Nueva persona" breadcrumbs={breadcrumbs} />
      <PersonaCreateForm />
    </Page>
  );
}
