import {useNavigate} from "react-router-dom";
import {Layout, Image, Typography, Row, Col, Button} from "antd";
import ItemCountInput from "./ItemCountInput.js";

const {Sider, Content} = Layout;
const {Text, Paragraph} = Typography;

const ProductDetail = ({product, count, addToCart, deleteFromCart}) => {
  const PICTURE_BASE = "http://localhost:8080/api/v1/file/";
  const navigate = useNavigate();
  return (
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
          Цена: {product.discount == 0
                  ? product.price
                  : <>
                      <Text delete type="danger">{product.price}</Text> {product.price - product.discount}
                    </>
                }
        </Paragraph>
        <Row>
          <Col span={10}>
            <ItemCountInput count={count} addToCart={addToCart} deleteFromCart={deleteFromCart}/>
          </Col>
          <Col offset = {4} span={10}>
          <Button type="primary"
            onClick={() => {
                  count == 0 ? addToCart(1) : addToCart(count);
                  navigate("/profile/cart")}}
          >
            Купить
          </Button>
          </Col>
        </Row>
      </Content>
    </Layout>
  );
}

export default ProductDetail;
