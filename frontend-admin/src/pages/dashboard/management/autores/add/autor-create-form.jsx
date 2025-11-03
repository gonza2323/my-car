import { Box, Button, Group, Textarea, TextInput } from "@mantine/core"
import { paths } from "@/routes"
import { useForm, zodResolver } from "@mantine/form"
import { notifications } from "@mantine/notifications"
import { AutorCreateDto } from "@/api/dtos"
import { useCreateAutor } from "@/hooks"
import { NavLink, useNavigate } from "react-router-dom"

export default function AutorCreateForm() {
  const navigate = useNavigate()
  const form = useForm({
    validate: zodResolver(AutorCreateDto),
    initialValues: {
      nombre: "",
      apellido: "",
      biografia: ""
    }
  })

  const createAutor = useCreateAutor()

  const handleSubmit = form.onSubmit(values => {
    createAutor.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: "Éxito",
            message: "Autor creado correctamente"
          })
          navigate(paths.dashboard.management.autores.list)
        },
        onError: error => {
          notifications.show({
            title: "Error",
            message:
              error instanceof Error
                ? error.message
                : "Ocurrió un error inesperado",
            color: "red"
          })
        }
      }
    )
  })

  return (
    <Box component="form" onSubmit={handleSubmit} maw={400}>
      <TextInput
        label="Nombre"
        placeholder="Ingrese el nombre"
        {...form.getInputProps("nombre")}
      />

      <TextInput
        label="Apellido"
        placeholder="Ingrese el apellido"
        {...form.getInputProps("apellido")}
      />

      <Textarea
        label="Biografía"
        placeholder="Biografía del autor"
        {...form.getInputProps("biografia")}
      />

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.autores.list}
        >
          Cancelar
        </Button>
        <Button type="submit" loading={createAutor.isPending}>
          Crear
        </Button>
      </Group>
    </Box>
  )
}
