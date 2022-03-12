import React from "react";
import { Card, Row, Col, Divider } from "antd";
import './styles/style.css';
import Config from "../../api/Config";
import defaultPic from '../../logo.svg';

const OrderDetailsProductCard = (props) => {
    const config = new Config();
    const {product, amount, idx} = {...props};
    
    return ( 
        <Card className="order_details_product_card">
                <Row justify="space-between" className="order_card_header">
                    <Col>{`Position ${idx + 1}`}</Col>
                    <Col>{`${amount} `}{amount > 1 ? 'products, ' : 'product, '}{`${product.weight * amount} gm.`}</Col>
                </Row>
                <Divider style={{margin: "12px"}}/>
                <Row align="middle">
                    <Col span={8}><img src={product.pictureUUID ? config.FILE_URL + `/${product.pictureUUID}` : defaultPic} alt="Product" className="order_details_product_card_picture"/></Col>
                    <Col span={6}>{`${product.name}`}</Col>
                    <Col offset={6} span={4}>
                        <span className='order_details_card_key'>{(product.price * amount).toFixed(2)}</span><br/>    
                    </Col>
                </Row>
        </Card>
    );
}

export default OrderDetailsProductCard;