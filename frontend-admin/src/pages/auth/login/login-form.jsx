import { NavLink, useLocation, useNavigate } from "react-router-dom"
import { Anchor, Button, Group, Stack } from "@mantine/core"
import { useForm, zodResolver } from "@mantine/form"
import { LoginRequestSchema } from "@/api/dtos"
import { Checkbox } from "@/components/forms/checkbox"
import { FormProvider } from "@/components/forms/form-provider"
import { PasswordInput } from "@/components/forms/password-input"
import { TextInput } from "@/components/forms/text-input"
import { useAuth, useLogin } from "@/hooks"
import { paths } from "@/routes"
import { handleFormErrors } from "@/utilities/form"

export function LoginForm({ ...props }) {
  const { mutate: login, isPending } = useLogin()
  const location = useLocation();
  const navigate = useNavigate();

  const params = new URLSearchParams(location.search);
  const redirectTo = params.get('r') || paths.dashboard.root;

  const form = useForm({
    mode: "uncontrolled",
    validate: zodResolver(LoginRequestSchema),
    initialValues: {
      email: "admin@gmail.com",
      password: "admin",
      remember: false
    }
  })

  const handleSubmit = form.onSubmit(variables => {
    login(
      { variables },
      {
        onError: error => handleFormErrors(form, error)
      }
    )
  })

  return (
    <FormProvider form={form} onSubmit={handleSubmit}>
      <Stack {...props}>
        <TextInput name="email" label="Email" required />
        <PasswordInput name="password" label="Password" required />
        <Group justify="space-between">
          <Checkbox name="remember" label="Remember me" />
          <Anchor size="sm" component={NavLink} to={paths.auth.forgotPassword}>
            Forgot password?
          </Anchor>
        </Group>
        <Button type="submit" loading={isPending}>
          Login
        </Button>
      </Stack>
    </FormProvider>
  )
}
