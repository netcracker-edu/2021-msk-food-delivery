import React from "react";
import { Button } from "antd";

import {wrapWithMessages} from "../../helpers/wrapWithMessages.ts";
import OrderClient from "../../api/OrderClient";

const OrderPackingButton = (props) => {
    const {auth, orderId, setOrderStatus, currentStatus} = {...props};
    
    const orderClient = new OrderClient(auth);

    const neededStatus = currentStatus === "COURIER_APPOINTED" ? "PACKING" : "DELIVERING";
    const wrappedFinishOrder
     = wrapWithMessages(async () => {
        await orderClient.changeOrderStatus(orderId, neededStatus);
    }, async () => {
        setOrderStatus(neededStatus);
    });

    return (
        <Button type="primary" onClick={wrappedFinishOrder}>
            {neededStatus === "PACKING" ? "Передать в упаковку" : "Передать в доставку"}
        </Button>
    );
}

export default OrderPackingButton;