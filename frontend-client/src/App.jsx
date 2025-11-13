
import "react-loading-skeleton/dist/skeleton.css";
import { useEffect } from "react";
import Modal from "./components/Modals/Modal";
import CancelOrder from "./components/Modals/CancelOrder";
import { QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "./providers/auth-provider";
import { queryClient } from "./api/query-client";
import { MantineProvider } from "@mantine/core";
import { Notifications } from "@mantine/notifications";
import { ModalsProvider } from "@mantine/modals";
import AppRoutes from "./Routes";

import '@mantine/core/styles.css';
import '@mantine/dates/styles.layer.css';
import '@mantine/notifications/styles.layer.css';

const theme = {
  primaryColor: 'orange', 
}


function App() {

  return (
    <div>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <MantineProvider theme={theme}>
            <Notifications position="bottom-center" />
            <ModalsProvider>
              <AppRoutes />
            </ModalsProvider>
          </MantineProvider>
        </AuthProvider>
      </QueryClientProvider>
    </div>
  );
}

export default App;
