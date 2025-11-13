import React from "react";
import { useEffect, useState } from "react";
import AlquilerItem from "../components/Delivery/DeliveryItem/AlquilerItem";
import Skeleton from "react-loading-skeleton";
import { toast } from "react-toastify";
import { useAuth } from "@/hooks";
import { useGetAlquileres } from "@/hooks/api/alquileres";
import { Container, Loader, Stack, Text, Title } from "@mantine/core";


const AlquileresList = () => {

  const { data, isLoading } = useGetAlquileres();

  const alquileres = data?.data ?? [];
  const meta = data?.meta ?? {};

  console.log(alquileres)

  if (isLoading) {
    return <Loader />
  }

  if (alquileres.length === 0) {
    return <Text>No tiene alquileres</Text>
  }

  return (
    <Container py="lg">
      <Stack>
      <Title>Mis alquileres</Title>
        {(alquileres.length > 0 &&
          alquileres.map((alquiler) => {
            return (
              <AlquilerItem key={alquiler.id} alquiler={alquiler}></AlquilerItem>
            );
          })) || <Skeleton height={500}></Skeleton>}
      </Stack>
    </Container>
  );
};

export default AlquileresList;
