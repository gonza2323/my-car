import { Box, Button, Grid, Group, Select, Stack, TextInput, NumberInput, Loader } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { NavLink, useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { paths } from '@/routes';
import {
  useCreateEmpleado,
  useGetPaises,
  useGetProvincias,
  useGetDepartamentos,
  useGetLocalidades,
} from '@/hooks';
import { DateInput, DatePicker, DatePickerInput } from '@mantine/dates';
import { EmpleadoCreateDto } from '@/api/dtos';
import { useEffect, useMemo } from 'react';


export default function EmpleadoCreateForm() {
  const navigate = useNavigate();
  const createEmpleado = useCreateEmpleado();

  const form = useForm({
    validate: zodResolver(EmpleadoCreateDto),
    initialValues: {
      nombre: '',
      apellido: '',
      fechaNacimiento: null,
      tipoDocumento: 'DNI',
      numeroDocumento: '',
      telefono: '',
      tipoEmpleado: 'JEFE',
      email: '',
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
  });

  const {
    data: paisResponse,
    isLoading: paisLoading,
  } = useGetPaises({ query: { size: 99999 } });

  const paises = paisResponse?.data ?? [];
const paisOptions = useMemo(() => {
  const paises = paisResponse?.data ?? [];
  return [
    { value: '', label: 'Seleccione un país' },
    ...paises.map(p => ({ value: String(p.id), label: p.nombre })),
  ];
}, [paisResponse?.data]);

  const {
    data: provinciaResponse,
    isLoading: provinciaLoading,
  } = useGetProvincias(
    { query: { size: 99999, paisId: form.values.direccion.paisId } },
    { enabled: !!form.values.direccion.paisId }
  );

  const provincias = provinciaResponse?.data ?? [];
const provinciaOptions = useMemo(() => {
  const provincias = provinciaResponse?.data ?? [];
  return [
    { value: '', label: 'Seleccione una provincia' },
    ...provincias.map(p => ({ value: String(p.id), label: p.nombre })),
  ];
}, [provinciaResponse?.data]);

  const {
    data: departamentoResponse,
    isLoading: departamentoLoading,
  } = useGetDepartamentos(
    { query: { size: 99999, provinciaId: form.values.direccion.provinciaId } },
    { enabled: !!form.values.direccion.provinciaId }
  );

  const departamentos = departamentoResponse?.data ?? [];
const departamentoOptions = useMemo(() => {
  const departamentos = departamentoResponse?.data ?? [];
  return [
    { value: '', label: 'Seleccione un departamento' },
    ...departamentos.map(d => ({ value: String(d.id), label: d.nombre })),
  ];
}, [departamentoResponse?.data]);

  const {
    data: localidadResponse,
    isLoading: localidadLoading,
  } = useGetLocalidades(
    { query: { size: 99999, departamentoId: form.values.direccion.departamentoId } },
    { enabled: !!form.values.direccion.departamentoId }
  );

  const localidades = localidadResponse?.data ?? [];
const localidadOptions = useMemo(() => {
  const localidades = localidadResponse?.data ?? [];
  return [
    { value: '', label: 'Seleccione una localidad' },
    ...localidades.map(l => ({ value: String(l.id), label: l.nombre })),
  ];
}, [localidadResponse?.data]);

  // useEffect(() => {
  //   console.log("clear provincias")
  //   form.setFieldValue('direccion.provinciaId', '');
  // }, [form.values.direccion.paisId]);

  // useEffect(() => {
  //   console.log("clear departamentos")
  //   form.setFieldValue('direccion.departamentoId', '');
  // }, [form.values.direccion.provinciaId]);

  // useEffect(() => {
  //   console.log("clear localidades")
  //   form.setFieldValue('direccion.localidadId', '');
  // }, [form.values.direccion.departamentoId]);

  const handleSubmit = form.onSubmit((values) => {
    console.log(values)
    createEmpleado.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: 'Éxito',
            message: 'Empleado creado correctamente',
          });
          navigate(paths.dashboard.management.empleados.list);
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
    <Stack component="form" onSubmit={handleSubmit} maw={600}>
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

      <Select
        label="Tipo de empleado"
        data={['JEFE', 'ADMINISTRATIVO']}
        allowDeselect={false}
        checkIconPosition='right'
        {...form.getInputProps('tipoEmpleado')}
      />

      <TextInput label="Email" {...form.getInputProps('email')} />

      <Stack mt="md">
        <Select
          label="País"
          searchable
          data={paisOptions}
          disabled={paisLoading}
          withCheckIcon={false}
          allowDeselect={false}
          {...form.getInputProps('direccion.paisId')}
        />
        <Select
          label="Provincia"
          searchable
          data={provinciaOptions}
          disabled={!form.values.direccion.paisId || provinciaLoading}
          withCheckIcon={false}
          allowDeselect={false}
          {...form.getInputProps('direccion.provinciaId')}
        />

        <Select
          label="Departamento"
          searchable
          data={departamentoOptions}
          disabled={!form.values.direccion.provinciaId || departamentoLoading}
          withCheckIcon={false}
          allowDeselect={false}
          {...form.getInputProps('direccion.departamentoId')}
        />
        <Select
          label="Localidad"
          searchable
          data={localidadOptions}
          disabled={!form.values.direccion.departamentoId || localidadLoading}
          withCheckIcon={false}
          allowDeselect={false}
          {...form.getInputProps('direccion.localidadId')}
        />

        <TextInput label="Calle" {...form.getInputProps('direccion.calle')} />
        <TextInput label="Numeración" {...form.getInputProps('direccion.numeracion')} />
        <TextInput label="Barrio" {...form.getInputProps('direccion.barrio')} />
        <TextInput label="Manzana / Piso" {...form.getInputProps('direccion.manzanaPiso')} />
        <TextInput label="Casa / Departamento" {...form.getInputProps('direccion.casaDepartamento')} />
        <TextInput label="Referencia" {...form.getInputProps('direccion.referencia')} />
      </Stack>

      <Group position="right" mt="md">
        <Button variant="outline" component={NavLink} to={paths.dashboard.management.empleados.list}>
          Cancelar
        </Button>
        <Button type="submit" loading={createEmpleado.isPending}>
          Crear
        </Button>
      </Group>
    </Stack>
  );
}
