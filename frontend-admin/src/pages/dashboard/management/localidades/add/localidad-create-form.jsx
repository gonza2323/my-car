import { Box, Button, Grid, Group, Loader, Select, Stack, TextInput } from '@mantine/core';
import { paths } from '@/routes';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { LocalidadCreateDto as LocalidadCreateSchema } from '@/api/dtos';
import { useCreateLocalidad, useGetDepartamentos, useGetLocalidades, useGetPaises, useGetProvincias } from '@/hooks';
import { NavLink, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

export default function LocalidadCreateForm() {
  const navigate = useNavigate();

  const form = useForm({
    validate: zodResolver(LocalidadCreateSchema),
    mode: 'uncontrolled',
    initialValues: {
      nombre: '',
      paisId: '',
      provinciaId: '',
      departamentoId: '',
    },
    onValuesChange: (values) => {
      setPaisId(values.paisId);
      setProvinciaId(values.provinciaId);
      setDepartamentoId(values.departamentoId);
      setLocalidadId(values.localidadId);
    }
  });

  const [paisId, setPaisId] = useState('');
  const [provinciaId, setProvinciaId] = useState('');
  const [departamentoId, setDepartamentoId] = useState('');
  const [localidadId, setLocalidadId] = useState('');

  const {
    data: paisResponse,
    isLoading: paisLoading,
  } = useGetPaises({ query: { size: 99999 } });

  const paises = paisResponse?.data ?? [];
  const paisOptions = [
    ...paises.map(p => ({ value: String(p.id), label: p.nombre })),
  ];

  const {
    data: provinciaResponse,
    isLoading: provinciaLoading,
  } = useGetProvincias(
    { query: { size: 99999, paisId: paisId } },
    { enabled: !!paisId }
  );

  const provincias = provinciaResponse?.data ?? [];
  const provinciaOptions = [
    ...provincias.map(p => ({ value: String(p.id), label: p.nombre })),
  ];

  const {
    data: departamentoResponse,
    isLoading: departamentoLoading,
  } = useGetDepartamentos(
    { query: { size: 99999, provinciaId: provinciaId } },
    { enabled: !!provinciaId }
  );

  const departamentos = departamentoResponse?.data ?? [];
  const departamentoOptions = [
    ...departamentos.map(d => ({ value: String(d.id), label: d.nombre })),
  ];

  const {
    data: localidadesResponse,
    isLoading: localidadesLoading,
  } = useGetLocalidades(
    { query: { size: 99999, departamentoId: departamentoId } },
    { enabled: !!departamentoId }
  );

  const localidades = localidadesResponse?.data ?? [];
  const localidadesOptions = [
    ...localidades.map(d => ({ value: String(d.id), label: d.nombre })),
  ];

  useEffect(() => {
    if (provinciaId !== '') {
      setProvinciaId('')
      form.setValues({provinciaId: ''});
    }
  }, [paisId]);

  useEffect(() => {
    if (departamentoId !== '') {
      setDepartamentoId('')
      form.setValues({departamentoId: ''});
    }
  }, [provinciaId]);

  useEffect(() => {
    if (localidadId !== '') {
      setLocalidadId('')
      form.setValues({localidadId: ''});
    }
  }, [departamentoId]);

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
        {...form.getInputProps('paisId')}
        label="País"
        searchable={true}
        placeholder="Seleccione un país"
        data={paisOptions}
        disabled={paisLoading}
        rightSection={paisLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
      />

      <Select
        {...form.getInputProps('provinciaId')}
        label="Provincia"
        searchable={true}
        placeholder={
          !paisId
            ? "Seleccione primero un país"
            : provinciaOptions.length === 0
            ? "No se encontraron provincias"
            : "Seleccione una provincia"
        }
        data={provinciaOptions}
        disabled={!paisId || provinciaLoading || !provinciaOptions.length}
        rightSection={provinciaLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
        key={form.key('provinciaId')}
      />

      <Select
        {...form.getInputProps('departamentoId')}
        label="Departamento"
        searchable={true}
        placeholder={
          !provinciaId
            ? "Seleccione primero una provincia"
            : departamentoOptions.length === 0
            ? "No se encontraron departamentos"
            : "Seleccione un departamento"
        }
        data={departamentoOptions}
        disabled={!provinciaId || departamentoLoading || !departamentoOptions.length}
        rightSection={departamentoLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
        key={form.key('departamentoId')}
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