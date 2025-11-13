import { useEffect, useMemo } from "react";
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
import { ReporteRecaudacionButton } from "@/components/ReporteRecaudacionButton";
import { ReporteVehiculosButton } from "@/components/ReporteVehiculosButton";
import dayjs from "dayjs";
import minMax from "dayjs/plugin/minMax";
dayjs.extend(minMax);
import BotonFactura from "@/components/BotonFactura.tsx";

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

  const { data, isLoading } = useGetAlquileres({
    query: {
      page,
      size,
      // status: tabs.value as Alquiler['status'],
      sort: sort.query
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
        accessor: "monto",
        title: "Subtotal",
        sortable: true,
        render: alquiler => (
          <Text truncate="end">{formatCurrency(alquiler.monto)}</Text>
        )
      },
      {
        accessor: "factura",
        title: "Factura",
        textAlign: "center",
        width: 160,
        render: alquiler => (
          <BotonFactura alquilerId={alquiler.id} disabled={alquiler.estado !== "PAGADO"} />
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
  const handleDownloadPdf = async () => {
    try {
      const response = await client.get("http://localhost:8080/api/v1/auth/alquileres", {
        responseType: "blob",
        headers: { Accept: "application/pdf" },
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "alquileres_autos.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error al generar PDF:", error);
      alert("No se pudo generar el reporte de alquileres");
    }

  }
  //Calcular fecha mínima y máxima de los alquileres actuales
  const alquileres = Array.isArray(data?.data) ? data.data : [];

  let fechaMin = null;
  let fechaMax = null;

  try {
    if (alquileres.length > 0) {
      const fechasValidas = alquileres
        .filter(a => a.fechaDesde && a.fechaHasta)
        .map(a => ({
          desde: dayjs(a.fechaDesde),
          hasta: dayjs(a.fechaHasta),
        }));

      if (fechasValidas.length > 0) {
        const desdeArray = fechasValidas.map(f => f.desde.toDate());
        const hastaArray = fechasValidas.map(f => f.hasta.toDate());

        fechaMin = dayjs(Math.min(...desdeArray)).format("YYYY-MM-DD");
        fechaMax = dayjs(Math.max(...hastaArray)).format("YYYY-MM-DD");
      }
    }
  } catch (e) {
    console.error("Error calculando fechas:", e);
  }

  return (
    <DataTable.Container>
      <DataTable.Title
        title="Alquileres"
        description="Lista de alquileres"
        actions={
          <Group>
            <ReporteRecaudacionButton fechaMin={fechaMin ?? ""} fechaMax={fechaMax ?? ""} />
              <ReporteVehiculosButton fechaMin={fechaMin ?? ""} fechaMax={fechaMax ?? ""} />
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
