import { Button, Group , Modal, Text} from "@mantine/core";
import { useState } from "react";
import { DatePickerInput } from "@mantine/dates";
import dayjs from "dayjs";
import { client } from "@/api/axios";

interface Props {
    fechaMin: string | null;
    fechaMax: string | null;
}

export function ReporteRecaudacionButton({ fechaMin, fechaMax }: Props) {
    const [opened, setOpened] = useState(false);
    const [fechaInicio, setFechaInicio] = useState<Date | null>(null);
    const [fechaFin, setFechaFin] = useState<Date | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const handleDownloadPdf = async () => {
        if (!fechaInicio || !fechaFin) {
            setError("Debe seleccionar ambas fechas.");
            return;
        }

        const inicio = dayjs(fechaInicio);
        const fin = dayjs(fechaFin);

        if (fechaMin && inicio.isBefore(dayjs(fechaMin))) {
            setError(`La fecha de inicio no puede ser anterior a ${fechaMin}.`);
            return;
        }
        if (fechaMax && fin.isAfter(dayjs(fechaMax))) {
            setError(`La fecha de fin no puede ser posterior a ${fechaMax}.`);
            return;
        }
        if (inicio.isAfter(fin)) {
            setError("La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        setError(null);
        setLoading(true);
        try {
            const response = await client.get(
                `http://localhost:8080/api/v1/reportes/recaudacion?fechaInicio=${inicio.format(
                    "YYYY-MM-DD"
                )}&fechaFin=${fin.format("YYYY-MM-DD")}`,
                {
                    responseType: "blob",
                    headers: { Accept: "application/pdf" },
                }
            );

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute(
                "download",
                `recaudacion_${inicio.format("YYYY-MM-DD")}_a_${fin.format("YYYY-MM-DD")}.pdf`
            );
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
            setOpened(false);
        } catch (error) {
            console.error("Error al generar PDF:", error);
            setError("Error al generar el reporte de recaudación.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Button color="blue" variant="filled" size="xs" onClick={() => setOpened(true)}>
                📄 Generar PDF
            </Button>

            <Modal
                opened={opened}
                onClose={() => {
                    setError(null);
                    setOpened(false);
                }}
                title="Generar reporte de recaudación"
                centered
            >
                <Group grow>
                    <DatePickerInput
                        label="Desde"
                        value={fechaInicio}
                        onChange={setFechaInicio}
                        placeholder="Seleccionar inicio"
                    />
                    <DatePickerInput
                        label="Hasta"
                        value={fechaFin}
                        onChange={setFechaFin}
                        placeholder="Seleccionar fin"
                    />
                </Group>

                {fechaMin && fechaMax && (
                    <Text size="sm" mt="xs" c="dimmed">
                        Fechas disponibles: {fechaMin} → {fechaMax}
                    </Text>
                )}

                {error && (
                    <Text mt="xs" size="sm" c="red">
                        {error}
                    </Text>
                )}

                <Group justify="flex-end" mt="md">
                    <Button variant="default" onClick={() => setOpened(false)}>
                        Cancelar
                    </Button>
                    <Button color="blue" loading={loading} onClick={handleDownloadPdf}>
                        Generar
                    </Button>
                </Group>
            </Modal>
        </>
    );
}