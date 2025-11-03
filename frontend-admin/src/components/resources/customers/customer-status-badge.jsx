import { Badge } from "@mantine/core"
import { match } from "@/utilities/match"

export function CustomerStatusBadge({ status, variant = "outline", ...props }) {
  const color = match(
    [status === "active", "teal"],
    [status === "banned", "orange"],
    [status === "archived", "red"],
    [true, "gray"]
  )

  return (
    <Badge color={color} variant={variant} {...props}>
      {status}
    </Badge>
  )
}
