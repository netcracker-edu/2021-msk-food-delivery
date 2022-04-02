import {Row, Col, Typography, Divider} from "antd";
import {ThunderboltTwoTone} from '@ant-design/icons';
const { Text, Paragraph, Title } = Typography;

const CartTotalPrice = ({price}) => {
  return (
    <>
    {price == null
      ? "Полная сумма не расчитана"
      : <>
          <Row align="middle">
            <Col span={6}>
              <Title level={4}>
                Итого
              </Title>
            </Col>
            <Col offset={6} span={4}>
              <Paragraph>Скидка</Paragraph>
              {(price.discount).toFixed(2)}
            </Col>
            <Col offset={4} span={4}>
              <Text strong>
                <Paragraph> К оплате</Paragraph>
                {(price.overallCost).toFixed(2)}
              </Text>
            </Col>
          </Row>
        </>
    }
    </>
    );
}

export default CartTotalPrice;
