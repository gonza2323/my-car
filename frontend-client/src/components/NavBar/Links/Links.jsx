import { useAuth } from "@/hooks";
import { Link, useLocation } from "react-router-dom";
const Links = () => {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const isHomePage = location.pathname === "/";

  const scrollToProducts = () => {
    if (!isHomePage) return;
    const products = document.getElementById("products");
    products.scrollIntoView({ behavior: "smooth" });
    removeExpandedClass();
  };

  const removeExpandedClass = () => {
    let mobileExpandedMenu = document.querySelector(".mobile-expanded-menu");
    mobileExpandedMenu.classList.remove("mobile-expanded");
  };

  return (
    <div className="links">
      <Link to={"/"} onClick={removeExpandedClass}>
        Inicio
      </Link>
      <Link to={"/modelos"} onClick={scrollToProducts}>
        Vehículos
      </Link>
      <Link to={"/alquileres"} onClick={scrollToProducts}>
        Acerca de
      </Link>
      { isAuthenticated && <Link to={"/delivery"} onClick={removeExpandedClass}>
        Mis alquileres
      </Link>}
    </div>
  );
};

// replace with react router
export default Links;
