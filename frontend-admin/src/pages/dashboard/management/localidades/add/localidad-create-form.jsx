import { Box, Button, Grid, Group, TextInput } from '@mantine/core';
import { paths } from '@/routes';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { LocalidadCreateDto as LocalidadCreateSchema } from '@/api/dtos';
import { useCreateLocalidad } from '@/hooks';
import { NavLink, useNavigate } from 'react-router-dom';

export default function LocalidadCreateForm() {
  const navigate = useNavigate();
  const form = useForm({
    validate: zodResolver(LocalidadCreateSchema),
    initialValues: { denominacion: "" },
  });

  const createLocalidad = useCreateLocalidad();

  const handleSubmit = form.onSubmit((values) => {
    createLocalidad.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: "Éxito",
            message: "Localidad creada correctamente",
          });
          navigate(paths.dashboard.management.localidades.list);
        },
        onError: (error) => {
          notifications.show({
            title: "Error",
            message:
              error instanceof Error
                ? error.message
                : "Ocurrió un error inesperado",
            color: "red",
          });
        },
      }
    );
  });

  return (
    <Box component="form" onSubmit={handleSubmit} maw={400}>
      <TextInput
        label="Nombre"
        placeholder="Ingrese el nombre"
        {...form.getInputProps("denominacion")}
      />

      <Group justify="flex-end" mt="md">
        <Button variant='outline' component={NavLink} to={paths.dashboard.management.localidades.list}>
          Cancelar
        </Button>
        <Button type="submit" loading={createLocalidad.isPending}>
          Crear
        </Button>
      </Group>
    </Box>
  );
}
