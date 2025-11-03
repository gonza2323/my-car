import { z } from 'zod';
import { dateSchema } from '@/utilities/date';

export const LoginRequestSchema = z.object({
  email: z.string().email(),
  password: z.string(),
  remember: z.boolean().optional(),
});
