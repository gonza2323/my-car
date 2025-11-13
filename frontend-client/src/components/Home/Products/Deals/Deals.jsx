import Product from "../Product/Product";
import "./Deals.css";
import Skeleton from "react-loading-skeleton";

const products = [
  {
    _id: 1,
    name: "Toyota Corolla",
    description: "Caja automática, 5 puertas",
    price: 59999
  },
  {
    _id: 2,
    name: "Ford Focus",
    description: "Caja manula, 5 puertas",
    price: 44999
  },
  {
    _id: 3,
    name: "Volkswagen Golf",
    description: "Caja manual, 5 puertas",
    price: 39999
  }
]

const Deals = () => {

  let cheapest = products.sort((a, b) => a.price - b.price);
  return (
    <div className="sub-container">
      <h2>Deals Just For You!</h2>
      {products.length > 0 ? (
        <div className="contains-product">
          {cheapest.map((product) => {
            return <Product key={product._id} product={product}></Product>;
          })}
        </div>
      ) : (
        <div className="skeleton">
          <Skeleton height={250}></Skeleton>
        </div>
      )}
    </div>
  );
};
export default Deals;
