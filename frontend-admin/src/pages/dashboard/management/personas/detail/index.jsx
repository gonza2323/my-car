import { Page } from "@/components/page"
import { PageHeader } from "@/components/page-header"
import { paths } from "@/routes"
import ProveedorDetalle from "./persona-detail"
import { useParams } from "react-router-dom"

const breadcrumbs = [
  { label: "Dashboard", href: paths.dashboard.root },
  { label: "Management", href: paths.dashboard.management.root },
  { label: "Proveedores", href: paths.dashboard.management.proveedores.root },
  { label: "Detalle Proveedor" }
]

export default function PersonaDetailPage() {
  const { proveedorId } = useParams()

  return (
    <Page title="Detalle proveedor">
      <PageHeader title="Detalle proveedor" breadcrumbs={breadcrumbs} />
      <ProveedorDetalle proveedorId={proveedorId} />
    </Page>
  )
}
