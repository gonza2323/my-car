import { Box, Button, Grid, Group, Select, Stack, TextInput, NumberInput, Loader } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { NavLink, useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { paths } from '@/routes';
import {
  useCreateCliente,
  useGetPaises,
  useGetProvincias,
  useGetDepartamentos,
  useGetLocalidades,
  useGetNacionalidades,
} from '@/hooks';
import { DateInput, DatePicker, DatePickerInput } from '@mantine/dates';
import { ClienteCreateDto } from '@/api/dtos';
import { useEffect, useMemo, useState } from 'react';
import { queryOptions } from '@tanstack/react-query';


export default function ClienteCreateForm() {
  const navigate = useNavigate();
  const createCliente = useCreateCliente();

  const form = useForm({
    validate: zodResolver(ClienteCreateDto),
    mode: 'uncontrolled',
    initialValues: {
      nombre: '',
      apellido: '',
      fechaNacimiento: null,
      tipoDocumento: 'DNI',
      numeroDocumento: '',
      telefono: '',
      email: '',
      nacionalidadId: '',
      direccion: {
        calle: '',
        numeracion: '',
        barrio: '',
        manzanaPiso: '',
        casaDepartamento: '',
        referencia: '',
        paisId: '',
        provinciaId: '',
        departamentoId: '',
        localidadId: '',
      },
    },
    onValuesChange: (values) => {
      setPaisId(values.direccion.paisId);
      setProvinciaId(values.direccion.provinciaId);
      setDepartamentoId(values.direccion.departamentoId);
      setLocalidadId(values.direccion.localidadId);
    }
  });

  const [paisId, setPaisId] = useState('');
  const [provinciaId, setProvinciaId] = useState('');
  const [departamentoId, setDepartamentoId] = useState('');
  const [localidadId, setLocalidadId] = useState('');

  const {
    data: nacionalidadResponse,
    isLoading: nacionalidadLoading,
  } = useGetNacionalidades({ query: { size: 99999 } });

  const nacionalidades = nacionalidadResponse?.data ?? [];
  const nacionalidadOptions = [
    ...nacionalidades.map(p => ({ value: String(p.id), label: p.nombre })),
  ];

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
  const localidadOptions = [
    ...localidades.map(d => ({ value: String(d.id), label: d.nombre })),
  ];

  useEffect(() => {
    if (provinciaId !== '') {
      setProvinciaId('')
      form.setValues({ provinciaId: '' });
    }
  }, [paisId]);

  useEffect(() => {
    if (departamentoId !== '') {
      setDepartamentoId('')
      form.setValues({ departamentoId: '' });
    }
  }, [provinciaId]);

  useEffect(() => {
    if (localidadId !== '') {
      setLocalidadId('')
      form.setValues({ localidadId: '' });
    }
  }, [departamentoId]);

  const handleSubmit = form.onSubmit((values) => {
    createCliente.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: 'Éxito',
            message: 'Cliente creado correctamente',
          });
          navigate(paths.dashboard.management.clientes.list);
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
    <Stack component="form" onSubmit={handleSubmit}>
      <Grid>
        <Grid.Col span={6}>
          <TextInput label="Nombre" {...form.getInputProps('nombre')} />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput label="Apellido" {...form.getInputProps('apellido')} />
        </Grid.Col>
      </Grid>

      <DateInput
        label="Fecha de nacimiento"
        placeholder="Seleccione la fecha"
        valueFormat="YYYY-MM-DD"
        {...form.getInputProps('fechaNacimiento')}
      />

      <Select
        label="Tipo de documento"
        data={['DNI', 'PASAPORTE']}
        allowDeselect={false}
        checkIconPosition='right'
        {...form.getInputProps('tipoDocumento')}
      />

      <TextInput label="Número de documento" {...form.getInputProps('numeroDocumento')} />
      <TextInput label="Teléfono" {...form.getInputProps('telefono')} />
      <TextInput label="Email" {...form.getInputProps('email')} />

      <Select
        {...form.getInputProps('nacionalidadId')}
        label="Nacionalidad"
        searchable={true}
        placeholder="Seleccione la nacionalidad"
        data={nacionalidadOptions}
        disabled={nacionalidadLoading}
        rightSection={nacionalidadLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
      />


      <Stack mt="md">
        <Select
          {...form.getInputProps('direccion.paisId')}
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
          {...form.getInputProps('direccion.provinciaId')}
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
          {...form.getInputProps('direccion.departamentoId')}
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

        <Select
          {...form.getInputProps('direccion.localidadId')}
          label="Localidad"
          searchable={true}
          placeholder={
            !departamentoId
              ? "Seleccione primero un departamento"
              : localidadOptions.length === 0
                ? "No se encontraron localidades"
                : "Seleccione una localidad"
          }
          data={localidadOptions}
          disabled={!departamentoId || localidadesLoading || !localidadOptions.length}
          rightSection={localidadesLoading && <Loader size="sm" />}
          withCheckIcon={false}
          allowDeselect={false}
          key={form.key('localidadId')}
        />

        <TextInput label="Calle" {...form.getInputProps('direccion.calle')} />
        <TextInput label="Numeración" {...form.getInputProps('direccion.numeracion')} />
        <TextInput label="Barrio" {...form.getInputProps('direccion.barrio')} />
        <TextInput label="Manzana / Piso" {...form.getInputProps('direccion.manzanaPiso')} />
        <TextInput label="Casa / Departamento" {...form.getInputProps('direccion.casaDepartamento')} />
        <TextInput label="Referencia" {...form.getInputProps('direccion.referencia')} />
      </Stack>

      <Group position="right" mt="md">
        <Button variant="outline" component={NavLink} to={paths.dashboard.management.clientes.list}>
          Cancelar
        </Button>
        <Button type="submit" loading={createCliente.isPending}>
          Crear
        </Button>
      </Group>
    </Stack>
  );
}
