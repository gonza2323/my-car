import { Box, Button, Grid, Group, Select, Stack, TextInput, NumberInput, Loader } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { NavLink, useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { paths } from '@/routes';
import {
  useCreateVehiculo,
  useGetPaises,
  useGetProvincias,
  useGetDepartamentos,
  useGetLocalidades,
  useGetModelos,
} from '@/hooks';
import { DateInput, DatePicker, DatePickerInput } from '@mantine/dates';
import { VehiculoCreateDto } from '@/api/dtos';
import { useEffect, useMemo, useState } from 'react';
import { queryOptions } from '@tanstack/react-query';


export default function VehiculoCreateForm() {
  const navigate = useNavigate();
  const createVehiculo = useCreateVehiculo();

  const form = useForm({
    validate: zodResolver(VehiculoCreateDto),
    mode: 'uncontrolled',
    initialValues: {
      patente: '',
      caracteristicasAutoId: '',
    },
  });

  const {
    data: modelosResponse,
    isLoading: modelosLoading,
  } = useGetModelos({ query: { size: 99999 } });

  const modelos = modelosResponse?.data ?? [];
  const modelosOptions = [
    ...modelos.map(m => ({ value: String(m.id), label: `${m.marca} ${m.modelo} (${m.anio})` })),
  ];

  const handleSubmit = form.onSubmit((values) => {
    createVehiculo.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: 'Éxito',
            message: 'Vehículo creado correctamente',
          });
          navigate(paths.dashboard.management.vehiculos.list);
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

      <Select
        {...form.getInputProps('caracteristicasAutoId')}
        label="Modelo"
        searchable={true}
        placeholder="Seleccione un modelo"
        data={modelosOptions}
        disabled={modelosLoading}
        rightSection={modelosLoading && <Loader size="sm" />}
        withCheckIcon={false}
        allowDeselect={false}
      />

      <TextInput label="Patente" {...form.getInputProps('patente')} />

      <Group position="right" mt="md" justify='end'>
        <Button variant="outline" component={NavLink} to={paths.dashboard.management.vehiculos.list}>
          Cancelar
        </Button>
        <Button type="submit" loading={createVehiculo.isPending}>
          Crear
        </Button>
      </Group>
    </Stack>
  );
}
