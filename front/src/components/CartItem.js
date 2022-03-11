import { useState } from "react";
import { addItem, removeItem, useCartContext } from "../hooks/CartContext";
import {Row, Col, Avatar, Typography, InputNumber} from "antd";
import { DeleteTwoTone } from '@ant-design/icons';
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
      : <Row>
          <Col span={4}>
            <Avatar size={50}
                    src={product?.pictureUUID == null
                          ? PICTURE_BASE+"a3026e4e-02b0-4fc0-a92a-bdcdf064fa06"
                          : PICTURE_BASE + product.pictureUUID}/>
          </Col>
          <Col span={10}>
            <Text>{product.name}</Text>
          </Col>
          <Col span={5}>
            <Text strong>
              {product.discount == 0
                    ? product.price
                    : <>
                        <Text delete type="danger">{product.price}</Text> {product.price - product.discount}
                      </>
              }
            </Text>
          </Col>
          <Col span={5}>
            <InputNumber
                  addonAfter={
                    <Link onClick={deleteFromCart}>
                      <DeleteTwoTone style={{ fontSize: '20px' }} twoToneColor="#eb2f96" />
                    </Link>
                  }
                  defaultValue={count} min={0} max={100}
                  onChange={(value) => ( value == 0
                                          ? deleteFromCart()
                                          : addToCart(value))}
            />
          </Col>
        </Row>
    }
    </>
  );
}

export default CartItem;
