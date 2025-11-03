import { useEffect, useMemo } from "react"
import { Text } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetLibros, useDeleteLibro } from "@/hooks"
import { paths } from "@/routes"
import { NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"

export function LibrosTable() {
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "titulo"
    }
  })

  const { data, isLoading } = useGetLibros({
    query: {
      page,
      size,
      // status: tabs.value as Libro['status'],
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

  const deleteMutation = useDeleteLibro()

  const columns = useMemo(
    () => [
      {
        accessor: "titulo",
        title: "Título",
        sortable: true,
        render: autor => <Text truncate="end">{autor.titulo}</Text>
      },
      {
        accessor: "fecha",
        title: "Año",
        sortable: true,
        render: autor => <Text truncate="end">{autor.fecha}</Text>
      },
      {
        accessor: "genero",
        title: "Género",
        sortable: true,
        render: autor => <Text truncate="end">{autor.genero}</Text>
      },
      {
        accessor: "paginas",
        title: "Páginas",
        sortable: true,
        render: autor => <Text truncate="end">{autor.paginas}</Text>
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: autor => (
          <DataTable.Actions
            onView={() => {
              navigate(paths.dashboard.management.libros.view(autor.id))
            }}
            onEdit={() => {
              navigate(paths.dashboard.management.libros.edit(autor.id))
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
        title="Libros"
        description="Lista de libros"
        actions={
          <AddButton
            variant="default"
            size="xs"
            component={NavLink}
            to={paths.dashboard.management.libros.add}
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
          recordsPerPageLabel={DataTable.recordsPerPageLabel("libros")}
          paginationText={DataTable.paginationText("libros")}
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
