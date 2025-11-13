import { Stack, TextInput, PasswordInput, Button, Group, Divider, Title } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { modals } from '@mantine/modals';
import { z } from 'zod';
import { FcGoogle } from 'react-icons/fc';
import { SiMicrosoft } from 'react-icons/si';
import { app } from '@/config';

// OAuth URLs
const GOOGLE_URL = `${app.baseUrl}/oauth2/authorization/auth0?connection=google-oauth2`;
const MICROSOFT_URL = `${app.baseUrl}/oauth2/authorization/auth0?connection=windowslive`;

// Zod schema for sign up
const signUpSchema = z
  .object({
    email: z.string().email({ message: 'Invalid email' }),
    password: z.string().min(6, { message: 'Password must be at least 6 characters' }),
    confirmPassword: z.string().min(6, { message: 'Confirm password must be at least 6 characters' }),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'],
  });

export function SignUpForm() {
  const form = useForm({
    schema: zodResolver(signUpSchema),
    initialValues: { email: '', password: '', confirmPassword: '' },
  });

  return (
    <Stack>
      <form onSubmit={form.onSubmit((values) => console.log('Sign Up values:', values))}>
        <TextInput label="Email" placeholder="you@example.com" required {...form.getInputProps('email')} />
        <PasswordInput label="Contraseña" placeholder="Contraseña" required mt="sm" {...form.getInputProps('password')} />
        <PasswordInput
          label="Confirma la contraseña"
          placeholder="Confirma la contraseña"
          required
          mt="sm"
          {...form.getInputProps('confirmPassword')}
        />
        <Group justify="end" mt="md">
          <Button type="submit">Registrarse</Button>
        </Group>
      </form>

      <Divider label="O registrate con" labelPosition="center" my="sm" />

      <Group grow>
        <Button leftSection={<SiGoogle />} component="a" href={GOOGLE_URL} variant="outline">
          Google
        </Button>
        <Button leftSection={<SiMicrosoft />} component="a" href={MICROSOFT_URL} variant="outline">
          Microsoft
        </Button>
      </Group>
    </Stack>
  );
}

export const openSignUpModal = () => {
  modals.open({
    title: <Title order={4}>Registrate</Title>,
    children: <SignUpForm />, // <--- Component with hooks
    size: 'sm',
  });
};
