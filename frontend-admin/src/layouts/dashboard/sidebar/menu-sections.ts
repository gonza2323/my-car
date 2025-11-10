import { ElementType } from 'react';
import {
  PiBookDuotone,
  PiCarDuotone,
  PiChartLineUpDuotone,
  PiChatCenteredDotsDuotone,
  PiFilesDuotone,
  PiKanbanDuotone,
  PiLockKeyDuotone,
  PiMapPinDuotone,
  PiPencilDuotone,
  PiShieldCheckDuotone,
  PiShoppingCart,
  PiSquaresFourDuotone,
  PiStarDuotone,
  PiTableDuotone,
  PiTruck,
  PiUserDuotone,
  PiUserPlusDuotone,
  PiUsersDuotone,
} from 'react-icons/pi';
import { paths } from '@/routes/paths';

interface MenuItem {
  header: string;
  section: {
    name: string;
    href: string;
    icon: ElementType;
    dropdownItems?: {
      name: string;
      href: string;
      badge?: string;
    }[];
  }[];
}

export const menu: MenuItem[] = [
  {
    header: 'Overview',
    section: [
      {
        name: 'Welcome',
        href: paths.dashboard.home,
        icon: PiStarDuotone,
      },
    ],
  },

  {
    header: 'Management',
    section: [
      {
        name: 'Clientes',
        icon: PiUserDuotone,
        href: paths.dashboard.management.clientes.root,
        dropdownItems: [
          {
            name: 'List',
            href: paths.dashboard.management.clientes.list,
          },
        ],
      },
      {
        name: 'Empleados',
        icon: PiUsersDuotone,
        href: paths.dashboard.management.empleados.root,
        dropdownItems: [
          {
            name: 'List',
            href: paths.dashboard.management.empleados.list,
          },
        ],
      },
      {
        name: 'Modelos',
        icon: PiCarDuotone,
        href: paths.dashboard.management.modelos.root,
        dropdownItems: [
          {
            name: 'List',
            href: paths.dashboard.management.modelos.list,
          },
        ],
      },
      {
        name: 'Vehículos',
        icon: PiCarDuotone,
        href: paths.dashboard.management.vehiculos.root,
        dropdownItems: [
          {
            name: 'List',
            href: paths.dashboard.management.vehiculos.list,
          },
        ],
      },
      {
        name: 'Localidades',
        icon: PiMapPinDuotone,
        href: paths.dashboard.management.localidades.root,
        dropdownItems: [
          {
            name: 'List',
            href: paths.dashboard.management.localidades.list,
          },
        ],
      },
    ],
  },

  {
    header: 'Apps',
    section: [
      {
        name: 'Kanban',
        href: paths.dashboard.apps.kanban,
        icon: PiKanbanDuotone,
      },
    ],
  },

  {
    header: 'Widgets',
    section: [
      {
        name: 'Charts',
        href: paths.dashboard.widgets.charts,
        icon: PiChartLineUpDuotone,
      },
      {
        name: 'Metrics',
        href: paths.dashboard.widgets.metrics,
        icon: PiSquaresFourDuotone,
      },
      {
        name: 'Tables',
        href: paths.dashboard.widgets.tables,
        icon: PiTableDuotone,
      },
    ],
  },

  {
    header: 'Documentation',
    section: [
      {
        name: 'Documentation',
        href: paths.docs.root,
        icon: PiFilesDuotone,
      },
    ],
  },

  {
    header: 'Authentication',
    section: [
      {
        name: 'Register',
        href: paths.auth.register,
        icon: PiUserPlusDuotone,
      },
      {
        name: 'Login',
        href: paths.auth.login,
        icon: PiShieldCheckDuotone,
      },
      {
        name: 'Forgot Password',
        href: paths.auth.forgotPassword,
        icon: PiLockKeyDuotone,
      },
      {
        name: 'OTP',
        href: paths.auth.otp,
        icon: PiChatCenteredDotsDuotone,
      },
    ],
  },
];
