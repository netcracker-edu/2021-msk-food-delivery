import React from "react";
import { Card, Tag, Descriptions, Row, Col, Divider } from "antd";
import { useNavigate } from "react-router-dom";
import './styles/style.css';

const OrderHistoryCard = (props) => {
    const statusTagMap = new Map();
    statusTagMap.set('CREATED', ['cyan', 'Создан']);
    statusTagMap.set('COURIER_APPOINTED', ['blue', 'Курьер назначен']);
    statusTagMap.set('PACKING', ['geekblue', 'Упаковка']);
    statusTagMap.set('DELIVERING', ['purple', 'Доставка']);
    statusTagMap.set('DELIVERED', ['green', 'Доставлен']);
    statusTagMap.set('CANCELLED', ['red', 'Отменён']);
    
    const {order, page, size, customPrevPath} = {...props}; 
    const navigate = useNavigate();

    return (
        <>{!order ? <></> : 
        <>
        <Card hoverable className='order_history_card'
        onClick={() => navigate(`/order/${order.id}`, { state : {
            page: page,
            size: size,
            prevPath: customPrevPath != null? customPrevPath : '/profile/orderHistory',
        }})}>
        <Row justify="space-between" className="order_card_header">
                <Col>{`ID заказа: ${order.id}`}</Col>
                <Col>{`Создан: ${order.dateStart}`}</Col>
        </Row>
        <Divider style={{margin: '14px'}}/>
            <Descriptions>
                <Descriptions.Item label="Статус">
                        <Tag color={`${statusTagMap.get(order.status)[0]}`}>
                            {`${statusTagMap.get(order.status)[1]}`}
                        </Tag>
                </Descriptions.Item>
                
                <Descriptions.Item label="Общая стоимость">    
                    <strong>{order.overallCost}</strong>  
                </Descriptions.Item>
                
                {order.dateEnd ? (
                    order.status === 'DELIVERED' 
                    ?
                        <Descriptions.Item label='Доставлен'>
                            <strong>{`${order.dateEnd}`}</strong>
                        </Descriptions.Item>
                    
                    :   <Descriptions.Item label='Отменён'>
                            <strong>{`${order.dateEnd}`}</strong>
                        </Descriptions.Item>
                                       
                    ) 
                : null}
            </Descriptions>
        </Card>
        </>}</>
    );
}

export default OrderHistoryCard;