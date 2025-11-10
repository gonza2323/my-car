import { Page } from "@/components/page"
import { PageHeader } from "@/components/page-header"
import { paths } from "@/routes"
import LocalidadDetalle from "./modelo-detalle"
import { useParams } from "react-router-dom"

const breadcrumbs = [
  { label: "Dashboard", href: paths.dashboard.root },
  { label: "Management", href: paths.dashboard.management.root },
  { label: "Localidades", href: paths.dashboard.management.localidades.root },
  { label: "Detalle Localidad" }
]

export default function LocalidadDetailPage() {
  const { localidadId } = useParams()

  return (
    <Page title="Detalle Localidad">
      <PageHeader title="Detalle Localidad" breadcrumbs={breadcrumbs} />
      <LocalidadDetalle localidadId={localidadId} />
    </Page>
  )
}
