import React, { useEffect, useState } from "react";
import { Button, Card, Row, Col, Divider, Pagination } from "antd";
import DeliverySessionClient from "../../api/DeliverySessionClient";
import './styles/style.css';
import HistoryCard from "./HistoryCard";
import { useLocation } from "react-router-dom";

const ACTIVE_CARD_BORDER_COLOR = '#00FF7F';

const History = ({auth}) => {
    const deliverySessionClient = new DeliverySessionClient(auth);
    
    const location = useLocation();

    const [isLoading, setIsLoading] = useState(true);
    const [sessions, setSessions] = useState([]);
    const [sessionsAmount, setSessionsAmount] = useState(null);
    const [currentSession, setCurrentSession] = useState(null);
    const [page, setPage] = useState(location.state?.page ?? 1);
    const [size, setSize] = useState(location.state?.size ?? 5);

    async function fetchData(){
        setSessions(await deliverySessionClient.fetchPage(page, size));
        setSessionsAmount(await deliverySessionClient.getOverallAmount().amount);
        setCurrentSession(await deliverySessionClient.getCurrentSession());
        setIsLoading(false);
    }

    const onPageChange = async (newPageNumber, newOrdersPerPage) => {
        setPage(newPageNumber);
        setSize(newOrdersPerPage);
        setSessions(await deliverySessionClient.fetchPage(newPageNumber, newOrdersPerPage));
    }

    const startNewSession = async () => {
        setIsLoading(true);
        await deliverySessionClient.startSession();
        const newSession = await deliverySessionClient.getCurrentSession();
        setCurrentSession(newSession);
        let sessionArrCopy = [...sessions];
        sessionArrCopy.pop();
        sessionArrCopy.unshift(newSession);
        setSessions(sessionArrCopy);
        setIsLoading(false);
    }

    useEffect(() => fetchData(), []);

    return (
        
        <><h1>Delivery sessions</h1><div className="history_wrapper">
            <>{isLoading ? <></> :
            <>    { currentSession  == null ?
                    <div className="new_session_starter_wrapper">
                        <Card className="new_session_starter" bordered>
                            <Row justify="center" gutter={[8, 16]}>
                                <Col><h3>No active sessions currently. Start new one?</h3></Col>
                                <Col><Button disabled={currentSession != null} type="primary" onClick={startNewSession}>Start</Button></Col>
                            </Row>
                        </Card>
                    </div>
                    :
                    <></>
                    }

                {!sessions.length ? <></> :
                    <div className="history_pagination_wrapper">
                        <Divider><h3>History</h3></Divider>
                        <Row gutter={[0, 24]}>
                            <>{sessions.map((session) => <Col span={24}>
                                <HistoryCard session={session}
                                    activeSessionStyle={session?.id === currentSession?.id ?
                                        { border: `3px solid ${ACTIVE_CARD_BORDER_COLOR}` } : {}}
                                    page={page} size={size} />
                            </Col>)}</>
                        </Row>
                        <div className='pagination' style={{ margin: '15px 0' }}>
                            <Pagination showSizeChanger current={page} onChange={onPageChange}
                                defaultCurrent={1} defaultPageSize={5} total={6}
                                pageSizeOptions={[3, 5, 10]} />
                        </div>

                    </div>
                }</>
            }</>
        </div></>
    );
}

export default History;