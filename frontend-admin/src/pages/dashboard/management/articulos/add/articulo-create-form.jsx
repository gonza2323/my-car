import {
  Box,
  Button,
  FileInput,
  Select,
  Group,
  NumberInput,
  TextInput
} from "@mantine/core"
import { paths } from "@/routes"
import { useForm } from "@mantine/form"
import { notifications } from "@mantine/notifications"
import { NavLink, useNavigate } from "react-router-dom"
import { client } from "@/api/axios"
import { useEffect, useState } from "react"

export default function ArticuloCreateForm() {
  const navigate = useNavigate()
  const [proveedores, setProveedores] = useState([])

  const form = useForm({
    initialValues: {
      nombre: "",
      precio: 0,
      proveedorId: "",
      imagen: null
    }
  })

  useEffect(() => {
    client
      .get("proveedores/all")
      .then(res => setProveedores(res.data))
      .catch(() => {
        notifications.show({
          title: "Error",
          message: "No se pudieron cargar los proveedores",
          color: "red"
        })
      })
  }, [])

  const handleSubmit = async values => {
    const formData = new FormData()
    formData.append(
      "articulo",
      new Blob(
        [
          JSON.stringify({
            nombre: values.nombre,
            precio: values.precio,
            proveedorId: values.proveedorId ? Number(values.proveedorId) : null
          })
        ],
        { type: "application/json" }
      )
    )
    if (values.imagen) {
      formData.append("imagen", values.imagen)
    }

    try {
      await client.post("articulos", formData, {
        headers: { "Content-Type": "multipart/form-data" }
      })
      notifications.show({
        title: "Éxito",
        message: "Artículo creado correctamente"
      })
      navigate(paths.dashboard.management.articulos.list)
    } catch (error) {
      notifications.show({
        title: "Error",
        message: "No se pudo crear el artículo",
        color: "red"
      })
    }
  }

  return (
    <Box component="form" onSubmit={form.onSubmit(handleSubmit)} maw={400}>
      <TextInput
        label="Nombre"
        placeholder="Ingrese el nombre"
        {...form.getInputProps("nombre")}
        required
      />

      <NumberInput
        label="Precio"
        placeholder="Ingrese el precio"
        {...form.getInputProps("precio")}
        required
        min={0}
        step={0.01}
      />

      <Select
        label="Proveedor"
        placeholder="Seleccione un proveedor"
        data={proveedores.map(p => ({
          value: p.id.toString(),
          label: p.nombre
        }))}
        {...form.getInputProps("proveedorId")}
        required
      />

      <FileInput
        label="Imagen"
        placeholder="Seleccione una imagen"
        accept="image/*"
        {...form.getInputProps("imagen")}
      />

      <Group justify="flex-end" mt="md">
        <Button
          variant="outline"
          component={NavLink}
          to={paths.dashboard.management.articulos.list}
        >
          Cancelar
        </Button>
        <Button type="submit">Crear</Button>
      </Group>
    </Box>
  )
}
