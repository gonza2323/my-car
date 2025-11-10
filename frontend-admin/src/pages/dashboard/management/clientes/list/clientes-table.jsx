import { useEffect, useMemo } from "react"
import { ActionIcon, Text, Tooltip } from "@mantine/core"
import { usePagination } from "@/api/helpers"
import { AddButton } from "@/components/add-button"
import { DataTable } from "@/components/data-table"
import { useGetClientes, useDeleteCliente, useAuth } from "@/hooks"
import { paths } from "@/routes"
import { Link, NavLink, useNavigate } from "react-router-dom"
import { modals } from "@mantine/modals"
import { notifications } from "@mantine/notifications"
import { PiWhatsappLogoDuotone } from "react-icons/pi"

export function ClientesTable() {
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

  const { data, isLoading } = useGetClientes({
    query: {
      page,
      size,
      // status: tabs.value as Cliente['status'],
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

  const deleteMutation = useDeleteCliente()

  const columns = useMemo(
    () => [
      {
        accessor: "tipoDocumento",
        title: "Tipo Documento",
        sortable: true,
        render: cliente => (
          <Text truncate="end">{cliente.tipoDocumento}</Text>
        )
      },
      {
        accessor: "numeroDocumento",
        title: "N Documento",
        sortable: true,
        render: cliente => (
          <Text truncate="end">{cliente.numeroDocumento}</Text>
        )
      },
      {
        accessor: "apellido",
        title: "Nombre completo",
        sortable: true,
        render: cliente => (
          <Text truncate="end">{cliente.nombre + ' ' + cliente.apellido}</Text>
        )
      },
      {
        accessor: "email",
        title: "Correo",
        sortable: true,
        render: cliente => (
          <Text truncate="end">{cliente.usuarioEmail}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: cliente => (
          <DataTable.Actions
            onView={() => navigate(paths.dashboard.management.clientes.view(cliente.id))}
            onEdit={() => navigate(paths.dashboard.management.clientes.edit(cliente.id))}
            onDelete={() => handleDelete(cliente)}
          >
            <Tooltip label="WhatsApp">
              <ActionIcon
              component="a"
              href={`https://wa.me/${cliente.telefono}`}
              target="_blank"
              rel="noopener noreferrer"
              variant="default">
                <PiWhatsappLogoDuotone size="1rem" />
              </ActionIcon>
            </Tooltip>
          </DataTable.Actions>
        ),
      }
    ],
    []
  )

  const handleDelete = cliente => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el cliente?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: cliente,
          route: { id: cliente.id }
        }, {
          onSuccess: () => {
            notifications.show({
              title: 'Borrado',
              message: 'El cliente fue borrado con éxito',
              color: 'green',
            });
          },
          onFailure: (error) => {
            notifications.show({
              title: 'Error',
              message: error.message || 'No se pudo borrar el cliente',
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
        title="Clientes"
        description="Lista de clientes"
        actions={
          <AddButton
            variant="default"
            size="xs"
            component={NavLink}
            to={paths.dashboard.management.clientes.add}
          >
            Agregar cliente
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("cliente")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("clientes")}
          paginationText={DataTable.paginationText("clientes")}
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
