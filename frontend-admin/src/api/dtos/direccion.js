import { z } from "zod";

export const DireccionCreateDto = z.object({
  calle: z.string().nonempty('Debe indicar la calle').max(50),
  numeracion: z.string().nonempty('Debe indicar la numeración').max(20),
  barrio: z.string().max(50).optional(),
  manzanaPiso: z.string().max(10).optional(),
  casaDepartamento: z.string().max(10).optional(),
  referencia: z.string().max(50).optional(),
  paisId: z.coerce.number().int().positive('Debe seleccionar un país'),
  provinciaId: z.coerce.number().int().positive('Debe seleccionar una provincia'),
  departamentoId: z.coerce.number().int().positive('Debe seleccionar un departamento'),
  localidadId: z.coerce.number().int().positive('Debe seleccionar una localidad'),
});