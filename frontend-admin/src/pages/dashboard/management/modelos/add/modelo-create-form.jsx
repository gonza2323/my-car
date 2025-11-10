import { Box, Button, Grid, Group, Select, Stack, TextInput, NumberInput, Loader } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { NavLink, useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { paths } from '@/routes';
import {
  useCreateModelo,
  useGetPaises,
  useGetProvincias,
  useGetDepartamentos,
  useGetLocalidades,
} from '@/hooks';
import { DateInput, DatePicker, DatePickerInput } from '@mantine/dates';
import { ModeloCreateDto } from '@/api/dtos';
import { useEffect, useMemo, useState } from 'react';
import { queryOptions } from '@tanstack/react-query';


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
    },
  });

  const handleSubmit = form.onSubmit((values) => {
    createModelo.mutate(
      { variables: values },
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
      <NumberInput label="Cantidad de asientos" mih={0} max={10} allowDecimal={false} {...form.getInputProps('cantidadAsientos')} />

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
