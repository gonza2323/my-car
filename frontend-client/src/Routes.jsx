import HomeView from "./views/HomeView";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import NavBar from "@/components/NavBar/NavBar";
import ShopFooter from "@/components/Footer/ShopFooter";
import ErrorView from "./views/ErrorView";
import CartView from "./views/CartView";
import AlquileresList from "./views/AlquileresPage";
import VehiculosPage from "./views/VehiculosPage";
import { AuthGuard } from "./guards/auth-guard";
import { CompleteProfileGuard } from "./guards/complete-profile-guard";
import CompleteProfilePage from "./views/CompletarPerfil";

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <header>
                <NavBar></NavBar>
            </header>
            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<HomeView />} />
                <Route path="/vehiculos" element={<VehiculosPage />} />

                {/* Protected Routes with CompleteProfileGuard */}
                <Route
                    path="/alquileres"
                    element={
                        <AuthGuard>
                            <CompleteProfileGuard>
                                <AlquileresList />
                            </CompleteProfileGuard>
                        </AuthGuard>
                    }
                />

                <Route path="/success" element={<Navigate to={"/alquileres"} />} />

                {/* Complete profile route should be accessible even if profile is incomplete */}
                <Route path="/complete-profile" element={<AuthGuard><CompleteProfilePage /></AuthGuard>} />

                {/* Catch all */}
                <Route path="*" element={<ErrorView />} />
            </Routes>
            <footer>
                <ShopFooter></ShopFooter>
            </footer>
        </BrowserRouter>
    )
};
