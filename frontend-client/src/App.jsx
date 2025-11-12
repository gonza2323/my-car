import HomeView from "./views/HomeView";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import NavBar from "@/components/NavBar/NavBar";
import ShopFooter from "@/components/Footer/ShopFooter";
import ErrorView from "./views/ErrorView";
import CartView from "./views/CartView";
import DeliveryView from "./views/DeliveryView";
import "react-loading-skeleton/dist/skeleton.css";
import { useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import Modal from "./components/Modals/Modal";
import CancelOrder from "./components/Modals/CancelOrder";
import "react-toastify/dist/ReactToastify.css";
import { QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "./providers/auth-provider";
import { queryClient } from "./api/query-client";

function App() {

  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
      <div>
        <BrowserRouter>
          <header>
            <NavBar></NavBar>
          </header>
          <Routes>
            <Route path="/" element={<HomeView />} />
            <Route path="/cart" element={<CartView />} />
            <Route path="/delivery" element={<DeliveryView />} />
            <Route path="*" element={<ErrorView />} />
          </Routes>
          <footer>
            <ShopFooter></ShopFooter>
          </footer>
        </BrowserRouter>
        <ToastContainer />
      </div>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
