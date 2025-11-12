import { Button } from "@mantine/core";
import { IconCar } from "@tabler/icons-react";
import { client } from "@/api/axios";

interface ReporteVehiculosButtonProps {
    fechaMin: string | null;
    fechaMax: string | null;
}

export function ReporteVehiculosButton({ fechaMin, fechaMax }: ReporteVehiculosButtonProps) {

    const handleDownload = async () => {
        if (!fechaMin || !fechaMax) {
            alert(" No hay fechas válidas para generar el reporte");
            return;
        }

        try {
            const response = await client.get(
                `http://localhost:8080/api/v1/reportes/vehiculos?fechaInicio=${fechaMin}&fechaFin=${fechaMax}`,
                { responseType: "blob", headers: { Accept: "application/pdf" } }
            );

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", `reporte_vehiculos_${fechaMin}_${fechaMax}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error al descargar el reporte de vehículos:", error);
            alert("No se pudo generar el reporte de vehículos alquilados");
        }
    };

    return (
        <Button
            leftSection={<IconCar size={16} />}
            variant="light"
            color="blue"
            size="xs"
            onClick={handleDownload}
        >
            Reporte de Vehículos
        </Button>
    );
}
