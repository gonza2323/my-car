import { Alert, Button, Card, Group, Image, Loader, Stack, Text } from "@mantine/core"
import { paths } from "@/routes"
import { NavLink } from "react-router-dom"
import { useGetModelo } from "@/hooks"
import { app } from "@/config"

export default function ModeloDetalle({ modeloId }) {
  const { data: modelo, isLoading, error } = useGetModelo({
    route: { id: modeloId }
  })

  if (isLoading) return <Loader />
  if (error)
    return (
      <Alert color="red" maw={400}>
        Error al cargar modelo
      </Alert>
    )


  return (
    <Card shadow="sm" padding="lg" maw={500}>
      <Card.Section>
        <Image
          src={`${app.apiBaseUrl}/vehiculos/${modelo.id}/imagen`}
          height={280}
          alt="Norway"
        />
      </Card.Section>

      <Stack gap="sm" mt="md">
        <Group>
          <Text color="dimmed">Marca:</Text>
          <Text>{modelo?.marca || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">Modelo:</Text>
          <Text>{modelo?.modelo || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">Año:</Text>
          <Text>{modelo?.anio || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">Asientos:</Text>
          <Text>{modelo?.cantidadAsientos || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">Puertas:</Text>
          <Text>{modelo?.cantidadPuertas || "-"}</Text>
        </Group>
        <Group>
          <Text color="dimmed">Cantidad de unidades:</Text>
          <Text>{modelo?.cantTotalAutos || "-"}</Text>
        </Group>
      </Stack>

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.modelos.list}
        >
          Volver
        </Button>
      </Group>
    </Card>
  )
}
