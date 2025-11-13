import { useEffect, useState } from "react";
import {
  Container,
  SimpleGrid,
  Card,
  CardSection,
  Image,
  Text,
  Group,
  Stack,
  Title,
  Pagination,
  Loader,
} from "@mantine/core";
import { useAuth, useGetModelosDisponibles } from "@/hooks";
import { usePagination } from "@/api/helpers";
import { PiCar, PiSeatDuotone } from "react-icons/pi";
import dayjs from "dayjs";
import { formatCurrency } from "@/utilities/number";
import { DatePickerInput } from "@mantine/dates";
import { Link } from "react-router-dom";
import { paths } from "@/routes";
import { app } from "@/config";

export function ModelosList() {
  const { page, size, setPage, setSize } = usePagination();

  const [fechaDesde, setFechaDesde] = useState(new Date());
  const [fechaHasta, setFechaHasta] = useState(dayjs().add(4, "day").toDate());

  const fechaDesdeFormatted = dayjs(fechaDesde).format("YYYY-MM-DD");
  const fechaHastaFormatted = dayjs(fechaHasta).format("YYYY-MM-DD");

  const { data, isLoading } = useGetModelosDisponibles({
    query: {
      page,
      size,
      fechaDesde: fechaDesde ? fechaDesdeFormatted : undefined,
      fechaHasta: fechaHasta ? fechaHastaFormatted : undefined,
    }
  });

  useEffect(() => {
    const totalPages = data?.meta?.totalPages;
    if (totalPages != null && page >= totalPages) {
      setPage(Math.max(page - 1, 0));
    }
  }, [data, page, setPage]);

  if (isLoading) return <Loader />;

  const cars = data?.data ?? [];
  const meta = data?.meta ?? {};

  return (
    <Container size="lg" py="xl">
      <Group mb="xl">
        <DatePickerInput
          label="Desde"
          value={fechaDesde}
          onChange={setFechaDesde}
        />
        <DatePickerInput
          label="Hasta"
          value={fechaHasta}
          onChange={setFechaHasta}
          minDate={fechaDesde ?? undefined}
        />
      </Group>

      <SimpleGrid
        cols={{ base: 1, sm: 2, md: 3 }}
        spacing="lg"
      >
        {cars.map((car) => (
          <Card
            key={car.id}
            shadow="sm"
            radius="md"
            withBorder
            component={Link}
            to={paths.dashboard.management.alquileres.add.model(car.id) + `?fechaDesde=${fechaDesdeFormatted}&fechaHasta=${fechaHastaFormatted}`}
            >
            <CardSection>
              <Image
                src={`${app.apiBaseUrl}/vehiculos/${car.caracteristicaAutoId}/imagen`}
                alt={`${car.marca} ${car.modelo}`}
                height={160}
                fit="cover"
              />
            </CardSection>

            <Stack gap="xs" mt="sm">
              <Group justify="space-between">
                <Title order={4}>{`${car.marca} ${car.modelo}`}</Title>
                <Text size="sm" c="dimmed">{car.anio}</Text>
              </Group>

              <Group gap="xs">
                <Group gap={4}>
                  <PiCar size={18} />
                  <Text size="sm">{car.cantidadPuertas}</Text>
                </Group>
                <Group gap={4}>
                  <PiSeatDuotone size={18} />
                  <Text size="sm">{car.cantidadAsientos}</Text>
                </Group>
              </Group>

              <Group justify="space-between" mt="sm">
                <Stack gap={0}>
                  <Text size="sm" c="dimmed">Precio por día</Text>
                  <Text fw={600}>{formatCurrency(car.precioPorDia)}</Text>
                </Stack>
                <Stack gap={0}>
                  <Text size="sm" c="dimmed">Precio total</Text>
                  <Text fw={700} c="blue">
                    {formatCurrency(car.precioTotal)}
                  </Text>
                </Stack>
              </Group>
            </Stack>
          </Card>
        ))}
      </SimpleGrid>

      <Group justify="center" mt="xl">
        <Pagination
          total={meta.totalPages ?? 1}
          value={(meta.number ?? 0) + 1}
          onChange={(p) => setPage(p - 1)}
        />
      </Group>
    </Container>
  );
}
