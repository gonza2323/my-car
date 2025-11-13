import {
  Card,
  Image,
  Text,
  Group,
  Stack,
  Button,
  Badge,
  Title,
  Container,
  Loader,
  Skeleton,
  Divider,
  Tooltip,
  ActionIcon,
} from '@mantine/core';
import { TbUsers, TbArmchair, TbCalendar, TbDiscount, TbCurrencyDollar, TbCheck, TbDoor, TbInvoice } from "react-icons/tb";
import { useGetAlquileres } from '@/hooks';
import { app } from '@/config';
import { client } from '@/api/axios';

const getCarImageUrl = (carId) =>
  `https://example.com/api/cars/${carId}/image`; // adjust this

const AlquilerItem = ({ alquiler }) => {
  const {
    id,
    vehiculo,
    fechaDesde,
    fechaHasta,
    estado,
    costoPorDia,
    subtotal,
    porcentajeDescuento,
    cantidadDescuento,
    total
  } = alquiler

  const handleDownloadPdf = async () => {
    try {
      const response = await client.get(`${app.apiBaseUrl}/alquileres/${id}/factura`, {
        responseType: "blob",
        headers: { Accept: "application/pdf" },
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "factura.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error al generar PDF:", error);
      alert("No se pudo generar la factura");
    }
  }

  const handlePayButton = async () => {
    try {
      const response = await client.post(`${app.apiBaseUrl}/payments`, {
          alquilerId: id,
      });

      const data = response.data;

      if (data && data.urlDePago) {
        window.location.href = data.urlDePago;
      } else {
        console.error("urlDePago not found in response:", data);
        alert("No se pudo generar el link de pago");
      }

    } catch (error) {
      console.error("Error al generar PDF:", error);
      alert("No se pudo generar el link de pago");
    }
  }

  return (
    <Card
      shadow="sm"
      radius="lg"
      withBorder
      p="md"
      style={{
        display: 'flex',
        gap: '1rem',
        alignItems: 'stretch',
        overflow: 'hidden',
      }}
    >
      <Image
        src={getCarImageUrl(vehiculo.caracteristicaAutoId)}
        alt={`${vehiculo.marca} ${vehiculo.modelo}`}
        width="33%"
        radius="md"
        fit="cover"
      />

      <Stack justify="space-between" style={{ flex: 1 }}>
        {/* Top section */}
        <Group justify="space-between" align="flex-start">
          <Stack gap={2}>
            <Title order={4}>
              {vehiculo.marca} {vehiculo.modelo}
            </Title>
            <Text size="sm" c="dimmed">
              {vehiculo.anio}
            </Text>
          </Stack>
          {estado == 'PAGADO' ? (
            <Group>
              <Tooltip label="Factura">
                <ActionIcon
                  onClick={handleDownloadPdf}
                  variant="default"
                  >
                  <TbInvoice />
                </ActionIcon>
              </Tooltip>
              <Badge color="teal" leftSection={<TbCheck size={14} />}>
                Pagado
              </Badge>
            </Group>
          ) : (
            <Button variant="filled" onClick={handlePayButton}>
              Pagar
            </Button>
          )}
        </Group>

        {/* Dates */}
        <Group gap="xs" mt="xs">
          <TbCalendar size={16} />
          <Text size="sm">
            {new Date(fechaDesde).toLocaleDateString()} –{' '}
            {new Date(fechaHasta).toLocaleDateString()}
          </Text>
        </Group>

        {/* Info */}
        <Group gap="md" mt="xs">
          <Group gap={4}>
            <TbArmchair size={16} />
            <Text size="sm">{vehiculo.cantidadAsientos}</Text>
          </Group>
          <Group gap={4}>
            <TbDoor size={16} />
            <Text size="sm">{vehiculo.cantidadPuertas}</Text>
          </Group>
        </Group>

        <Divider my="xs" />

        {/* Price breakdown */}
        <Stack gap={2}>
          <Group justify="space-between">
            <Text size="sm" c="dimmed">
              Precio por día
            </Text>
            <Group gap={4}>
              <TbCurrencyDollar size={14} />
              <Text size="sm">{costoPorDia.toFixed(2)}</Text>
            </Group>
          </Group>

          {porcentajeDescuento ? (
            <>
              <Group justify="space-between">
                <Text size="sm" c="dimmed">
                  Subtotal
                </Text>
                <Text size="sm">{subtotal.toFixed(2)}</Text>
              </Group>
              <Group justify="space-between" c="green">
                <Group gap={4}>
                  <TbDiscount size={14} />
                  <Text size="sm">Descuento ({(porcentajeDescuento * 100).toFixed(2)}%)</Text>
                </Group>
                <Text size="sm">- {cantidadDescuento.toFixed(2)}</Text>
              </Group>
            </>
          ) : null}

          <Divider my={4} />

          <Group justify="space-between">
            <Text fw={600}>Total</Text>
            <Text fw={600}>{total.toFixed(2)}</Text>
          </Group>
        </Stack>
      </Stack>
    </Card>
  );
};

export default AlquilerItem;