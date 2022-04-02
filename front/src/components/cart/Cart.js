import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Layout, List, Avatar, Typography, Row, Col, Button, Spin, Alert } from "antd";
import {ThunderboltTwoTone} from '@ant-design/icons';
import { useCartContext } from "../../hooks/CartContext";
import ProductClient from "../../api/ProductClient.js";
import OrderClient from "../../api/OrderClient.js";
import CartItem from "./CartItem.js";
import CartTitle from "./CartTitle.js";
import CartTotalPrice from "./CartTotalPrice.js";
const { Content } = Layout;
const { Text, Title } = Typography;

const Cart = ({auth, address}) => {
  const { cartItems, dispatch } = useCartContext();
  const productClient = new ProductClient(auth);
  const orderClient = new OrderClient(auth);
  const [cartList, setCartList] = useState([]);
  const [totalPrice, setTotalPrice] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const [alert, setAlert] = useState([]);
  const coords = { "lat" : address.coord[0], "lon" : address.coord[1]};
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
    const response = await orderClient.calculateTotalPrice(cartItems, coords, address.warehouseId);
    if (response && response.success) {
      setTotalPrice(response.data);
    } else {
      setTotalPrice();
    }
  }

  const deleteFromCartList = (id) => {
    setCartList(cartList.filter( product => product.id !== id));
  }

  const orderCheckout = async () => {
    setAlert(["info", "Проверяем наличие и ищем курьера "]);
    setIsLoading(true);
    const response = await orderClient.orderCheckout(totalPrice, address, cartItems);
    if (response && response.success) {
      console.log(response.success);
      setAlert(["success", "Заказ оформлен!"]);
    } else {
      console.log(response.error);
      const UUID = response.error?.errorUUID;
      if (UUID == "6979bd9b-3a64-46c6-88d5-79917ed78616") {
        setAlert(["error", "Нет свободных курьеров. Попробуйте позже!"]);
      }
    }
    setIsLoading(false);
  }

  useEffect(() => {
    if (cartItems) {
      fetchCartProducts(cartItems);
      calculateTotalPrice(cartItems, coords, address.warehouseId);
    } else {
      setCartList([]);
      setTotalPrice();
    }
  }, [])

  return (
    <Content className="wrapper min-width-60">
      <h2>КОРЗИНА</h2>
      {cartList.length == 0
        ? <>
            <h3>Нет добавленных в корзину продуктов. Надеемся это временно!</h3>
            <Link to="/products">
              <Button type="primary" size="large"> К продуктам</Button>
            </Link>
          </>
        :
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
              {
                alert.length != 0
                ? <Alert type={alert[0]}
                        message={<span>
                                  {alert[1]}
                                  {isLoading ? <Spin/> : <></>}
                                </span>}
                        closable afterClose={() => setAlert([])}
                  />
                : <></>
              }
              <br></br>
              <CartTitle />
              {cartList.map(product =>
                <CartItem
                  key={product.id}
                  product={product}
                  itemCount={cartItems[`${product.id}`]}
                  calculateTotalPrice={calculateTotalPrice}
                  deleteFromCartList={deleteFromCartList}
                  coords={coords}
                  warehouseId={address.warehouseId}
                />
              )}
              <CartTotalPrice price={totalPrice}/>
              <br/>
              <Button type="primary" onClick={orderCheckout}>
                Оформить заказ
              </Button>
        </Layout>
        }
    </Content>
  );
}

export default Cart;
