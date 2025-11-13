import { Stack, TextInput, PasswordInput, Button, Group, Divider, Title } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { modals } from '@mantine/modals';
import { z } from 'zod';
import { FcGoogle } from 'react-icons/fc';
import { SiGoogle, SiMicrosoft } from 'react-icons/si';
import { app } from '@/config';
import { useSignUp } from '@/hooks';

// OAuth URLs
const GOOGLE_URL = `${app.baseUrl}/oauth2/authorization/auth0?connection=google-oauth2`;
const MICROSOFT_URL = `${app.baseUrl}/oauth2/authorization/auth0?connection=windowslive`;

// Zod schema for sign up
const signUpSchema = z
  .object({
    email: z.string().email({ message: 'Invalid email' }),
    password: z.string().min(6, { message: 'Password must be at least 6 characters' }),
    passwordConfirm: z.string().min(6, { message: 'Confirm password must be at least 6 characters' }),
  })
  .refine((data) => data.password === data.passwordConfirm, {
    message: "Passwords don't match",
    path: ['passwordConfirm'],
  });

export function SignUpForm() {
  const { mutate: signup, isPending } = useSignUp();


  const form = useForm({
    schema: zodResolver(signUpSchema),
    initialValues: { email: '', password: '', passwordConfirm: '' },
  });

    const handleSubmit = form.onSubmit(variables => {
      signup(
        { variables },
        {
          onSuccess: () => {
            modals.closeAll();
            notifications.show({ title: 'Registro exitoso', message: 'Ahora debe completar su perfil' });
          },
          onError: error => {
            notifications.show({ message: error.message, color: 'red' });
          }
        }
      )
    })

  return (
    <Stack>
      <form onSubmit={handleSubmit}>
        <TextInput label="Email" placeholder="you@example.com" required {...form.getInputProps('email')} />
        <PasswordInput label="Contraseña" placeholder="Contraseña" required mt="sm" {...form.getInputProps('password')} />
        <PasswordInput
          label="Confirma la contraseña"
          placeholder="Confirma la contraseña"
          required
          mt="sm"
          {...form.getInputProps('passwordConfirm')}
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
