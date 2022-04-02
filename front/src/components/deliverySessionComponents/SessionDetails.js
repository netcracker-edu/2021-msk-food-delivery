import { Col, Row, Card, Tag, Typography, Divider, Button, Popconfirm, message, Spin} from "antd";
import { Link, useLocation, useParams } from "react-router-dom";
import React, {useState, useEffect} from "react";

import DeliverySessionClient from "../../api/DeliverySessionClient";
import OrderClient from "../../api/OrderClient";
import OrderHistoryCard from "../orderComponents/OrderHistoryCard";

const {Title, Text} = Typography;

const ERROR_MESSAGE_DELAY = 3.5;
const SUCCESS_MESSAGE_DELAY = 2.0;
const REQUEST_INTERVAL = 5000;

let needToFindOrder = null;

const SessionDetails = ({auth}) => {

    const deliverySessionClient = new DeliverySessionClient(auth);
    const orderClient = new OrderClient(auth);

    const [session, setSession] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [orders, setOrders] = useState([]);
    const [currentOrder, setCurrentOrder] = useState(null);
    const [isCurrentSession, setIsCurrentSession] = useState(false);

    const params = useParams();
    const location = useLocation();
    let page = location.state?.page;
    let size = location.state?.size;

    async function fetchSession(){
        setIsLoading(true);
        const session = await deliverySessionClient.getSessionById(params.sessionId);
        setSession(session);
        setIsCurrentSession(session?.endTime === null);
        const ordersResponse = await orderClient.getOrdersFromSession(params.sessionId);
        setOrders(ordersResponse.data);
        if(session.endTime == null){
            let found = false;
            for (let i = 0; i < ordersResponse.data.length; i++) {
                const order = ordersResponse.data[i];
                if(order.status !== "CANCELLED" && order.status !== "DELIVERED"){
                    setCurrentOrder(order);
                    found = true;
                    break;
                }
            }
            setIsLoading(false);
            if(!found){
                needToFindOrder = true;
                await fetchCurrentOrder(ordersResponse.data);
            }
            return;
        }
        setIsLoading(false);
    }

    async function finishSession(){
        message.info("In progress...");
        const finishResult = await deliverySessionClient.finishSession();
        if(finishResult === true){
            setIsLoading(true);
            setSession(await deliverySessionClient.getSessionById(params.sessionId));
            setIsCurrentSession(false);
            setIsLoading(false); 
            message.destroy();
            message.success("Finished!", SUCCESS_MESSAGE_DELAY);
            needToFindOrder = false;
        } else {    // else getting not finished orderId
            message.destroy();
            message.error(`You're still appointed to order! (ID: ${finishResult})`, ERROR_MESSAGE_DELAY);
        }
    }

    async function fetchCurrentOrder(orders){
       if(currentOrder) return;
       while(needToFindOrder){
            const response = await orderClient.getCurrentOrder();
            if(response.data != null){
                if(!orders) orders = [];
                orders.unshift(response.data);
                setOrders(orders);
                setCurrentOrder(response.data);
                needToFindOrder = false;
                return;
            }
            await new Promise(resolve => setTimeout(resolve, REQUEST_INTERVAL));
        }
    }

    useEffect(() => {
        fetchSession();
        return () => {
            needToFindOrder = false;     // on unmount
        }
    }, []);

    return ( 
        <>{isLoading ? <></> : 
        <div className="session_details_wrapper">
            <Row style={{margin: "8px 0"}} justify="center">
                <Col>
                    <Link to={"/deliverySessions"} state={{ page: page, size: size}} 
                     style={{fontSize: '13px'}}>
                        Back to delivery history
                    </Link>
                </Col>
            </Row>
            
            <Card className="session_details_card">
                <Row justify="center">
                    <Col>
                        <Title style={{display: "inline-flex"}} level={3}>{`Session id: ${params.sessionId}`}</Title>
                    </Col>
                </Row>
                {isCurrentSession ?
                <Row justify="center">    
                    <Col>
                        <Tag style={{display: "inline-flex"}} color="green">In progress</Tag>
                    </Col>
                </Row>
                : <></>}
                <Divider style={{margin: "14px 0"}}></Divider>
                <div className="session_details_properties">
                    <div className="session_details_key_value_wrapper">
                        <span>Orders completed: </span><span><strong>{session.ordersCompleted ?? 0}</strong></span> 
                    </div>

                    <div className="session_details_key_value_wrapper">
                        <span>Money earned: </span><span><strong>{session.moneyEarned?.toFixed(2) ?? '0.00'}</strong></span> 
                    </div>
            
                    {session.averageTimePerOrder ? 
                        <div className="session_details_key_value_wrapper">
                            <span>Average time per order: </span><span><strong>{session.averageTimePerOrder}</strong></span> 
                        </div>
                    :   <></>    
                    }

                    <div className="session_details_key_value_wrapper">
                        <span>Started: </span><span><strong>{session.startTime}</strong></span> 
                    </div>
                        
                    {session.endTime ? 
                        <div className="session_details_key_value_wrapper">
                            <span>Finished: </span><span><strong>{session.endTime}</strong></span> 
                        </div>
                        : <></>
                    } 
                </div>
                <>{session.endTime == null ?  
                    <><Divider />
                    <div className="buttons_container">

                        <Popconfirm
                            title="Are you sure?"
                            onConfirm={finishSession}
                            okText="Yes"
                            cancelText="No"
                        >
                            <Button type="primary">Finish session</Button>
                        </Popconfirm>
                    </div></>
                    : <></>
                }</> 

            </Card>
            <Divider>
                <Title level={4}>Orders</Title>
            </Divider>
            {isCurrentSession && currentOrder == null ? 
                <Card style={{borderRadius: '10px', width: '40%', margin: '0 auto'}}>
                    <div style={{display: 'flex', justifyContent: 'center'}}>
                        
                            <Text level={5} strong>Searching suitable order...</Text>
                        
                            <Spin style={{marginLeft: '10px'}}/>
                        
                    </div>
                </Card>
                : <></>
            }
            <>{!isCurrentSession && !orders?.length ? 
            <div style={{textAlign: 'center', margin: '0 auto'}}>
                <Text level={5} strong >
                    No orders in this session
                </Text>
            </div> 
            :
            <div style={{margin: '24px auto'}}>
                <Row gutter={[0, 24]}>          
                    {orders?.map((order) => 
                    <Col span={24}>
                        <OrderHistoryCard order={order} page={page} customPrevPath={`/deliverySessions/${params.sessionId}`} size={size}/>
                    </Col>)}
                </Row>
            </div>
            }</>
        </div>
    }</>
    );
}

export default SessionDetails;