import {
  PiArchiveDuotone,
  PiProhibitDuotone,
  PiPulseDuotone,
  PiUsersDuotone,
} from 'react-icons/pi';
import { Group, Loader, SimpleGrid } from '@mantine/core';
import { MetricCard } from '@/components/metric-card';
import { useGetArticulosMetrics } from '@/hooks';
import { formatInt } from '@/utilities/number';

export function ArticuloMetrics() {
  const { data: metrics, isLoading } = useGetArticulosMetrics();

  const cards = [
    { icon: PiUsersDuotone, title: 'Total proveedores', value: metrics?.total, color: 'blue' },
    { icon: PiPulseDuotone, title: 'Active proveedores', value: metrics?.banned, color: 'teal' },
    { icon: PiProhibitDuotone, title: 'Banned proveedores', value: metrics?.banned, color: 'orange' },
    { icon: PiArchiveDuotone, title: 'Archived proveedores', value: metrics?.banned, color: 'red' },
  ];

  return (
    <SimpleGrid cols={{ base: 1, sm: 2, xl: 4 }}>
      {cards.map((card) => (
        <MetricCard.Root key={card.title}>
          <Group>
            <MetricCard.Icon c={card.color}>
              <card.icon size="2rem" />
            </MetricCard.Icon>
            <div>
              <MetricCard.TextMuted>{card.title}</MetricCard.TextMuted>
              <MetricCard.TextEmphasis>
                {isLoading ? <Loader size="sm" color={card.color} /> : formatInt(card.value ?? 0)}
              </MetricCard.TextEmphasis>
            </div>
          </Group>
        </MetricCard.Root>
      ))}
    </SimpleGrid>
  );
}
