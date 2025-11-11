import { useEffect, useMemo } from "react";
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { Button, Group, Text } from "@mantine/core";
import { modals } from "@mantine/modals";
import { notifications } from "@mantine/notifications";
import { usePagination } from "@/api/helpers";
import { AddButton } from "@/components/add-button";
import { DataTable } from "@/components/data-table";
import { useAuth, useDeletePromocion, useGetPromociones } from "@/hooks";
import { paths } from "@/routes";
import { client } from "@/api/axios";


export function PromocionesTable() {
  const { roles } = useAuth();
  const isJefe = roles.includes('JEFE');
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "fechaInicio"
    }
  })

  const { data, isLoading } = useGetPromociones({
    query: {
      page,
      size,
      // status: tabs.value as Promocion['status'],
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

  const deleteMutation = useDeletePromocion()

  const columns = useMemo(
    () => [

      {
        accessor: "descripcion",
        title: "Descripción",
        sortable: true,
        render: promocion => (
          <Text truncate="end">{promocion.descripcion}</Text>
        )
      },
      {
        accessor: "fechaInicio",
        title: "Desde",
        sortable: true,
        render: promocion => (
          <Text truncate="end">{promocion.fechaInicio}</Text>
        )
      },
      {
        accessor: "fechaFin",
        title: "Hasta",
        sortable: true,
        render: promocion => (
          <Text truncate="end">{promocion.fechaFin}</Text>
        )
      },
      {
        accessor: "codigoDescuento",
        title: "Código",
        sortable: true,
        render: promocion => (
          <Text truncate="end">{promocion.codigoDescuento}</Text>
        )
      },
      {
        accessor: "porcentajeDescuento",
        title: "Descuento",
        sortable: true,
        render: promocion => (
          <Text truncate="end">{promocion.porcentajeDescuento}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: promocion => (
          <DataTable.Actions
            onView={() => navigate(paths.dashboard.management.promociones.view(promocion.id))}
            onEdit={() => navigate(paths.dashboard.management.promociones.edit(promocion.id))}
            onDelete={() => handleDelete(promocion)}
          />
        ),
      }
    ],
    []
  )

  const handleDelete = promocion => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el promocion?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: promocion,
          route: { id: promocion.id }
        }, {
          onSuccess: () => {
            notifications.show({
              title: 'Borrado',
              message: 'El promocion fue borrado con éxito',
              color: 'green',
            });
          },
          onFailure: (error) => {
            notifications.show({
              title: 'Error',
              message: error.message || 'No se pudo borrar el promocion',
              color: 'red',
            });
          }
        })
      }
    })
  }

  return (
    <DataTable.Container>
      <DataTable.Title
        title="Promociones"
        description="Lista de promociones"
        actions={
            <AddButton
              variant="default"
              size="xs"
              component={NavLink}
              to={paths.dashboard.management.promociones.add}
            >
              Agregar promocion
            </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("promocion")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("promociones")}
          paginationText={DataTable.paginationText("promociones")}
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
