import { Box, Button, Grid, Group, Loader, Select, Stack, TextInput } from '@mantine/core';
import { paths } from '@/routes';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { LocalidadCreateDto as LocalidadCreateSchema } from '@/api/dtos';
import { useCreateLocalidad, useGetDepartamentos, useGetPaises, useGetProvincias } from '@/hooks';
import { NavLink, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

export default function LocalidadCreateForm() {
  const navigate = useNavigate();

  const form = useForm({
    validate: zodResolver(LocalidadCreateSchema),
    initialValues: {
      nombre: '',
      paisId: '',
      provinciaId: '',
      departamentoId: '',
    },
  });

  const {
    data: paisResponse,
    isLoading: paisLoading,
  } = useGetPaises({ query: { size: 99999 } });

  const paises = paisResponse?.data ?? [];
  const paisOptions = [
    { value: '', label: 'Seleccione un país' },
    ...paises.map(p => ({ value: String(p.id), label: p.nombre })),
  ];

  const {
    data: provinciaResponse,
    isLoading: provinciaLoading,
  } = useGetProvincias(
    { query: { size: 99999, paisId: form.values.paisId } },
    { enabled: !!form.values.paisId }
  );

  const provincias = provinciaResponse?.data ?? [];
  const provinciaOptions = [
    { value: '', label: 'Seleccione una provincia' },
    ...provincias.map(p => ({ value: String(p.id), label: p.nombre })),
  ];

  const {
    data: departamentoResponse,
    isLoading: departamentoLoading,
  } = useGetDepartamentos(
    { query: { size: 99999, provinciaId: form.values.provinciaId } },
    { enabled: !!form.values.provinciaId }
  );

  const departamentos = departamentoResponse?.data ?? [];
  const departamentoOptions = [
    { value: '', label: 'Seleccione un departamento' },
    ...departamentos.map(d => ({ value: String(d.id), label: d.nombre })),
  ];

  useEffect(() => {
    form.setFieldValue('provinciaId', '');
  }, [form.values.paisId]);

  useEffect(() => {
    form.setFieldValue('departamentoId', '');
  }, [form.values.provinciaId]);

  // --- Create mutation
  const createLocalidad = useCreateLocalidad();

  const handleSubmit = form.onSubmit((values) => {
    createLocalidad.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: 'Éxito',
            message: 'Localidad creada correctamente',
          });
          navigate(paths.dashboard.management.localidades.list);
        },
        onError: (error) => {
          notifications.show({
            title: 'Error',
            message:
              error instanceof Error
                ? error.message
                : 'Ocurrió un error inesperado',
            color: 'red',
          });
        },
      }
    );
  });

  return (
    <Stack component="form" onSubmit={handleSubmit} maw={400}>
      <TextInput
        label="Nombre"
        placeholder="Ingrese el nombre"
        {...form.getInputProps('nombre')}
      />

      <Select
        label="País"
        searchable={true}
        placeholder="Seleccione un país"
        data={paisOptions}
        disabled={paisLoading}
        rightSection={paisLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
        {...form.getInputProps('paisId')}
      />

      <Select
        label="Provincia"
        searchable={true}
        placeholder={
          form.values.paisId ? 'Seleccione una provincia' : 'Seleccione un país primero'
        }
        data={provinciaOptions}
        disabled={!form.values.paisId || provinciaLoading}
        rightSection={provinciaLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
        {...form.getInputProps('provinciaId')}
      />

      <Select
        label="Departamento"
        searchable={true}
        placeholder={
          form.values.provinciaId ? 'Seleccione un departamento' : 'Seleccione una provincia primero'
        }
        data={departamentoOptions}
        disabled={!form.values.provinciaId || departamentoLoading}
        rightSection={departamentoLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
        {...form.getInputProps('departamentoId')}
      />

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.localidades.list}
        >
          Cancelar
        </Button>
        <Button type="submit" loading={createLocalidad.isPending}>
          Crear
        </Button>
      </Group>
    </Stack>
  );
}