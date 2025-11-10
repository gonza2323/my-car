import { useEffect, useMemo } from "react"
import { Text } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetVehiculos, useDeleteVehiculo, useAuth } from "@/hooks"
import { paths } from "@/routes"
import { Link, NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"
import { notifications } from "@mantine/notifications"

export function VehiculosTable() {
  const { roles } = useAuth();
  const isJefe = roles.includes('JEFE');
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "patente"
    }
  })

  const { data, isLoading } = useGetVehiculos({
    query: {
      page,
      size,
      // status: tabs.value as Vehiculo['status'],
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

  const deleteMutation = useDeleteVehiculo()

  const columns = useMemo(
    () => [
      {
        accessor: "patente",
        title: "Patente",
        sortable: true,
        render: vehiculo => (
          <Text truncate="end">{vehiculo.patente}</Text>
        )
      },
      {
        accessor: "marca",
        title: "Marca",
        sortable: false,
        render: vehiculo => (
          <Text truncate="end">{vehiculo.marca}</Text>
        )
      },
      {
        accessor: "modelo",
        title: "Modelo",
        sortable: false,
        render: vehiculo => (
          <Text truncate="end">{vehiculo.modelo}</Text>
        )
      },
      {
        accessor: "anio",
        title: "Año",
        sortable: false,
        render: vehiculo => (
          <Text truncate="end">{vehiculo.anio}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: vehiculo => (
          <DataTable.Actions
            onView={() => navigate(paths.dashboard.management.vehiculos.view(vehiculo.id))}
            onEdit={() => navigate(paths.dashboard.management.vehiculos.edit(vehiculo.id))}
            onDelete={() => handleDelete(vehiculo)}
          />
        ),
      }
    ],
    []
  )

  const handleDelete = vehiculo => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el vehiculo?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: vehiculo,
          route: { id: vehiculo.id }
        }, {
          onSuccess: () => {
            notifications.show({
              title: 'Borrado',
              message: 'El vehiculo fue borrado con éxito',
              color: 'green',
            });
          },
          onFailure: (error) => {
            notifications.show({
              title: 'Error',
              message: error.message || 'No se pudo borrar el vehiculo',
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
        title="Vehiculos"
        description="Lista de vehiculos"
        actions={
          <AddButton
            variant="default"
            size="xs"
            component={NavLink}
            to={paths.dashboard.management.vehiculos.add}
          >
            Agregar vehiculo
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("vehiculo")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("vehiculos")}
          paginationText={DataTable.paginationText("vehiculos")}
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
  )
}
