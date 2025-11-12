import { modals } from '@mantine/modals';
import { TextInput, PasswordInput, Button, Group, Divider, Stack, Title, Anchor, Checkbox } from '@mantine/core';
import { useForm, zodResolver } from '@mantine/form';
import { FcGoogle } from 'react-icons/fc';
import { SiMicrosoft } from 'react-icons/si';
import { z } from 'zod';
import { useLogin } from '@/hooks';
import { app } from '@/config';
import { notifications } from '@mantine/notifications';

const GOOGLE_URL = `${app.baseUrl}/oauth2/authorization/auth0?connection=google-oauth2`;
const MICROSOFT_URL = `${app.baseUrl}/oauth2/authorization/auth0?connection=windowslive`;

const loginSchema = z.object({
  email: z.string().email({ message: 'Invalid email' }),
  password: z.string(),
  remember: z.boolean().optional(),
});


function LoginForm({ modalId }) {
  const { mutate: login, isPending } = useLogin();

  const form = useForm({
    mode: "uncontrolled",
    validate: zodResolver(loginSchema),
    initialValues: {
      email: "user@gmail.com",
      password: "",
      remember: false
    }
  })

  const handleSubmit = form.onSubmit(variables => {
    login(
      { variables },
      {
        onSuccess: () => {
          modals.closeAll();
          notifications.show({ title: 'Welcome back!', message: 'You have successfully logged in' });
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
        <Group justify='space-between' mt="md">
          <Checkbox name="remember" label="Recordarme" />
          <Button type="submit">Iniciar Sesión</Button>
        </Group>
      </form>

      <Divider label="O ingresa con" labelPosition="center" my="sm" />

      <Group grow>
        <Button leftSection={<FcGoogle />} component="a" href={GOOGLE_URL} variant="outline">Google</Button>
        <Button leftSection={<SiMicrosoft />} component="a" href={MICROSOFT_URL} variant="outline">Microsoft</Button>
      </Group>
    </Stack>
  );
}

export const openLoginModal = () => {
  const modalId = 'login-modal';

  modals.open({
    id: modalId,
    title: <Title order={4}>Inicia sesión</Title>,
    children: <LoginForm modalId={modalId} />,
    size: 'sm',
  });
};