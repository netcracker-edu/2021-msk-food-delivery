import React from "react";
import { Card, Tag, Descriptions, Row, Col, Divider } from "antd";
import { useNavigate, useLocation} from "react-router-dom";
import './styles/style.css';

const OrderItem = (props) => {
    const statusTagMap = new Map();
    statusTagMap.set('CREATED', ['cyan', 'Created']);
    statusTagMap.set('COURIER_APPOINTED', ['blue', 'Courier is appointed']);
    statusTagMap.set('PACKING', ['geekblue', 'Packing']);
    statusTagMap.set('DELIVERING', ['purple', 'Delivering']);
    statusTagMap.set('DELIVERED', ['green', 'Delivered']);
    statusTagMap.set('CANCELLED', ['red', 'Cancelled']);
    
    const {order, page, size} = {...props}; 
    const navigate = useNavigate();
    const location = useLocation();
    
    return (
        <>{!order ? <></> : 
        <>
        <Card hoverable className='order_history_card'
        onClick={() => navigate(`/profile/orderHistory/${order.id}`, { state : {
            page: page,
            size: size
        }})}>
        <Row justify="space-between" className="order_card_header">
                <Col>{`Order id: ${order.id}`}</Col>
                <Col>{`Started: ${order.dateStart}`}</Col>
        </Row>
        <Divider style={{margin: '14px'}}/>
            <Descriptions>
                <Descriptions.Item label="Status">
                        <Tag color={`${statusTagMap.get(order.status)[0]}`}>
                            {`${statusTagMap.get(order.status)[1]}`}
                        </Tag>
                </Descriptions.Item>
                
                <Descriptions.Item label="Cost">    
                    <strong>{order.overallCost}</strong>  
                </Descriptions.Item>
                
                {order.dateEnd ? (
                    order.status === 'DELIVERED' 
                    ?
                        <Descriptions.Item label='Delivered on'>
                            <strong>{`${order.dateEnd}`}</strong>
                        </Descriptions.Item>
                    
                    :   <Descriptions.Item label='Cancelled on'>
                            <strong>{`${order.dateEnd}`}</strong>
                        </Descriptions.Item>
                                       
                    ) 
                : null}
            </Descriptions>
        </Card>
        </>}</>
    );
}

export default OrderItem;