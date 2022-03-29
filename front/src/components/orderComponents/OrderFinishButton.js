import React, { useState } from "react";
import { Button } from "antd";
import {wrapWithMessages} from "../../helpers/wrapWithMessages.ts";
import OrderClient from "../../api/OrderClient";

const OrderFinishButton = (props) => {    
    const [disabled, setDisabled] = useState(false);
    const {auth, orderId, setOrderStatus, setFinishButtonPressed} = {...props};
    
    const orderClient = new OrderClient(auth);

    const wrappedFinishOrder = wrapWithMessages(async () => {
        await orderClient.changeOrderStatus(orderId, "DELIVERED");
    }, () => {
        setDisabled(true);
        setOrderStatus("DELIVERED");
        setFinishButtonPressed(true);
    });

    return (
        <Button type="primary" onClick={wrappedFinishOrder} disabled={disabled}>
            Finish delivery
        </Button>
    );
}

export default OrderFinishButton;