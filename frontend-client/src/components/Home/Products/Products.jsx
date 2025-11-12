import Product from "./Product/Product";

import "./Products.css";
import { memo } from "react";
import Skeleton from "react-loading-skeleton";

const products = [
  {
    _id: 1,
    name: "hola",
    description: "hola2",
    price: 20
  }
]

const Products = () => {
  let sortedProducts = products
    .slice()
    .sort((a, b) => a.name.localeCompare(b.name));

  return (
    <div className="sub-container" id="products">
      <h2>Headphones For You</h2>
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
