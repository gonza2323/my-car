import { useForm, zodResolver } from "@mantine/form";
import { Button, Group, TextInput } from "@mantine/core";
import { DatePickerInput } from "@mantine/dates";
import { z } from "zod";
import { modals } from "@mantine/modals";
import { useCreateCosto } from "@/hooks";
import { notifications } from "@mantine/notifications";

const schema = z
    .object({
        fechaDesde: z.date({ required_error: "Fecha desde es obligatoria" }),
        fechaHasta: z.date({ required_error: "Fecha hasta es obligatoria" }),
        costoTotal: z.coerce
            .number({ invalid_type_error: "Debe ser un número" })
            .positive("Debe ser mayor a 0"),
    })
    .refine((d) => d.fechaHasta > d.fechaDesde, {
        message: "La fecha hasta debe ser posterior a la fecha desde",
        path: ["fechaHasta"],
    });

function CreateCostoForm({ modeloId }: { modeloId: number }) {
    const form = useForm({
        initialValues: {
            fechaDesde: null,
            fechaHasta: null,
            costoTotal: 0,
        },
        validate: zodResolver(schema),
    });

    const createCosto = useCreateCosto();

    const handleSubmit = form.onSubmit((values) => {
        console.log(modeloId)
        createCosto.mutate(
            {
                variables: { ...values, caracteristicasAutoId: modeloId }
            },
            {
                onSuccess: () => {
                    form.reset();
                    modals.closeAll();
                    notifications.show({
                        title: 'Éxito',
                        message: 'Precio añadido correctamente',
                    });
                },
                onError: (error) => {
                    notifications.show({
                        title: 'Error',
                        message: error instanceof Error ? error.message : 'Ocurrió un error inesperado',
                        color: 'red',
                    });
                },
            }
        );
    });

    return (
        <form onSubmit={handleSubmit}>
            <DatePickerInput
                label="Fecha desde"
                placeholder="Selecciona fecha"
                {...form.getInputProps("fechaDesde")}
            />
            <DatePickerInput
                label="Fecha hasta"
                placeholder="Selecciona fecha"
                mt="md"
                {...form.getInputProps("fechaHasta")}
            />
            <TextInput
                label="Costo total"
                placeholder="Ej. 150000"
                type="number"
                mt="md"
                {...form.getInputProps("costoTotal")}
            />
            <Group justify="flex-end" mt="xl">
                <Button variant="subtle" onClick={() => modals.closeAll()}>
                    Cancelar
                </Button>
                <Button type="submit" loading={createCosto.isPending}>
                    Guardar
                </Button>
            </Group>
        </form>
    );
}

// This function no longer calls hooks directly
export function openCreateCostoModal(modeloId: number) {
    modals.open({
        title: "Agregar nuevo precio",
        centered: true,
        children: <CreateCostoForm modeloId={modeloId} />,
    });
}
