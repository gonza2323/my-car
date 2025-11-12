import "./Product.css";
import headphones_pink from "@/assets/images/airpods_max_pink.jpg";
import { FaStar } from "react-icons/fa";
import { toast } from "react-toastify";

const Product = ({ product }) => {
  
  return (
    <div className="product-container">
      <div className="image">
        <img
          src={product?.product_image || headphones_pink}
          alt="Product Image"
          width={"100%"}
        />
      </div>
      <div className="product-details">
        <div className="name-add-to-cart"></div>
        <div className="price">
          <div className="name-price-product">
            <h4>{product?.name}</h4>
            <h5>
              $<span className="actual-product-price">{product?.price}.00</span>
            </h5>
          </div>
          <h5>{product?.description}</h5>
          <div className="star-rating">
            <span>({parseInt(Math.random() * 100)} Reviews)</span>
          </div>
        </div>
        <div>
          <button
            className="add-to-cart"
          >
            Add to Cart
          </button>
        </div>
      </div>
      <div className="heart"></div>
    </div>
  );
};
export default Product;
