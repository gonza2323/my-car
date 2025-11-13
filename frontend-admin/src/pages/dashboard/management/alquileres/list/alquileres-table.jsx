import { useEffect, useMemo, useState } from "react";
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { Button, Group, Text } from "@mantine/core";
import { modals } from "@mantine/modals";
import { notifications } from "@mantine/notifications";
import { usePagination } from "@/api/helpers";
import { AddButton } from "@/components/add-button";
import { DataTable } from "@/components/data-table";
import { useAuth, useDeleteAlquiler, useGetAlquileres } from "@/hooks";
import { paths } from "@/routes";
import { client } from "@/api/axios";
import { formatCurrency } from "@/utilities/number";
import dayjs from "dayjs";
import minMax from "dayjs/plugin/minMax";
import { DateInput, DatePickerInput } from "@mantine/dates";
import { app } from "@/config";
dayjs.extend(minMax);


export function AlquileresTable() {
  const { roles } = useAuth();
  const isJefe = roles.includes('JEFE');
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "fechaDesde"
    }
  })

  const [fechaDesde, setFechaDesde] = useState(new Date());
  const [fechaHasta, setFechaHasta] = useState(dayjs().add(4, "day").toDate());

  const fechaDesdeFormatted = dayjs(fechaDesde).format("YYYY-MM-DD");
  const fechaHastaFormatted = dayjs(fechaHasta).format("YYYY-MM-DD");

  const { data, isLoading } = useGetAlquileres({
    query: {
      page,
      size,
      // status: tabs.value as Alquiler['status'],
      sort: sort.query,
      fechaDesde: fechaDesde,
      fechaHasta: fechaHasta
    }
  })

  useEffect(() => {
    const totalPages = data?.meta?.totalPages
    if (totalPages != null && page != null) {
      if (page >= totalPages) {
        setPage(Math.max(page - 1, 0))
      }
    }
  }, [data, page, setPage])

  const deleteMutation = useDeleteAlquiler()

  const columns = useMemo(
    () => [
      {
        accessor: "fechaDesde",
        title: "Desde",
        sortable: true,
        render: alquiler => (
          <Text truncate="end">{alquiler.fechaDesde}</Text>
        )
      },
      {
        accessor: "fechaHasta",
        title: "Hasta",
        sortable: true,
        render: alquiler => (
          <Text truncate="end">{alquiler.fechaHasta}</Text>
        )
      },
      {
        accessor: "subtotal",
        title: "Total",
        sortable: true,
        render: alquiler => (
          <Text truncate="end">{formatCurrency(alquiler.subtotal)}</Text>
        )
      },
      {
        accessor: "cliente.apellido",
        title: "Total",
        sortable: true,
        render: alquiler => (
          <Text truncate="end">{alquiler.cliente.nombre + ' ' + alquiler.cliente.apellido}</Text>
        )
      },
      {
        accessor: "vehiculo.marca",
        title: "Total",
        sortable: true,
        render: alquiler => (
          <Text truncate="end">{alquiler.vehiculo.marca + ' ' + alquiler.vehiculo.modelo}</Text>
        )
      },
      {
        accessor: "cantidadDescuento",
        title: "Descuento",
        sortable: true,
        render: alquiler => (
          alquiler.cantidadDescuento ? (
            <Text truncate="end">
              {formatCurrency(alquiler.cantidadDescuento)} ({alquiler.porcentajeDescuento * 100}%)
            </Text>
          ) : (
            <Text>Sin Descuento</Text>
          )
        )
      },
      {
        accessor: "total",
        title: "Total",
        sortable: true,
        render: alquiler => (
          <Text truncate="end">{formatCurrency(alquiler.total)}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: alquiler => (
          <DataTable.Actions
            onView={() => navigate(paths.dashboard.management.alquileres.view(alquiler.id))}
            onEdit={() => navigate(paths.dashboard.management.alquileres.edit(alquiler.id))}
            onDelete={() => handleDelete(alquiler)}
          />
        ),
      }
    ],
    []
  )

  const handleDelete = alquiler => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el alquiler?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: alquiler,
          route: { id: alquiler.id }
        }, {
          onSuccess: () => {
            notifications.show({
              title: 'Borrado',
              message: 'El alquiler fue borrado con éxito',
              color: 'green',
            });
          },
          onFailure: (error) => {
            notifications.show({
              title: 'Error',
              message: error.message || 'No se pudo borrar el alquiler',
              color: 'red',
            });
          }
        })
      }
    })
  }

  const handleDownloadReport = async (formato) => {
    try {
      const response = await client.get(`${app.apiBaseUrl}/reportes/recaudacion/${formato}?fechaInicio=${fechaDesdeFormatted}&fechaFin=${fechaHastaFormatted}`, {
        responseType: "blob",
        headers: { Accept: `application/${formato}` },
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `alquileres_autos.${formato}`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error(`Error al generar ${formato}:`, error);
      alert("No se pudo generar el reporte de alquileres");
    }
  }

  return (
    <DataTable.Container>
      <DataTable.Title
        title="Alquileres"
        description="Lista de alquileres"
        actions={
          <Group>
            <Group mb="xl">
              <DateInput
                label="Desde"
                valueFormat="YYYY-MM-DD"
                value={fechaDesde}
                onChange={setFechaDesde}
                maxDate={fechaHasta ?? undefined}
              />
              <DateInput
                label="Hasta"
                valueFormat="YYYY-MM-DD"
                value={fechaHasta}
                onChange={setFechaHasta}
                minDate={fechaDesde ?? undefined}
              />
            </Group>
            <Button onClick={() => handleDownloadReport('pdf')}>PDF</Button>
            <Button onClick={() => handleDownloadReport('xlsx')}>Excel</Button>
            <AddButton
              variant="default"
              size="xs"
              component={NavLink}
              to={paths.dashboard.management.alquileres.add.root}
            >
              Agregar alquiler
            </AddButton>
          </Group>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("alquiler")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("alquileres")}
          paginationText={DataTable.paginationText("alquileres")}
          page={page + 1}
          records={data?.data ?? []}
          fetching={isLoading}
          onPageChange={pageNo => setPage(pageNo - 1)}
          recordsPerPage={size}
          totalRecords={data?.meta.totalElements ?? 0}
          onRecordsPerPageChange={setSize}
          recordsPerPageOptions={[5, 15, 30]}
          sortStatus={sort.status}
          onSortStatusChange={sort.change}
          columns={columns}
        />
      </DataTable.Content>
    </DataTable.Container>
  );
}
