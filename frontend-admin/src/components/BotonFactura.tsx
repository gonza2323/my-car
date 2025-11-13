import { Button } from "@mantine/core";
import { FileText } from "lucide-react";
import axios from "axios";

interface BotonFacturaProps {
    alquilerId: number;
    disabled?: boolean;
}

export default function BotonFactura({ alquilerId, disabled = false }: BotonFacturaProps) {
    const handleVerFactura = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/v1/facturas/alquiler/${alquilerId}/pdf`, {
                responseType: "blob",
            });

            const file = new Blob([response.data], { type: "application/pdf" });
            const fileURL = URL.createObjectURL(file);
            window.open(fileURL, "_blank");
        } catch (error) {
            console.error("Error descargando la factura:", error);
            alert("No se pudo descargar la factura");
        }
    };

    return (
        <Button
            variant="light"
            color="teal"
            radius="xl"
            leftSection={<FileText size={16} />}
            onClick={handleVerFactura}
            disabled={disabled}
            styles={{
                root: { fontWeight: 500 },
            }}
        >
            Ver Factura
        </Button>
    );
}
