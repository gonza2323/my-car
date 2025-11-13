import OrderDetails from "./OrderDetails/OrderDetails";
import OrderSummary from "./OrderSummary/OrderSummary";
import EmptyState from "./EmptyState/EmptyState";

import "./Order.css";

const products = [
      {
        _id: 1,
        name: "Item 1",
        description: "hola 1",
        price: 50,
        quantity: 1,
      },
      {
        _id: 2,
        name: "Item 2",
        description: "hola 2",
        price: 150,
        quantity: 1,
      }
];

const Order = () => {
  return (
    <div className="main-order-container">
      <div className="view-order">
        <div className="order-title">
          <h2>Order</h2>
          <h2>{2} Items</h2>
        </div>
        <div className="order-container">
          {(products.length > 0 &&
            products.map((product) => {
              return (
                <OrderDetails
                  key={product._id}
                  product={product}
                ></OrderDetails>
              );
            })) || <EmptyState></EmptyState>}
        </div>
      </div>
      <div className="order-summary">
        <h2>Order Summary</h2>
        <OrderSummary></OrderSummary>
      </div>
    </div>
  );
};
export default Order;
