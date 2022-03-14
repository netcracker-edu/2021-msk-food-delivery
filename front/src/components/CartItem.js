import { useState } from "react";
import { addItem, removeItem, useCartContext } from "../hooks/CartContext";
import {Row, Col, Avatar, Typography, InputNumber, Divider} from "antd";
import { DeleteTwoTone } from '@ant-design/icons';
import ItemCountInput from "./ItemCountInput.js";
const {Text, Link} = Typography;

const CartItem = ({product, itemCount}) => {
  const { cartItems, dispatch } = useCartContext();
  const [count, setCount] = useState(itemCount);
  const PICTURE_BASE = "http://localhost:8080/api/v1/file/";

  const addToCart = (count) => {
    setCount(count);
    let id = `${product.id}`;
    let data = { "key" : id, "value" : count};
    dispatch(addItem(data));
  }

  const deleteFromCart = () => {
    let id = `${product.id}`;
    dispatch(removeItem(id));
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
          <Col span={5}>
            <ItemCountInput count={count} addToCart={addToCart} deleteFromCart={deleteFromCart}/>
          </Col>
          <Col span={3}>
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
