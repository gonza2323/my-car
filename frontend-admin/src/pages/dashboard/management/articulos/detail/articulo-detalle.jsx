import {
  Alert,
  Button,
  Card,
  Group,
  Loader,
  Stack,
  Text,
  Image
} from "@mantine/core"
import { paths } from "@/routes"
import { NavLink } from "react-router-dom"
import { useGetArticulo } from "@/hooks"
import { app } from "@/config"
import { formatCurrency } from "@/utilities/number"

export default function ArticuloDetalle({ articuloId }) {
  const { data: articulo, isLoading, error } = useGetArticulo({
    route: { id: articuloId }
  })

  if (isLoading) return <Loader />
  if (error)
    return (
      <Alert color="red" maw={400}>
        Error al cargar articulo
      </Alert>
    )

  const imageUrl = articulo?.imagenId
    ? `${app.apiBaseUrl}/imagenes/${articulo.imagenId}`
    : null

  return (
    <Card shadow="sm" padding="lg" maw={500}>
      <Stack gap="sm">
        {imageUrl && (
          <Image
            src={imageUrl}
            alt={`Imagen de ${articulo?.nombre}`}
            radius="md"
            fit="contain"
            height={200}
          />
        )}

        <Group>
          <Text fw={500}>Nombre:</Text>
          <Text>{articulo?.nombre || "-"}</Text>
        </Group>

        <Group>
          <Text fw={500}>Precio:</Text>
          <Text>{articulo ? formatCurrency(articulo.precio) : "-"}</Text>
        </Group>

        <Group>
          <Text fw={500}>Proveedor:</Text>
          <Text>{articulo?.proveedorNombre || "-"}</Text>
        </Group>
      </Stack>

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.articulos.list}
        >
          Volver
        </Button>
      </Group>
    </Card>
  )
}
