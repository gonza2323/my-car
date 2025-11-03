import { useEffect, useMemo } from "react"
import { Text } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetLocalidades, useDeleteLocalidad } from "@/hooks"
import { paths } from "@/routes"
import { NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"

export function LocalidadesTable() {
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "denominacion"
    }
  })

  const { data, isLoading } = useGetLocalidades({
    query: {
      page,
      size,
      // status: tabs.value as Localidad['status'],
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

  const deleteMutation = useDeleteLocalidad()

  const columns = useMemo(
    () => [
      {
        accessor: "denominacion",
        title: "Nombre",
        sortable: true,
        render: localidad => (
          <Text truncate="end">{localidad.denominacion}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: localidad => (
          <DataTable.Actions
            onView={() => {
              navigate(
                paths.dashboard.management.localidades.view(localidad.id)
              )
            }}
            onEdit={() => {
              navigate(
                paths.dashboard.management.localidades.edit(localidad.id)
              )
            }}
            onDelete={() => handleDelete(localidad)}
          />
        )
      }
    ],
    []
  )

  const handleDelete = localidad => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el localidad?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: localidad,
          route: { id: localidad.id }
        })
      }
    })
  }

  return (
    <DataTable.Container>
      <DataTable.Title
        title="Localidades"
        description="Lista de localidades"
        actions={
          <AddButton
            variant="default"
            size="xs"
            component={NavLink}
            to={paths.dashboard.management.localidades.add}
          >
            Agregar localidad
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("localidad")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("localidades")}
          paginationText={DataTable.paginationText("localidades")}
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
