
import "react-loading-skeleton/dist/skeleton.css";
import { useEffect } from "react";
import Modal from "./components/Modals/Modal";
import CancelOrder from "./components/Modals/CancelOrder";
import "react-toastify/dist/ReactToastify.css";
import { QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "./providers/auth-provider";
import { queryClient } from "./api/query-client";
import '@mantine/core/styles.css';
import { MantineProvider } from "@mantine/core";
import { Notifications } from "@mantine/notifications";
import { ModalsProvider } from "@mantine/modals";
import AppRoutes from "./Routes";


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
