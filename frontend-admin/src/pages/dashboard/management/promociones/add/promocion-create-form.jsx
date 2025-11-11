import { Box, Button, Grid, Group, Select, Stack, TextInput, NumberInput, Loader } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { NavLink, useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { paths } from '@/routes';
import {
  useCreatePromocion,
  useGetPaises,
  useGetProvincias,
  useGetDepartamentos,
  useGetLocalidades,
} from '@/hooks';
import { DateInput, DatePicker, DatePickerInput } from '@mantine/dates';
import { PromocionCreateDto } from '@/api/dtos';
import { useEffect, useMemo, useState } from 'react';
import { queryOptions } from '@tanstack/react-query';


export default function PromocionCreateForm() {
  const navigate = useNavigate();
  const createPromocion = useCreatePromocion();

  const form = useForm({
    validate: zodResolver(PromocionCreateDto),
    mode: 'uncontrolled',
    initialValues: {
      descripcion: '',
      codigoDescuento: '',
      porcentajeDescuento: 0.1,
      fechaInicio: new Date(),
      fechaFin: new Date(),
    },
  });

  const handleSubmit = form.onSubmit((values) => {
    createPromocion.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: 'Éxito',
            message: 'Promocion creada correctamente',
          });
          navigate(paths.dashboard.management.promociones.list);
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

      <TextInput label="Descripción" {...form.getInputProps('descripcion')} />
      <TextInput label="Código" {...form.getInputProps('codigoDescuento')} />

      <DateInput
        label="Válida desde"
        placeholder="Seleccione la fecha"
        valueFormat="YYYY-MM-DD"
        {...form.getInputProps('fechaInicio')}
      />
            <DateInput
        label="Válida hasta"
        placeholder="Seleccione la fecha"
        valueFormat="YYYY-MM-DD"
        {...form.getInputProps('fechaFin')}
      />

      <NumberInput label="Porcentaje de descuento" mih={0} max={1} step={0.01} {...form.getInputProps('porcentajeDescuento')} />

      <Group position="right" mt="md">
        <Button variant="outline" component={NavLink} to={paths.dashboard.management.promociones.list}>
          Cancelar
        </Button>
        <Button type="submit" loading={createPromocion.isPending}>
          Crear
        </Button>
      </Group>
    </Stack>
  );
}
