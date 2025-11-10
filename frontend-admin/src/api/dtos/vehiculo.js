import { z } from "zod";

export const VehiculoCreateDto = z.object({
  patente: z.string().nonempty('Debe indicar la patente').max(50),
  caracteristicasAutoId: z.coerce.number().int().positive('Debe seleccionar un modelo'),
});
