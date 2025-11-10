import { useEffect, useMemo } from "react"
import { Text } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetEmpleados, useDeleteEmpleado, useAuth } from "@/hooks"
import { paths } from "@/routes"
import { Link, NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"
import { notifications } from "@mantine/notifications"

export function EmpleadosTable() {
  const { roles } = useAuth();
  const isJefe = roles.includes('JEFE');
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "apellido"
    }
  })

  const { data, isLoading } = useGetEmpleados({
    query: {
      page,
      size,
      // status: tabs.value as Empleado['status'],
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

  const deleteMutation = useDeleteEmpleado()

  const columns = useMemo(
    () => [
      {
        accessor: "tipoDocumento",
        title: "Tipo Documento",
        sortable: true,
        render: empleado => (
          <Text truncate="end">{empleado.tipoDocumento}</Text>
        )
      },
      {
        accessor: "numeroDocumento",
        title: "N Documento",
        sortable: true,
        render: empleado => (
          <Text truncate="end">{empleado.numeroDocumento}</Text>
        )
      },
      {
        accessor: "apellido",
        title: "Nombre completo",
        sortable: true,
        render: empleado => (
          <Text truncate="end">{empleado.nombre + ' ' + empleado.apellido}</Text>
        )
      },
      {
        accessor: "tipoEmpleado",
        title: "Tipo Empleado",
        sortable: true,
        render: empleado => (
          <Text truncate="end">{empleado.tipoEmpleado}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: empleado => (
          <DataTable.Actions
            onView={ () => navigate(paths.dashboard.management.empleados.view(empleado.id)) }
            onEdit={ () => navigate(paths.dashboard.management.empleados.edit(empleado.id)) }
            onDelete={ () => handleDelete(empleado)}
          />
        ),
      }
    ],
    []
  )

  const handleDelete = empleado => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el empleado?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: empleado,
          route: { id: empleado.id }
        }, {
          onSuccess: () => {
            notifications.show({
              title: 'Borrado',
              message: 'El empleado fue borrado con éxito',
              color: 'green',
            });
          },
          onFailure: (error) => {
            notifications.show({
              title: 'Error',
              message: error.message || 'No se pudo borrar el empleado',
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
        title="Empleados"
        description="Lista de empleados"
        actions={
          <AddButton
            variant="default"
            size="xs"
            component={NavLink}
            to={paths.dashboard.management.empleados.add}
          >
            Agregar empleado
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("empleado")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("empleados")}
          paginationText={DataTable.paginationText("empleados")}
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
