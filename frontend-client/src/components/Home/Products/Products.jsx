import Product from "./Product/Product";

import "./Products.css";
import { memo } from "react";
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

const Products = () => {
  let sortedProducts = products
    .slice()
    .sort((a, b) => a.name.localeCompare(b.name));

  return (
    <div className="sub-container" id="products">
      <h2>Los mejores autos para vos</h2>
      {products.length > 0 ? (
        <div className="contains-product">
          {sortedProducts.map((product) => (
            <Product key={product._id} product={product} />
          ))}
        </div>
      ) : (
        <div className="skeleton">
          <Skeleton height={250}></Skeleton>
        </div>
      )}
    </div>
  );
};
export default memo(Products);
