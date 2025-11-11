import { z } from "zod";
import { TipoDocumentoEnum } from "./empleado";

export const TipoDePago = z.enum(['EFECTIVO', 'TRANSFERENCIA', 'BILLETERA_VIRTUAL']);

export const RegistrarAlquilerDto = z.object({
  formaDePago: TipoDePago,
  clienteId: z.coerce.number().int().positive('Debe seleccionar un cliente'),
});
