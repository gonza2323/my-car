import Product from "../Product/Product";

import "./TopProducts.css";
import Skeleton from "react-loading-skeleton";

const products = [
  {
    _id: 1,
    name: "hola",
    description: "hola2",
    price: 20
  }
]

const TopProducts = () => {

  let topProducts = products.sort(
    (a, b) => b.times_bought - a.times_bought
  );
  return (
    <div className="sub-container">
      <h2>Top Sellers!</h2>
      <div className="contains-product">
        {products > 0 ? (
          <div className="contains-product">
            {topProducts.map((product) => {
              return <Product key={product._id} product={product}></Product>;
            })}
          </div>
        ) : (
          <div className="skeleton">
            <Skeleton height={250}></Skeleton>
          </div>
        )}
      </div>
    </div>
  );
};
export default TopProducts;
