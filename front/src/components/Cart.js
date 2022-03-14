import { useState, useEffect } from "react";
import { Layout, List, Avatar, Typography, Row, Col } from "antd";
import {ThunderboltTwoTone} from '@ant-design/icons';
import { useCartContext } from "../hooks/CartContext";
import ProductClient from "../api/ProductClient.js";
import OrderClient from "../api/OrderClient.js";
import CartItem from "./CartItem.js";
import CartTitle from "./CartTitle.js";
import CartTotalPrice from "./CartTotalPrice.js";
const { Content } = Layout;
const { Text, Title } = Typography;

const Cart = ({auth}) => {
  const { cartItems, dispatch } = useCartContext();
  const productClient = new ProductClient(auth);
  const orderClient = new OrderClient(auth);
  const [cartList, setCartList] = useState([]);
  const [totalPrice, setTotalPrice] = useState();
  const PICTURE_BASE = "http://localhost:8080/api/v1/file/";

  const fetchCartProducts = async (cartItems) => {
    const response = await productClient.fetchCartProducts(cartItems);
    if (response && response.success) {
      setCartList(response.data);
    } else {
      setCartList([]);
    }
  }

  const calculateTotalPrice = async (cartItems) => {
    const response = await orderClient.calculateTotalPrice(cartItems);
    if (response && response.success) {
      setTotalPrice(response.data);
    } else {
      console.log(response.error);
      setTotalPrice();
    }
  }

  useEffect(() => {
    console.log(cartItems);
    if (cartItems) {
      fetchCartProducts(cartItems);
      calculateTotalPrice(cartItems);
    } else {
      setCartList([]);
      setTotalPrice();
    }
  }, [])

  return (
    <Content className="wrapper">
        {cartList.length == 0
          ? <h1>USER CART EMPTY!</h1>
          : <>
            <h2>КОРЗИНА</h2>
            <Layout className={totalPrice?.highDemandCoeff > 1 ? "purple-border" : "grey-border"}>
              {
                totalPrice?.highDemandCoeff > 1
                ? <Text style={{textAlign:"left"}}>
                    <ThunderboltTwoTone style={{ fontSize: '25px' }}
                      twoToneColor="#800080"
                    />
                    <Text style={{color:"#800080"}}>
                      Повышенный спрос x{totalPrice.highDemandCoeff}
                    </Text>
                  </Text>
                : <></>
              }
            <br></br>
            <CartTitle />
            {cartList.map(product =>
              <CartItem
                  key={product.id}
                  product={product}
                  itemCount={cartItems[`${product.id}`]}
              />
            )}
            <CartTotalPrice price={totalPrice}/>
            </Layout>
            </>
        }
    </Content>
  );
}

export default Cart;
