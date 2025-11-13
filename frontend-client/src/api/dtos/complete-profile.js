import z from "zod";


export const TipoDocumentoEnum = z.enum(['DNI', 'PASAPORTE']);

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

export const ClienteCreateDto = z.object({
  nombre: z.string().nonempty('El nombre no puede estar vacío').max(50),
  apellido: z.string().nonempty('El apellido no puede estar vacío').max(50),
  fechaNacimiento: z.date().max(new Date(), 'La fecha debe ser en el pasado'),
  tipoDocumento: TipoDocumentoEnum,
  numeroDocumento: z.string().min(6).max(20),
  telefono: z.string().min(6).max(20),
  direccion: DireccionCreateDto,
  nacionalidadId: z.coerce.number().int().positive('Debe seleccionar una nacionalidad'),
});