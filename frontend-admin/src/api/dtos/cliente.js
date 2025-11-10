import { z } from "zod";
import { DireccionCreateDto } from "./direccion";
import { TipoDocumentoEnum } from "./empleado";

export const ClienteCreateDto = z.object({
  nombre: z.string().nonempty('El nombre no puede estar vacío').max(50),
  apellido: z.string().nonempty('El apellido no puede estar vacío').max(50),
  fechaNacimiento: z.date().max(new Date(), 'La fecha debe ser en el pasado'),
  tipoDocumento: TipoDocumentoEnum,
  numeroDocumento: z.string().min(6).max(20),
  telefono: z.string().min(6).max(20),
  email: z.string().email('Debe indicar un mail válido'),
  direccion: DireccionCreateDto,
  nacionalidadId: z.coerce.number().int().positive('Debe seleccionar una nacionalidad'),
});
