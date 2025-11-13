import { useAuth } from "@/hooks";
import { Link, useLocation } from "react-router-dom";
const Links = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="links">
      <Link to={"/"}>
        Inicio
      </Link>
      <Link to={"/about"} to={"/about"}>
        Acerca de
      </Link>
      <Link to={"/vehiculos"} to={"/vehiculos"}>
        Vehículos
      </Link>
      { isAuthenticated && <Link to={"/alquileres"}>
        Mis alquileres
      </Link>}
    </div>
  );
};

// replace with react router
export default Links;
