import React, { useEffect, useState } from "react";
import { Layout, Card, Row, Col, Space} from "antd";
import { UserOutlined, HomeOutlined, CreditCardOutlined, ShoppingOutlined } from "@ant-design/icons";
import { useParams, useNavigate, Link, useLocation } from "react-router-dom";

import UserRatingChanger from './UserRatingChanger';
import OrderDetailsProductCard from "./OrderDetailsProductCard";
import OrderSteps from "./OrderSteps";
import OrderCancelButton from "./OrderCancelButton";
import OrderFinishButton from "./OrderFinishButton";
import OrderClient from "../../api/OrderClient";
import ProfileClient from "../../api/ProfileClient";

import './styles/style.css';
import OrderPackingButton from "./OrderPackingButton";

const {Content} = Layout;

const OrderDetails = (props) => {
    const {orderId} = useParams();
    const navigate = useNavigate();
 
    const location = useLocation();
    const page = location.state?.page;
    const size = location.state?.size;
    const prevPath = location.state?.prevPath;
    
    const [order, setOrder] = useState({client: {}, courier: {}});
    const [profile, setProfile] = useState({});
    const [products, setProducts] = useState([]);
    const [cancelButtonPressed, setCancelButtonPressed] = useState(false);
    const [finishButtonPressed, setFinishButtonPressed] = useState(false);
    const [rating, setRating] = useState(null);
    const [orderStatus, setOrderStatus] = useState(null);
    const [prevOrderStatus, setPrevOrderStatus] = useState(null);   // needed for OrderSteps rendering
                                                                    // after OrderCancelButton pressed 
    const [isLoading, setIsLoading] = useState(true);

    const profileClient = new ProfileClient(props.auth);
    const orderClient = new OrderClient(props.auth);

    const NOT_FOUND_PAGE = "http://localhost:8080/api/v1/notFound";

    const fetchData = async () => {
        setIsLoading(true);
        let profileResponse = await profileClient.get();
        if (profileResponse && profileResponse.success) {
          setProfile(profileResponse.data);
        }
        
        let orderResponse = await orderClient.getOrderById(orderId);

        if(orderResponse.success){
            setOrder(orderResponse.data);
            setOrderStatus(orderResponse.data.status);
            setProducts(orderResponse.data.products);
            if(profile.role === "CLIENT") setRating(orderResponse.data.deliveryRating);
            else setRating(orderResponse.data.clientRating);
        } else {
            navigate(NOT_FOUND_PAGE);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchData();
      }, []);
      
    return (
        <Content className="order_details_wrapper">
        {isLoading ? <></> :
            <><Space direction='vertical' size={8} style={{marginBottom: '8px'}}>
                <Row>
                    <Col>
                    {prevPath === "/profile/orderHistory" ? 
                            <Link to={prevPath} state={{ page: page, size: size}} 
                            style={{fontSize: '13px'}}>
                                К истории заказов
                            </Link> 
                    : 
                    prevPath.search("^\/warehouses\/[0-9]+$") !== -1 ? 
                            <Link to={prevPath} 
                            style={{fontSize: '13px'}}>
                                К текущим заказам
                            </Link>
                    : 
                       
                    prevPath.search("^\/deliverySessions\/[0-9]+$") !== -1 ?
                        <Link to={prevPath} state={{ page: page, size: size}} 
                        style={{fontSize: '13px'}}>
                            К выходу на линию
                        </Link> 
                    : <></>   
                    }
                    </Col>
                </Row>
                <Space direction='vertical' size={4}>   
                    <Row>
                        <Col span={24} >
                            <h2>{`ID заказа: ${order.id}`}</h2>
                        </Col>
                    </Row>
                    <Row>
                        <Col span={24} >
                            {`Создан ${order.dateStart}`}
                        </Col>
                    </Row>
                </Space>
            </Space>
            <Card className="order_details_card">
                    <Row>
                        <Col span={8}>
                            <div>
                                <UserOutlined className='order_details_card_icon'/>
                                <span className='order_details_card_key'>Клиент</span><br/>
                                <span className='order_details_card_value'>{order.client.fullName}</span><br/>
                                <span className='order_details_card_value'>{order.client.phoneNumber}</span><br/>
                                <span className='order_details_card_value'>{order.client.email}</span>
                            </div>    
                        </Col>
                        {
                            !order.courier?.id ? 
                            <Col span={8}>
                                <div>
                                    <ShoppingOutlined className='order_details_card_icon'/>
                                    <span className='order_details_card_key'>Курьер</span><br/>
                                    <span className='order_details_card_value'>Не назначен</span><br/>    
                                </div>
                            </Col> 
                            : 
                            <Col span={8}>
                                <div>
                                    <ShoppingOutlined className='order_details_card_icon'/>
                                    <span className='order_details_card_key'>Курьер</span><br/>
                                    <span className='order_details_card_value'>{order.courier.fullName}</span><br/>
                                    <span className='order_details_card_value'>{order.courier.phoneNumber}</span><br/>
                                    <span className='order_details_card_value'>{order.courier.email}</span>    
                                </div>
                            </Col>
                        }
                        <Col span={6}>
                            <div>
                                <CreditCardOutlined className='order_details_card_icon'/>
                                <span className='order_details_card_key'>Оплачено</span><br/>
                                <span className='order_details_card_value'>Скидка</span><br/>
                                <span className='order_details_card_value'>Высокий спрос</span><br/>
                            </div>
                        </Col>

                        <Col span={2}>
                             <div>
                                <span className='order_details_card_key'>{order.overallCost?.toFixed(2)}</span><br/>
                                <span className='order_details_card_value'>{order.discount?.toFixed(2)}</span><br/>
                                <span className='order_details_card_value'>{order.highDemandCoeff?.toFixed(2)}</span>
                            </div>                        
                        </Col>
                    </Row>
                    <Row justify='space-between' align="middle">
                        <Col>
                            <div>
                                <HomeOutlined className='order_details_card_icon'/>
                                <span className='order_details_card_key'>Доставка</span><br/>
                                <span className='order_details_card_value'>{order.address}</span><br/>
                            </div>
                        </Col>
                        <Col>
                            <UserRatingChanger auth={props.auth} orderStatus={orderStatus} role={profile.role} 
                            rating={rating} setRating={setRating} orderId={orderId}/>    
                        </Col>
                    </Row>
            </Card>
            <div className='order_details_products_wrapper'>
                
                <div className='order_details_steps_buttons_wrapper'>
                    <OrderSteps status={orderStatus} cancelButtonPressed={cancelButtonPressed} 
                    finishButtonPressed={finishButtonPressed} prevStatus={prevOrderStatus}/>
                    {
                    orderStatus === null || orderStatus === "CANCELLED" || orderStatus === "DELIVERED" ? 
                        <></>
                    :   
                        <div className="order_details_buttons_wrapper">
                        {
                            (profile.role === "ADMIN" || profile.role === "MODERATOR") &&
                            (orderStatus === "COURIER_APPOINTED" || orderStatus === "PACKING") ?
                            <div className="order_details_packing_button_wrapper">
                                <OrderPackingButton auth={props.auth} orderId={orderId} 
                                setOrderStatus={setOrderStatus} currentStatus={orderStatus}
                                />
                            </div>
                            : <></>
                        }
                        {
                            <>
                            {profile.role === "COURIER" && orderStatus === "DELIVERING" ? 
                            <div className="order_details_finish_button_wrapper">
                                <OrderFinishButton auth={props.auth} orderId={orderId} 
                                setOrderStatus={setOrderStatus} setFinishButtonPressed={setFinishButtonPressed}/>
                            </div>
                            : <></>}

                            <div className="order_details_cancel_button_wrapper">
                                <OrderCancelButton auth={props.auth} orderId={orderId} 
                                setCancelButtonPressed={setCancelButtonPressed} 
                                orderStatus={orderStatus} setOrderStatus={setOrderStatus} 
                                setPrevOrderStatus={setPrevOrderStatus}/>            
                            </div>
                            </>
                        }
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
        </>}   
        </Content>
     );
}

export default OrderDetails;