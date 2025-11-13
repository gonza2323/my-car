import { useForm, zodResolver } from '@mantine/form';
import { Stack, TextInput, NumberInput, Group, Button, FileInput, Notification } from '@mantine/core';
import { useNavigate, NavLink } from 'react-router-dom';
import { z } from 'zod';
import { useCreateModelo } from '@/hooks';
import { paths } from '@/routes';

export const ModeloCreateDto = z.object({
  marca: z.string().nonempty('Debe indicar la marca').max(50),
  modelo: z.string().nonempty('Debe indicar el modelo').max(50),
  anio: z.coerce.number('Debe indicar el modelo').int().min(1960).max(2030),
  cantidadPuertas: z.coerce.number('Debe indicar la cantidad de puertas').int().min(0).max(10),
  cantidadAsientos: z.coerce.number('Debe indicar la cantidad de asientos').int().min(0).max(10),
});

export default function ModeloCreateForm() {
  const navigate = useNavigate();
  const createModelo = useCreateModelo();

  const form = useForm({
    validate: zodResolver(ModeloCreateDto),
    mode: 'uncontrolled',
    initialValues: {
      marca: '',
      modelo: '',
      anio: '',
      cantidadPuertas: '',
      cantidadAsientos: '',
      image: null, // optional image
    },
  });

  const handleSubmit = form.onSubmit((values) => {
    // create FormData to send as multipart/form-data
    const formData = new FormData();
    formData.append('dto', new Blob([JSON.stringify({
      marca: values.marca,
      modelo: values.modelo,
      anio: values.anio,
      cantidadPuertas: values.cantidadPuertas,
      cantidadAsientos: values.cantidadAsientos,
    })], { type: 'application/json' }));

    if (values.image) {
      formData.append('image', values.image);
    }

    createModelo.mutate(
      formData,
      {
        onSuccess: () => {
          notifications.show({
            title: 'Éxito',
            message: 'Modelo creado correctamente',
          });
          navigate(paths.dashboard.management.modelos.list);
        },
        onError: (error) => {
          notifications.show({
            title: 'Error',
            message: error instanceof Error ? error.message : 'Ocurrió un error inesperado',
            color: 'red',
          });
        },
      }
    );
  });

  return (
    <Stack component="form" onSubmit={handleSubmit} maw={400}>
      <TextInput label="Marca" {...form.getInputProps('marca')} />
      <TextInput label="Modelo" {...form.getInputProps('modelo')} />
      <NumberInput label="Año" min={1960} max={2030} allowDecimal={false} {...form.getInputProps('anio')} />
      <NumberInput label="Cantidad de puertas" min={0} max={10} allowDecimal={false} {...form.getInputProps('cantidadPuertas')} />
      <NumberInput label="Cantidad de asientos" min={0} max={10} allowDecimal={false} {...form.getInputProps('cantidadAsientos')} />

      {/* File picker for optional image */}
      <FileInput
        label="Imagen (opcional)"
        placeholder="Seleccione una imagen"
        accept="image/*"
        {...form.getInputProps('image')}
      />

      <Group position="right" mt="md">
        <Button variant="outline" component={NavLink} to={paths.dashboard.management.modelos.list}>
          Cancelar
        </Button>
        <Button type="submit" loading={createModelo.isPending}>
          Crear
        </Button>
      </Group>
    </Stack>
  );
}
