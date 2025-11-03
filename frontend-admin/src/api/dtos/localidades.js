import { z } from 'zod';

export const LocalidadCreateDto = z.object({
  denominacion: z.string().nonempty ("El nombre no puede estar vacío"),
});
