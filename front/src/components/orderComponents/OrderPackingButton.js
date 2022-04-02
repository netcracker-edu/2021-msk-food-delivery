import React, { useState } from "react";
import { Button } from "antd";
import {wrapWithMessages} from "../../helpers/wrapWithMessages.ts";
import OrderClient from "../../api/OrderClient";

const OrderPackingButton = (props) => {
    const {auth, orderId, setOrderStatus, currentStatus} = {...props};
    
    const orderClient = new OrderClient(auth);

    const neededStatus = currentStatus === "COURIER_APPOINTED" ? "PACKING" : "DELIVERING";
    const wrappedFinishOrder = wrapWithMessages(async () => {
        await orderClient.changeOrderStatus(orderId, neededStatus);
    }, () => {
        setOrderStatus(neededStatus);
    });

    return (
        <Button type="primary" onClick={wrappedFinishOrder}>
            {neededStatus === "PACKING" ? "Order is packing" : "Order is packed"}
        </Button>
    );
}

export default OrderPackingButton;