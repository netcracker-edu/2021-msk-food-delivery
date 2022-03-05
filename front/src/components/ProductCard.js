import { Link, useNavigate} from "react-router-dom";
import { useState } from "react";
import { Card, Col, Button, Row, InputNumber } from "antd";
const { Meta } = Card;

const ProductCard = ({ auth, product }) => {
  const navigate = useNavigate();
  const [count, setCount] = useState(0);
  const PICTURE_BASE = "http://localhost:8080/api/v1/file/";
  return (
    <Col span={8}>
      <Card
        cover={ <img alt={product.name}
                  height={350}
                  src={product?.pictureUUID == null
                    ? PICTURE_BASE+"a3026e4e-02b0-4fc0-a92a-bdcdf064fa06"
                    : PICTURE_BASE+product.pictureUUID}
                />}>
        <b>{product.name}</b>
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
              ? <Button onClick={() => setCount(1)}> В корзину </Button>
              : <InputNumber defaultValue={1} min={0} max={100}
                        onChange={(value) => setCount(value)}/> 
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
