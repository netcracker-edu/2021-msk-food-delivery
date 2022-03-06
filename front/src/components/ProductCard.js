import { Link, useNavigate} from "react-router-dom";
import { useState } from "react";
import { Card, Col, Button, Row, InputNumber, Modal } from "antd";
import { DeleteTwoTone } from '@ant-design/icons';
import { addItem, removeItem, useCartContext } from "../hooks/CartContext";
const { Meta } = Card;

const ProductCard = ({ auth, product }) => {
  const navigate = useNavigate();
  const [count, setCount] = useState(0);
  const { cartItems, dispatch } = useCartContext();
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

  const detail = () => {
    Modal.info({
      title: product.name,
      content: (
        <div>
          <p>{product.description}</p>
          <p>{product.composition}</p>
          <p>{product.expirationDays}</p>
          <p>{product.weight}</p>
        </div>
      ),
      onOk() {},
    });
  }

  return (
    <Col span={8}>
      <Card
        cover={ <img alt={product.name}
                  height={350}
                  src={product?.pictureUUID == null
                    ? PICTURE_BASE+"a3026e4e-02b0-4fc0-a92a-bdcdf064fa06"
                    : PICTURE_BASE+product.pictureUUID}
                />}>
        <Button type="link" onClick={detail}>
          <b>{product.name}</b>
        </Button>
        <Row>
          <Col span={24}>
            <h4>
              Цена: {product.discount == 0
                      ? product.price
                      : <>
                          <del>{product.price}</del> {product.price - product.discount}
                        </>
                    }
            </h4>
          </Col>
        </Row>
        <Row>
          <Col span={24}>
            Скидка: {product?.discount == null ? 0 : product.discount}
          </Col>
        </Row>

        <Row>
          <Col span={12}>
            {count == 0
              ? <Button onClick={() => addToCart(1)}> В корзину </Button>
              : <>
                <InputNumber defaultValue={1} min={0} max={100}
                        onChange={(value) => { value == 0
                                                ? deleteFromCart()
                                                : addToCart(value)}}/>
                <Button type="link" onClick={deleteFromCart}>
                  <DeleteTwoTone style={{ fontSize: '20px' }} twoToneColor="#eb2f96" />
                </Button>
                </>
            }
          </Col>
          <Col span={12}>
            <Button type="primary">Купить</Button>
          </Col>
        </Row>
      </Card>
    </Col>
  );

}

export default ProductCard;
