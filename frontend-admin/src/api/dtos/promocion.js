import { z } from "zod";

export const PromocionCreateDto = z.object({
  descripcion: z.string().nonempty('Debe dar una descripción').max(50),
  codigoDescuento: z.string().nonempty('Debe indicar el código de descuento').max(50),
  fechaInicio: z.date(),
  fechaFin: z.date(),
  porcentajeDescuento: z.coerce.number().min(0.01).max(0.99),
});
