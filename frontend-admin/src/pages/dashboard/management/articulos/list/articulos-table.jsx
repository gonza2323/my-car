import { useMemo } from "react"
import { Text } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetArticulos, useDeleteArticulo } from "@/hooks"
import { paths } from "@/routes"
import { NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"
import { formatCurrency } from "@/utilities/number"

export function ArticulosTable() {
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "nombre"
    }
  })

  const { data, isLoading } = useGetArticulos({
    query: {
      page,
      size,
      // status: tabs.value as Articulo['status'],
      sort: sort.query
    }
  })

  const deleteMutation = useDeleteArticulo({
    route: {}
  })

  const columns = useMemo(
    () => [
      {
        accessor: "nombre",
        title: "Nombre",
        sortable: true,
        render: articulo => <Text truncate="end">{articulo.nombre}</Text>
      },
      {
        accessor: "proveedorNombre",
        title: "Proveedor",
        sortable: false,
        render: articulo => (
          <Text truncate="end">{articulo.proveedorNombre}</Text>
        )
      },
      {
        accessor: "precio",
        title: "Precio",
        sortable: true,
        render: articulo => (
          <Text truncate="end">{formatCurrency(articulo.precio)}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: articulo => (
          <DataTable.Actions
            onView={() => {
              navigate(paths.dashboard.management.articulos.view(articulo.id))
            }}
            onEdit={() => {
              navigate(paths.dashboard.management.articulos.edit(articulo.id))
            }}
            onDelete={() => handleDelete(articulo)}
          />
        )
      }
    ],
    []
  )

  const handleDelete = articulo => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el articulo?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: articulo,
          route: { id: articulo.id }
        })
      }
    })
  }

  return (
    <DataTable.Container>
      <DataTable.Title
        title="Articulos"
        description="Lista de articulos"
        actions={
          <AddButton
            variant="default"
            size="xs"
            component={NavLink}
            to={paths.dashboard.management.articulos.add}
          >
            Agregar articulo
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("articulo")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("articulos")}
          paginationText={DataTable.paginationText("articulos")}
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
