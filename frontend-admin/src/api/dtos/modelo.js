import { z } from "zod";

export const ModeloCreateDto = z.object({
  marca: z.string().nonempty('Debe indicar la marca').max(50),
  modelo: z.string().nonempty('Debe indicar el modelo').max(50),
  anio: z.coerce.number('Debe indicar el modelo').int().min(1960).max(2030),
  cantidadPuertas: z.coerce.number('Debe indicar la cantidad de puertas').int().min(0).max(10),
  cantidadAsientos: z.coerce.number('Debe indicar la cantidad de asientos').int().min(0).max(10),
});
