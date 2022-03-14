import {Row, Col, Typography, Divider} from "antd";
const { Text } = Typography;

const CartTitle = () => {
  return (
      <>
        <Row align="middle">
            <Col span={2}>
              {""}
            </Col>
            <Col span={10}>
              <Text strong>Наименование</Text>
            </Col>
            <Col span={4}>
              <Text strong>Цена за шт.</Text>
            </Col>
            <Col span={5}>
              <Text strong> Количество </Text>
            </Col>
            <Col span={3}>
              <Text strong> Стоимость </Text>
            </Col>
          </Row>
          <Divider></Divider>
        </>
    );
}

export default CartTitle;
