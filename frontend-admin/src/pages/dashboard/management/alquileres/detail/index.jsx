import { Page } from "@/components/page"
import { PageHeader } from "@/components/page-header"
import { paths } from "@/routes"
import ModeloDetalle from "./modelo-detalle"
import { useParams } from "react-router-dom"

const breadcrumbs = [
  { label: "Dashboard", href: paths.dashboard.root },
  { label: "Management", href: paths.dashboard.management.root },
  { label: "Modelos", href: paths.dashboard.management.modelos.root },
  { label: "Detalle Modelo" }
]

export default function ModeloDetailPage() {
  const { modeloId } = useParams()

  return (
    <Page title="Detalle Modelo">
      <PageHeader title="Detalle Modelo" breadcrumbs={breadcrumbs} />
      <ModeloDetalle modeloId={modeloId} />
    </Page>
  )
}
