import { useEffect, useMemo } from "react"
import { Text } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetAutores, useDeleteAutor } from "@/hooks"
import { paths } from "@/routes"
import { NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"

export function AutoresTable() {
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "apellido"
    }
  })

  const { data, isLoading } = useGetAutores({
    query: {
      page,
      size,
      // status: tabs.value as Autor['status'],
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

  const deleteMutation = useDeleteAutor()

  const columns = useMemo(
    () => [
      {
        accessor: "apellido",
        title: "Apellido",
        sortable: true,
        render: autor => <Text truncate="end">{autor.apellido}</Text>
      },
      {
        accessor: "nombre",
        title: "Nombre",
        sortable: true,
        render: autor => <Text truncate="end">{autor.nombre}</Text>
      },
      {
        accessor: "biografia",
        title: "Biografia",
        sortable: false,
        render: autor => <Text truncate="end">{autor.biografia}</Text>
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: autor => (
          <DataTable.Actions
            onView={() => {
              navigate(paths.dashboard.management.autores.view(autor.id))
            }}
            onEdit={() => {
              navigate(paths.dashboard.management.autores.edit(autor.id))
            }}
            onDelete={() => handleDelete(autor)}
          />
        )
      }
    ],
    []
  )

  const handleDelete = autor => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el autor?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: autor,
          route: { id: autor.id }
        })
      }
    })
  }

  return (
    <DataTable.Container>
      <DataTable.Title
        title="Autores"
        description="Lista de autores"
        actions={
          <AddButton
            variant="default"
            size="xs"
            component={NavLink}
            to={paths.dashboard.management.autores.add}
          >
            Agregar autor
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("autor")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("autores")}
          paginationText={DataTable.paginationText("autores")}
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
