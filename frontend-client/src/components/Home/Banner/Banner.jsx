import car from "@/assets/images/car.jpg";
import "./Banner.css";

const Banner = () => {
  return (
    <div className="sub-container">
      <div className="banner">
        <div className="banner-text">
          <h1>
            Conseguí hasta 30% de descuento en<br></br>vehículos seleccionados
          </h1>
          <span className="is-buy-now">
            <a href="#products">
              <button className="btn-rounded buy-now">Alquilar Ahora</button>
            </a>
          </span>
        </div>
        <div className="subject">
          <img src={car} alt="Girl Headphones" width={"100%"} />
        </div>
      </div>
    </div>
  );
};
export default Banner;
