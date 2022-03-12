import React, { useEffect, useState } from "react";
import { Layout, Card, Row, Col, Space} from "antd";
import { UserOutlined, HomeOutlined, CreditCardOutlined } from "@ant-design/icons";
import { useParams, useNavigate, Link } from "react-router-dom";
import Config from "../../api/Config";
import { commonFetch } from "../../helpers/fetchers";
import ProfileClient from '../../api/ProfileClient';
import UserRatingChanger from './UserRatingChanger';
import OrderDetailsProductCard from "./OrderDetailsProductCard";
import './styles/style.css';
import OrderSteps from "./OrderSteps";
import OrderCancelButton from "./OrderCancelButton";

const {Content} = Layout;

const OrderDetails = (props) => {
    const {orderId} = useParams();
    const [profile, setProfile] = useState({});
    const config = new Config();
    const navigate = useNavigate();
    const [order, setOrder] = useState({client: {}});
    const profileClient = new ProfileClient(props.auth);
    const [products, setProducts] = useState([]);
    const [cancelButtonPressed, setCancelButtonPressed] = useState(false);
    const [rating, setRating] = useState(null);
    const [orderStatus, setOrderStatus] = useState(null);

    const fetchData = async () => {

        let response = await profileClient.get();
        if (response && response.success) {
          setProfile(response.data);
        }

        response = await commonFetch(config.ORDER_URL + `/${orderId}`, 'GET', config.headersWithAuthorization(), null); 
        if(response.success){
            setOrder(response.data);
            setOrderStatus(response.data.status);
            setProducts(response.data.products);
            if(profile.role === 'CLIENT') setRating(response.data.deliveryRating);
            else setRating(response.data.clientRating);

        } else {
            navigate(config.BASE_PATH + 'notFound');
        }

    }

    useEffect(() => {
        fetchData();
      }, []);

    return ( 
        <Content className="order_details_wrapper">
            <Space direction='vertical' size={8} style={{marginBottom: '8px'}}>
                <Row>
                    <Col>
                        <Link to="/profile/orderHistory" style={{fontSize: '13px'}}>
                            Back to order history
                        </Link>
                    </Col>
                </Row>
                <Space direction='vertical' size={4}>   
                    <Row>
                        <Col span={24} >
                            <span style={{fontSize: '24px', fontWeight: 'bold'}}>{`Order № ${order.id}`}</span>
                        </Col>
                    </Row>
                    <Row>
                        <Col span={24} >
                            {`Dated ${order.dateStart}`}
                        </Col>
                    </Row>
                </Space>
            </Space>
            <Card className="order_details_card">
                    <Row>
                        <Col span={8}>
                            <div>
                                <UserOutlined className='order_details_card_icon'/><span className='order_details_card_key'>Client</span><br/>
                                <span className='order_details_card_value'>{order.client.fullName}</span><br/>
                                <span className='order_details_card_value'>{order.client.phoneNumber}</span><br/>
                                <span className='order_details_card_value'>{order.client.email}</span>
                            </div>    
                        </Col>

                        <Col span={6}>
                            <div>
                                <CreditCardOutlined className='order_details_card_icon'/><span className='order_details_card_key'>Payed</span><br/>
                                <span className='order_details_card_value'>Discount</span><br/>
                                <span className='order_details_card_value'>High demand coefficient</span><br/>
                            </div>
                        </Col>

                        <Col span={2}>
                             <div>
                                <span className='order_details_card_key'>{order.overallCost?.toFixed(2)}</span><br/>
                                <span className='order_details_card_value'>{order.discount?.toFixed(2)}</span><br/>
                                <span className='order_details_card_value'>{order.highDemandCoeff?.toFixed(2)}</span>
                            </div>                        
                        </Col>

                        <Col offset={1} span={7}>
                            <div>
                                <HomeOutlined className='order_details_card_icon'/><span className='order_details_card_key'>Delivery</span><br/>
                                <span className='order_details_card_value'>{order.address}</span><br/>
                            </div>
                        </Col>
                    </Row>
                    <Row justify='end'>
                        <Col>
                            <UserRatingChanger auth={props.auth} orderStatus={orderStatus} role={profile.role} 
                            rating={rating} setRating={setRating} orderId={orderId}/>    
                        </Col>
                    </Row>
            </Card>
            <div className='order_details_products_wrapper'>
                
                <div className='order_details_steps_cancell_button_wrapper'>
                    <OrderSteps status={order.status} cancelButtonPressed={cancelButtonPressed} />
                    {
                    order.status === "CANCELLED" || order.status === "DELIVERED" ? 
                        <></>
                    : 
                        <div className="order_details_cancel_button_wrapper">
                            <OrderCancelButton auth={props.auth} orderId={orderId} 
                            setCancelButtonPressed={setCancelButtonPressed} setOrderStatus={setOrderStatus}/>            
                        </div>
                    }
                </div> 
                
                <div style={{flexBasis: '68%'}}>
                    <Row gutter={[0, 24]}>
                        {products.map((element, idx) =>  
                            <Col span={24}>
                                <OrderDetailsProductCard product={element.product} 
                                amount={element.amount} idx={idx} auth={props.auth} />
                            </Col>
                        )}
                    </Row>
                </div>
            </div>
        </Content>
     );
}

export default OrderDetails;