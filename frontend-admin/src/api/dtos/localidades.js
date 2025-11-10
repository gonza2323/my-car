import { z } from 'zod';

export const LocalidadCreateDto = z.object({
  nombre: z.string().nonempty("El nombre no puede estar vacío"),
  paisId: z.coerce.number().int().positive("Debe seleccionar un país"),
  provinciaId: z.coerce.number().int().positive("Debe seleccionar una provincia"),
  departamentoId: z.coerce.number().int().positive("Debe seleccionar un departamento"),
});
