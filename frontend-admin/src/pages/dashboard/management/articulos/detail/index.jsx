import { Page } from "@/components/page"
import { PageHeader } from "@/components/page-header"
import { paths } from "@/routes"
import ArticuloDetalle from "./articulo-detalle"
import { useParams } from "react-router-dom"

const breadcrumbs = [
  { label: "Dashboard", href: paths.dashboard.root },
  { label: "Management", href: paths.dashboard.management.root },
  { label: "Articulos", href: paths.dashboard.management.articulos.root },
  { label: "Detalle Articulo" }
]

export default function ArticuloDetailPage() {
  const { articuloId } = useParams()

  return (
    <Page title="Detalle articulo">
      <PageHeader title="Detalle articulo" breadcrumbs={breadcrumbs} />
      <ArticuloDetalle articuloId={articuloId} />
    </Page>
  )
}
