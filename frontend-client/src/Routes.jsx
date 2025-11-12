import HomeView from "./views/HomeView";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import NavBar from "@/components/NavBar/NavBar";
import ShopFooter from "@/components/Footer/ShopFooter";
import ErrorView from "./views/ErrorView";
import CartView from "./views/CartView";
import DeliveryView from "./views/DeliveryView";

export default function AppRoutes() {
    return (
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
    )
};
