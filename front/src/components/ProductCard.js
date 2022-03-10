
import { useState } from "react";
import { Card, Col, Button, Row, InputNumber, Modal, Image, Layout, Typography} from "antd";
import { DeleteTwoTone } from '@ant-design/icons';
import { addItem, removeItem, useCartContext } from "../hooks/CartContext";
import "../productCard.css";
const { Meta } = Card;
const { Sider, Content } = Layout;
const { Text, Paragraph, Title, Link } = Typography;

const ProductCard = ({ auth, product }) => {
  const { cartItems, dispatch } = useCartContext();
  const getCountFromCart = () => {
    let id = `${product.id}`;
    if (cartItems[id]) return cartItems[id];
    return 0;
  }
  const [count, setCount] = useState(getCountFromCart());
  const [isModalVisible, setIsModalVisible] = useState(false);
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
    <Col span={6}>
      <Card
        cover={ <img alt={product.name}
                  height={300}
                  src={product?.pictureUUID == null
                    ? PICTURE_BASE+"a3026e4e-02b0-4fc0-a92a-bdcdf064fa06"
                    : PICTURE_BASE+product.pictureUUID}
                />}>
        <Link onClick={() => setIsModalVisible(true)}>
          <b>{product.name}</b>
        </Link>
        <Row>
          <Col span={24}>
            <Title level={5}>
              Цена: {product.discount == 0
                      ? product.price
                      : <>
                          <Text delete type="danger">{product.price}</Text> {product.price - product.discount}
                        </>
                    }
            </Title>
          </Col>
        </Row>

        <Row>
          <Col span={12}>
            {count == 0
              ? <Button onClick={() => addToCart(1)}> В корзину </Button>
              : <>
                <InputNumber
                        addonAfter={
                          <Link onClick={deleteFromCart}>
                            <DeleteTwoTone style={{ fontSize: '20px' }} twoToneColor="#eb2f96" />
                          </Link>
                        }
                        defaultValue={count} min={0} max={100}
                        onChange={(value) => { value == 0
                                                ? deleteFromCart()
                                                : addToCart(value)}}/>

                </>
            }
          </Col>
          <Col span={12}>
            <Button type="primary">Купить</Button>
          </Col>
        </Row>
      </Card>
      <Modal
        title={product.name}
        visible={isModalVisible}
        onCancel = {() => setIsModalVisible(false)}
        footer={null}>
            <Layout>
              <Sider theme="light">
                <Image src={product?.pictureUUID == null
                            ? PICTURE_BASE+"a3026e4e-02b0-4fc0-a92a-bdcdf064fa06"
                            : PICTURE_BASE+product.pictureUUID}
                      alt={product.name}
                />
              </Sider>
              <Content className="white-background">
                <Text type="secondary">Описание:
                </Text>
                <p>{product.description}</p>
                <Text type="secondary">Состав:
                </Text>
                <p>{product.composition}</p>
                <Paragraph type="secondary">
                  Срок годности: {" "}
                  <Text>{product.expirationDays} суток</Text>
                </Paragraph>
                <Paragraph type="secondary">
                  Вес: {" "}
                  <Text>{product.weight} г.</Text>
                </Paragraph>
                <Paragraph type="secondary">
                  Цена: {" "}
                  <Text>{product.discount == 0
                          ? product.price
                          : <>
                              <del>{product.price}</del> {product.price - product.discount}
                            </>
                        }
                  </Text>
                </Paragraph>
                <Row>
                  <Col span={14}>
                    {count == 0
                      ? <Button onClick={() => addToCart(1)}> В корзину </Button>
                      : <>
                        <InputNumber defaultValue={count} min={0} max={100}
                                onChange={(value) => { value == 0
                                                        ? deleteFromCart()
                                                        : addToCart(value)}}/>
                        <Button type="link" onClick={deleteFromCart}>
                          <DeleteTwoTone style={{ fontSize: '20px' }} twoToneColor="#eb2f96" />
                        </Button>
                        </>
                    }
                  </Col>
                  <Col span={10}>
                    <Button type="primary">Купить</Button>
                  </Col>
                </Row>
              </Content>
            </Layout>
      </Modal>
    </Col>
  );

}

export default ProductCard;
