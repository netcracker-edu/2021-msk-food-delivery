import { useState, useEffect } from "react";
import { addItem, removeItem, updateCart, useCartContext } from "../hooks/CartContext";
import {Row, Col, Avatar, Typography, InputNumber, Divider} from "antd";
import { DeleteTwoTone } from '@ant-design/icons';
import ItemCountInput from "./ItemCountInput.js";
const {Text, Link} = Typography;

const CartItem = ({product, itemCount,
                    calculateTotalPrice, deleteFromCartList}) => {
  const { cartItems, dispatch } = useCartContext();
  const [count, setCount] = useState(itemCount);
  const PICTURE_BASE = "http://localhost:8080/api/v1/file/";

  const updateCartItem = (count) => {
    setCount(count);
    let id = `${product.id}`;
    cartItems[id] = count;
    dispatch(updateCart(cartItems));
    if (count == 0) {
      deleteFromCartList(product.id);
    }
    calculateTotalPrice(cartItems);
  }

  const deleteFromCart = () => {
    let id = `${product.id}`;
    delete cartItems[id];
    dispatch(updateCart(cartItems));
    deleteFromCartList(product.id);
    calculateTotalPrice(cartItems);
    setCount(0);
  }

  return (
    <>
    {count == 0
      ? <></>
      : <>
        <Row align="middle">
          <Col span={2}>
            <Avatar size={50}
                    src={product?.pictureUUID == null
                          ? PICTURE_BASE+"a3026e4e-02b0-4fc0-a92a-bdcdf064fa06"
                          : PICTURE_BASE + product.pictureUUID}/>
          </Col>
          <Col span={10}>
            <Text>{product.name}</Text>
          </Col>
          <Col span={4}>
            <Text>
              {product.discount == 0
                    ? (product.price).toFixed(2)
                    : <>
                        <Text delete type="danger">{(product.price).toFixed(2)}</Text> {(product.price - product.discount).toFixed(2)}
                      </>
              }
            </Text>
          </Col>
          <Col span={4}>
            <ItemCountInput count={count} addToCart={updateCartItem} deleteFromCart={deleteFromCart}/>
          </Col>
          <Col span={4}>
            {product.discount == 0
                  ? <Text> {(product.price * count).toFixed(2)}</Text>
                  : <>
                      <Text> {((product.price - product.discount) * count).toFixed(2)}</Text>
                    </>
            }
          </Col>
        </Row>
        <Divider></Divider>
        </>
    }
    </>
  );
}

export default CartItem;
