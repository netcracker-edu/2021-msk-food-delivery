import { useState } from "react";
import {useNavigate} from "react-router-dom";
import { Card, Row, Col, Button, Modal, Typography, Image} from "antd";
import { addItem, removeItem, useCartContext } from "../../hooks/CartContext";
import "./productCard.css";
import ItemCountInput from "../ItemCountInput.js";
import ProductDetail from "./ProductDetail.js";
const { Text, Title, Link } = Typography;

const ProductCard = ({ auth, product }) => {
  const { cartItems, dispatch } = useCartContext();
  const getCountFromCart = () => {
    let id = `${product.id}`;
    if (cartItems[id]) return cartItems[id];
    return 0;
  }
  const [count, setCount] = useState(getCountFromCart());
  const [isModalVisible, setIsModalVisible] = useState(false);
  const navigate = useNavigate();
  const PICTURE_BASE = "http://localhost:8080/api/v1/file/";

  const addToCart = (value) => {
    let id = `${product.id}`;
    let data = { "key" : id, "value" : value};
    dispatch(addItem(data));
    setCount(value);
  }

  const deleteFromCart = () => {
    let id = `${product.id}`;
    dispatch(removeItem(id));
    setCount(0);
  }

  return (
    <Col xs={{ span: 24 }} sm={{ span: 12}} md={{ span: 8}} lg={{ span: 6}}>
      <Card
        cover={ <Image preview={false}
                  style={{maxWidth : '274px'}}
                  height={274}
                  alt={product.name}
                  src={product?.pictureUUID == null
                    ? PICTURE_BASE+"a3026e4e-02b0-4fc0-a92a-bdcdf064fa06"
                    : PICTURE_BASE+product.pictureUUID}
                />}
      >
        <Link onClick={() => setIsModalVisible(true)}>
          <b>{product.name}</b>
        </Link>

        <Title level={5}>
          Цена: {product.discount == 0
                  ? product.price.toFixed(2)
                  : <>
                      <Text delete type="danger">
                        {product.price.toFixed(2)}
                      </Text>
                      {" "}
                      {(product.price - product.discount).toFixed(2)}
                    </>
                }
        </Title>
        <Row>
          <Col span={12}>
            <ItemCountInput
              count={count}
              addToCart={addToCart}
              deleteFromCart={deleteFromCart}
            />
          </Col>
          <Col offset={4} span={8}>
            <Button type="primary"
              onClick={() => {
                    count == 0 ? addToCart(1) : addToCart(count);
                    navigate("/profile/cart")}}
            >
              Купить
            </Button>
          </Col>
        </Row>
      </Card>

      {/*PRODUCT DETAILS*/}
      <Modal
        title={product.name}
        visible={isModalVisible}
        onCancel = {() => setIsModalVisible(false)}
        footer={null}
      >
            <ProductDetail
              count={count}
              addToCart={addToCart}
              deleteFromCart={deleteFromCart}
              product={product}
            />
      </Modal>
    </Col>
  );

}

export default ProductCard;
