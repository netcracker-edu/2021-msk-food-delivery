import React from "react";
import { Row, Col } from "antd";

import OrderHistoryCard from "./OrderHistoryCard";

const OrderRowColList = ({orders, page, size, customPrevPath}) =>{
    return (
        <>
        {orders?.length ?
            <Row gutter={[0, 24]}>          
            {orders.map((order) => <Col span={24}><OrderHistoryCard order={order} 
            page={page} size={size} customPrevPath={customPrevPath}/></Col>)}
            </Row> : <></>
        }
        </>
    );
}

export default OrderRowColList;