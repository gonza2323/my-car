import { Alert, Button, Card, Group, Loader, Stack, Text } from "@mantine/core"
import { paths } from "@/routes"
import { NavLink } from "react-router-dom"
import { useGetLocalidad } from "@/hooks"

export default function LocalidadDetalle({ localidadId }) {
  const { data: localidad, isLoading, error } = useGetLocalidad({
    route: { id: localidadId }
  })

  if (isLoading) return <Loader />
  if (error)
    return (
      <Alert color="red" maw={400}>
        Error al cargar localidad
      </Alert>
    )

  return (
    <Card shadow="sm" padding="lg" maw={500}>
      <Stack gap="sm">
        <Group>
          <Text color="dimmed">Nombre:</Text>
          <Text>{localidad?.nombre || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">Departamento:</Text>
          <Text>{localidad?.departamentoNombre || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">Provincia:</Text>
          <Text>{localidad?.provinciaNombre || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">País:</Text>
          <Text>{localidad?.paisNombre || "-"}</Text>
        </Group>
      </Stack>

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.localidades.list}
        >
          Volver
        </Button>
      </Group>
    </Card>
  )
}
