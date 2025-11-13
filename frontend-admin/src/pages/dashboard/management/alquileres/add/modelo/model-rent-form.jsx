import {
  Box, Button, Grid, Card, Group, Select, Stack, TextInput, Loader,
  CardSection, Title, Image, Text
} from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { NavLink, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { z } from 'zod';
import { paths } from '@/routes';
import {
  useGetClientes,
  useGetModeloDisponible,
  useRegistrarAlquiler,
  useVerificarCupon, // <- query hook
} from '@/hooks';
import { DateInput } from '@mantine/dates';
import { useState } from 'react';
import { PiDoor, PiSeatDuotone } from 'react-icons/pi';
import { formatCurrency } from '@/utilities/number';
import { RegistrarAlquilerDto } from '@/api/dtos/alquiler';
import { app } from '@/config';

export default function ModeloRentForm() {
  const navigate = useNavigate();
  const alquilarModelo = useRegistrarAlquiler();
  const { modelId } = useParams();
  const [searchParams] = useSearchParams();

  const fechaDesde = searchParams.get("fechaDesde") || new Date().toISOString().split('T')[0];
  const fechaHasta = searchParams.get("fechaHasta") ||
    new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

  const [discount, setDiscount] = useState(null);

  const form = useForm({
    validate: zodResolver(RegistrarAlquilerDto),
    mode: 'uncontrolled',
    initialValues: {
      caracteristicaAutoId: modelId,
      clienteId: '',
      fechaDesde: new Date(fechaDesde),
      fechaHasta: new Date(fechaHasta),
      formaDePago: 'EFECTIVO',
      codigoDescuento: '',
    },
  });

  const {
    data: modelo,
    isLoading: modeloLoading,
  } = useGetModeloDisponible({
    route: { id: modelId },
    query: { fechaDesde, fechaHasta },
  });

  const {
    data: clientesResponse,
    isLoading: clientesLoading,
  } = useGetClientes({ query: { size: 99999, sort: 'apellido%2Casc' } });

  const clientes = clientesResponse?.data ?? [];
  const clientesOptions = clientes.map((m) => ({
    value: String(m.id),
    label: `${m.nombre} ${m.apellido} (${m.numeroDocumento})`,
  }));

  // coupon verification query — disabled until manually triggered
  const verificarCupon = useVerificarCupon({
    query: { codigo: form.getValues().codigoDescuento.trim() },
    enabled: false,
  });

  const handleVerifyCoupon = async () => {
    const codigo = form.getValues().codigoDescuento.trim();
    if (!codigo) {
      notifications.show({
        title: 'Atención',
        message: 'Ingrese un código de descuento antes de verificar.',
        color: 'yellow',
      });
      return;
    }

    const { data } = await verificarCupon.refetch();
    console.log(data)

    if (!data) {
      notifications.show({
        title: 'Error',
        message: 'No se pudo verificar el cupón.',
        color: 'red',
      });
      return;
    }

    if (data.valid) {
      setDiscount(data.discount);
      notifications.show({
        title: 'Cupón válido',
        message: `Descuento del ${data.discount}% aplicado.`,
        color: 'green',
      });
    } else {
      setDiscount(null);
      notifications.show({
        title: 'Cupón inválido',
        message: data.reason || 'El código ingresado no es válido.',
        color: 'red',
      });
      form.setFieldValue('codigoDescuento', '');
    }
  };

  const handleSubmit = form.onSubmit((values) => {
    alquilarModelo.mutate(
      { variables: values },
      {
        onSuccess: () => {
          notifications.show({
            title: 'Éxito',
            message: 'Se ha alquilado el vehículo exitosamente',
          });
          navigate(paths.dashboard.management.alquileres.list);
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

  console.log(modelo)

  const totalPrice = modelo?.precioTotal ?? 0;
  const discountedPrice = discount ? totalPrice * (1 - discount) : totalPrice;

  return (
    <Group grow align="start" gap="xl">
      <Stack component="form" onSubmit={handleSubmit}>
        <Group grow>
          <DateInput
            label="Desde"
            valueFormat="YYYY-MM-DD"
            readOnly
            maxDate={fechaHasta ?? undefined}
            {...form.getInputProps('fechaDesde')}
          />
          <DateInput
            label="Hasta"
            valueFormat="YYYY-MM-DD"
            readOnly
            minDate={fechaDesde ?? undefined}
            {...form.getInputProps('fechaHasta')}
          />
        </Group>

        <Select
          {...form.getInputProps('clienteId')}
          label="Cliente"
          searchable
          placeholder="Seleccione un cliente"
          data={clientesOptions}
          disabled={clientesLoading}
          rightSection={clientesLoading && <Loader size="sm" />}
          withCheckIcon={false}
          allowDeselect={false}
        />

        <Select
          label="Forma de pago"
          data={['EFECTIVO', 'TRANSFERENCIA', 'BILLETERA_VIRTUAL']}
          allowDeselect={false}
          checkIconPosition="right"
          {...form.getInputProps('formaDePago')}
        />

        <Group position="right" grow align="end">
          <TextInput
            key={form.key('codigoDescuento')}
            label="Código de descuento"
            {...form.getInputProps('codigoDescuento')}
          />
          <Button
            type="button"
            loading={verificarCupon.isFetching}
            onClick={handleVerifyCoupon}
          >
            Verificar cupón
          </Button>
        </Group>

        <Group position="right" mt="md" justify="end">
          <Button
            variant="outline"
            component={NavLink}
            to={paths.dashboard.management.alquileres.add.root}
          >
            Cancelar
          </Button>
          <Button type="submit" loading={alquilarModelo.isPending}>
            Alquilar
          </Button>
        </Group>
      </Stack>

      <Card shadow="sm" radius="md" withBorder>
        <CardSection>
          <Image
            src={`${app.apiBaseUrl}/vehiculos/${modelId}/imagen`}
            height={160}
            fit="cover"
          />
        </CardSection>

        {modeloLoading && <Loader />}

        {!modeloLoading && (
          <Stack gap="xs" mt="sm">
            <Group justify="space-between">
              <Title order={4}>{`${modelo.marca} ${modelo.modelo}`}</Title>
              <Text size="sm" c="dimmed">
                {modelo.anio}
              </Text>
            </Group>

            <Group gap="xs">
              <Group gap={4}>
                <PiDoor size={18} />
                <Text size="sm">{modelo.cantidadPuertas}</Text>
              </Group>
              <Group gap={4}>
                <PiSeatDuotone size={18} />
                <Text size="sm">{modelo.cantidadAsientos}</Text>
              </Group>
            </Group>

            <Group justify="space-between" mt="sm">
              <Stack gap={0}>
                <Text size="sm" c="dimmed">
                  Precio por día
                </Text>
                <Text fw={600}>{formatCurrency(modelo.precioPorDia)}</Text>
              </Stack>

              <Stack gap={0}>
                <Text size="sm" c="dimmed">
                  Precio total
                </Text>
                {discount ? (
                  <Group gap="xs">
                    <Text fw={500} td="line-through" c="dimmed">
                      {formatCurrency(totalPrice)}
                    </Text>
                    <Text fw={700} c="green">
                      {formatCurrency(discountedPrice)}
                    </Text>
                  </Group>
                ) : (
                  <Text fw={700} c="blue">
                    {formatCurrency(totalPrice)}
                  </Text>
                )}
              </Stack>
            </Group>
          </Stack>
        )}
      </Card>
    </Group>
  );
}
