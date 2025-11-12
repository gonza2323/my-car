import { useEffect, useMemo } from "react";
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { Button, Group, Text } from "@mantine/core";
import { modals } from "@mantine/modals";
import { notifications } from "@mantine/notifications";
import { usePagination } from "@/api/helpers";
import { AddButton } from "@/components/add-button";
import { DataTable } from "@/components/data-table";
import { useAuth, useDeleteModelo, useGetModelos } from "@/hooks";
import { paths } from "@/routes";
import { client } from "@/api/axios";


export function ModelosTable() {
  const { roles } = useAuth();
  const isJefe = roles.includes('JEFE');
  const navigate = useNavigate()
  const { page, size, setSize: setSize, setPage } = usePagination()
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable({
    sortConfig: {
      direction: "asc",
      column: "marca"
    }
  })

  const { data, isLoading } = useGetModelos({
    query: {
      page,
      size,
      // status: tabs.value as Modelo['status'],
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

  const deleteMutation = useDeleteModelo()

  const columns = useMemo(
    () => [
      {
        accessor: "marca",
        title: "Marca",
        sortable: true,
        render: modelo => (
          <Text truncate="end">{modelo.marca}</Text>
        )
      },
      {
        accessor: "modelo",
        title: "Modelo",
        sortable: true,
        render: modelo => (
          <Text truncate="end">{modelo.modelo}</Text>
        )
      },
      {
        accessor: "anio",
        title: "Año",
        sortable: true,
        render: modelo => (
          <Text truncate="end">{modelo.anio}</Text>
        )
      },
      {
        accessor: "actions",
        title: "Acciones",
        textAlign: "right",
        width: 100,
        render: modelo => (
          <DataTable.Actions
            onView={() => navigate(paths.dashboard.management.modelos.view(modelo.id))}
            onEdit={() => navigate(paths.dashboard.management.modelos.edit(modelo.id))}
            onDelete={() => handleDelete(modelo)}
          />
        ),
      }
    ],
    []
  )

  const handleDelete = modelo => {
    modals.openConfirmModal({
      title: "Confirmar borrado",
      children: <Text>¿Está seguro de que desea borrar el modelo?</Text>,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onConfirm: () => {
        deleteMutation.mutate({
          model: modelo,
          route: { id: modelo.id }
        }, {
          onSuccess: () => {
            notifications.show({
              title: 'Borrado',
              message: 'El modelo fue borrado con éxito',
              color: 'green',
            });
          },
          onFailure: (error) => {
            notifications.show({
              title: 'Error',
              message: error.message || 'No se pudo borrar el modelo',
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
        title="Modelos"
        description="Lista de modelos"
        actions={
          <Group>

            <AddButton
              variant="default"
              size="xs"
              component={NavLink}
              to={paths.dashboard.management.modelos.add}
            >
              Agregar modelo
            </AddButton>
          </Group>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText("modelo")}
          recordsPerPageLabel={DataTable.recordsPerPageLabel("modelos")}
          paginationText={DataTable.paginationText("modelos")}
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
