import { useEffect, useMemo } from "react"
import { Group, Text } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetCostos, useDeleteCosto, useAuth } from "@/hooks"
import { paths } from "@/routes"
import { Link, NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"
import { notifications } from "@mantine/notifications"
import { useDisclosure } from "@mantine/hooks"
import { openCreateCostoModal } from "./add-costo-modal"

export function ModeloCostosTable({ modeloId }) {
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

  const { data, isLoading } = useGetCostos({
    query: {
      page,
      size,
      // status: tabs.value as Costo['status'],
      sort: sort.query,
      modeloId: modeloId
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

  const deleteMutation = useDeleteCosto()

  const columns = useMemo(
    () => [
      {
        accessor: "fechaDesde",
        title: "Desde",
        sortable: true,
        render: costo => (
          <Text truncate="end">{costo.fechaDesde}</Text>
        )
      },
      {
        accessor: "fechaHasta",
        title: "Hasta",
        sortable: true,
        render: costo => (
          <Text truncate="end">{costo.fechaHasta}</Text>
        )
      },
      {
        accessor: "costoTotal",
        title: "Precio",
        sortable: true,
        render: costo => (
          <Text truncate="end">{costo.costoTotal}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: costo => (
          <DataTable.Actions
            onView={() => navigate(paths.dashboard.management.costos.view(costo.id))}
            onEdit={() => navigate(paths.dashboard.management.costos.edit(costo.id))}
            onDelete={() => handleDelete(costo)}
          />
        ),
      }
    ],
    []
  )

  const handleDelete = costo => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el costo?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: costo,
          route: { id: costo.id }
        }, {
          onSuccess: () => {
            notifications.show({
              title: 'Borrado',
              message: 'El costo fue borrado con éxito',
              color: 'green',
            });
          },
          onFailure: (error) => {
            notifications.show({
              title: 'Error',
              message: error.message || 'No se pudo borrar este precio',
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
        title="Costos"
        description="Lista de costos"
        actions={
          <AddButton
            variant="default"
            size="xs"
            onClick={() => openCreateCostoModal(modeloId)}
          >
            Agregar precio
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("costo")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("costos")}
          paginationText={DataTable.paginationText("costos")}
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
