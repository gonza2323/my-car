import { useQuery } from "@tanstack/react-query"
import { Group, Text, Loader, Tooltip } from "@mantine/core"
import { TbCloud, TbSun, TbCloudRain } from "react-icons/tb"

async function fetchWeather(lat, lon) {
  const res = await fetch(
    `https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}&current_weather=true`
  )
  if (!res.ok) throw new Error("Weather fetch failed")
  return res.json()
}

export function WeatherWidget() {
  // Hardcode your coordinates here
  const coords = { lat: -34.61, lon: -58.38 } // Buenos Aires example

  const { data, isLoading, error } = useQuery({
    queryKey: ["weather", coords],
    queryFn: () => fetchWeather(coords.lat, coords.lon),
    staleTime: 1000 * 60 * 10 // 10 min cache
  })

  if (isLoading) return <Loader size="xs" />
  if (error || !data) return <Text size="sm">N/A</Text>

  const { temperature, weathercode } = data.current_weather

  // Map Open-Meteo weather codes to icons
  const icon = [0, 1].includes(weathercode) ? (
    <TbSun size={16} />
  ) : [2, 3, 45, 48].includes(weathercode) ? (
    <TbCloud size={16} />
  ) : (
    <TbCloudRain size={16} />
  )

  const condition = [0, 1].includes(weathercode)
    ? "Clear"
    : [2, 3, 45, 48].includes(weathercode)
    ? "Cloudy"
    : "Rainy"

  return (
    <Tooltip label={condition} withArrow>
      <Group gap={4}>
        {icon}
        <Text size="sm">{temperature}°C</Text>
      </Group>
    </Tooltip>
  )
}
