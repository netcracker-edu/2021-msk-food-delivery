import { useState, useEffect } from "react";
import { Layout, List, Avatar, Typography, Row, Col } from "antd";
import { useCartContext } from "../hooks/CartContext";
import ProductClient from "../api/ProductClient.js";
import CartItem from "./CartItem.js";
const { Content } = Layout;
const { Text, Title } = Typography;

const Cart = ({auth}) => {
  const { cartItems, dispatch } = useCartContext();
  const productClient = new ProductClient(auth);
  const [cartList, setCartList] = useState([]);
  const PICTURE_BASE = "http://localhost:8080/api/v1/file/";

  const fetchCartProducts = async (cartItems) => {
    const response = await productClient.fetchCartProducts(cartItems);
    if (response && response.success) {
      setCartList(response.data);
    } else {
      setCartList([]);
    }
  }

  useEffect(() => {
    console.log(cartItems);
    if (cartItems) {
      fetchCartProducts(cartItems);
    } else {
      setCartList([]);
    }
  }, [])

  return (
    <Content className="wrapper">
        {cartList.length == 0
          ? <h1>USER CART EMPTY!</h1>
          : <>
            <div>USER CART</div>
            {cartList.map(product =>
              <CartItem
                  key={product.id}
                  product={product}
                  itemCount={cartItems[`${product.id}`]}
              />
            )}
            </>
        }
    </Content>
  );
}

export default Cart;
