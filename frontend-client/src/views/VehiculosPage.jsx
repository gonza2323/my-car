import React from "react";
import { useEffect, useState } from "react";
import AlquilerItem from "../components/Delivery/DeliveryItem/AlquilerItem";
import Skeleton from "react-loading-skeleton";
import { toast } from "react-toastify";
import { useAuth, useGetModelosDisponibles } from "@/hooks";
import { useGetAlquileres } from "@/hooks/api/alquileres";
import { Card, CardSection, Container, Group, Image, Loader, Pagination, SimpleGrid, Stack, Text, Title } from "@mantine/core";
import { usePagination } from "@/api/helpers";
import dayjs from "dayjs";
import { DatePickerInput } from "@mantine/dates";
import { TbArmchair, TbCar } from "react-icons/tb";
import { formatCurrency } from "@/utilities/number";


const VehiculosPage = () => {

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
      <Title mb="lg">Vehículos disponibles</Title>
      
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
            >
            <CardSection>
              <Image
                src={`https://example.com/images/cars/${car.id}.jpg`}
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
                  <TbCar size={18} />
                  <Text size="sm">{car.cantidadPuertas}</Text>
                </Group>
                <Group gap={4}>
                  <TbArmchair size={18} />
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


export default VehiculosPage;