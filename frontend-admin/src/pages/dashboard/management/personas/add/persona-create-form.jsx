import { Box, Button, Group, Select, TextInput } from '@mantine/core';
import { paths } from '@/routes';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { PersonaCreateDto } from '@/api/dtos';
import { useCreatePersona, useGetLocalidadesFull } from '@/hooks';
import { NavLink, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

export default function PersonaCreateForm() {
  const navigate = useNavigate();
  const createPersona = useCreatePersona();

  const [localidadesData, setLocalidadesData] = useState([]);
  const { data: localidades, isLoading: localidadesLoading } = useGetLocalidadesFull();

  useEffect(() => {
    if (localidades) {
      setLocalidadesData(
        localidades.map((loc) => ({
          value: String(loc.id),
          label: loc.denominacion,
        }))
      );
    }
  }, [localidades]);

  const form = useForm({
    validate: zodResolver(PersonaCreateDto),
    initialValues: {
      nombre: "",
      apellido: "",
      dni: "",
      domicilio: {
        calle: "",
        numeracion: "",
        localidadId: "",
      },
    },
  });

  const handleSubmit = form.onSubmit((values) => {
    createPersona.mutate({ variables: values }, {
      onSuccess: () => {
        notifications.show({
          title: "Éxito",
          message: "Persona creada correctamente",
        });
        navigate(paths.dashboard.management.personas.list);
      },
      onError: (error) => {
        console.error(error)
        notifications.show({
          title: "Error",
          message:
            error instanceof Error
              ? error.message
              : "Ocurrió un error inesperado",
          color: "red",
        });
      },
    });
  });

  return (
    <Box component="form" onSubmit={handleSubmit} maw={500}>
      <TextInput
        label="Nombre"
        placeholder="Ingrese el nombre"
        {...form.getInputProps("nombre")}
        required
      />

      <TextInput
        mt="sm"
        label="Apellido"
        placeholder="Ingrese el apellido"
        {...form.getInputProps("apellido")}
        required
      />

      <TextInput
        mt="sm"
        label="DNI"
        placeholder="Ingrese el DNI"
        {...form.getInputProps("dni")}
        required
      />

      <TextInput
        mt="sm"
        label="Calle"
        placeholder="Ingrese la calle"
        {...form.getInputProps("domicilio.calle")}
        required
      />

      <TextInput
        mt="sm"
        label="Numeración"
        placeholder="Ingrese la numeración"
        {...form.getInputProps("domicilio.numeracion")}
        required
      />

      <Select
        mt="sm"
        label="Localidad"
        placeholder="Seleccione una localidad"
        data={localidadesData}
        {...form.getInputProps("domicilio.localidadId")}
        searchable
        nothingFoundMessage={localidadesLoading ? "Cargando..." : "Sin resultados"}
        required
      />

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.personas.list}
        >
          Cancelar
        </Button>
        <Button type="submit" loading={createPersona.isPending}>
          Crear
        </Button>
      </Group>
    </Box>
  );
}