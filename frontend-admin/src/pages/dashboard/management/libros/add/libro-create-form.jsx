import {
  Box,
  Button,
  Group,
  MultiSelect,
  NumberInput,
  Select,
  TextInput
} from "@mantine/core"
import { paths } from "@/routes"
import { useForm, zodResolver } from "@mantine/form"
import { notifications } from "@mantine/notifications"
import { LibroCreateDto } from "@/api/dtos"
import { useCreateLibro, useGetAutoresFull, useGetPersonasFull } from "@/hooks"
import { NavLink, useNavigate } from "react-router-dom"

export default function LibroCreateForm() {
  const navigate = useNavigate()
  const createLibro = useCreateLibro()

  const { data: autores, isLoading: autoresLoading } = useGetAutoresFull()
  const { data: personas, isLoading: personasLoading } = useGetPersonasFull()

  const autoresData =
    autores?.map(a => ({ value: String(a.id), label: a.nombre })) || []
  const personasData =
    personas?.map(p => ({
      value: String(p.id),
      label: `${p.nombre} ${p.apellido}`
    })) || []

  const form = useForm({
    validate: zodResolver(LibroCreateDto),
    initialValues: {
      titulo: "",
      fecha: 2025,
      genero: "",
      paginas: 100,
      personaId: "",
      autoresIds: []
    }
  })

  const handleSubmit = form.onSubmit(values => {
    createLibro.mutate(
      // values are already transformed
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: "Éxito",
            message: "Libro creado correctamente"
          })
          navigate(paths.dashboard.management.libros.list)
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
    <Box component="form" onSubmit={handleSubmit} maw={500}>
      <TextInput
        label="Título"
        placeholder="Ingrese el título"
        {...form.getInputProps("titulo")}
        required
      />

      <NumberInput
        mt="sm"
        label="Año"
        placeholder="Ingrese el año"
        {...form.getInputProps("fecha")}
        required
      />

      <TextInput
        mt="sm"
        label="Género"
        placeholder="Ingrese el género"
        {...form.getInputProps("genero")}
        required
      />

      <NumberInput
        mt="sm"
        label="Páginas"
        placeholder="Ingrese la cantidad de páginas"
        {...form.getInputProps("paginas")}
        required
      />

      <Select
        mt="sm"
        label="Persona dueña"
        placeholder="Seleccione la persona"
        data={personasData}
        {...form.getInputProps("personaId")}
        searchable
        nothingFoundMessage={personasLoading ? "Cargando..." : "Sin resultados"}
        required
      />

      <MultiSelect
        mt="sm"
        label="Autores"
        placeholder="Seleccione los autores"
        data={autoresData}
        {...form.getInputProps("autoresIds")}
        searchable
        nothingFoundMessage={autoresLoading ? "Cargando..." : "Sin resultados"}
        required
      />

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.libros.list}
        >
          Cancelar
        </Button>
        <Button type="submit" loading={createLibro.isPending}>
          Crear
        </Button>
      </Group>
    </Box>
  )
}
