import React, { useState } from "react";
import { Button, Popconfirm } from "antd";
import { wrapWithMessages } from "../../helpers/wrapWithMessages.ts";
import OrderClient from "../../api/OrderClient";

const OrderCancelButton = (props) => {
    const {auth, orderId, setCancelButtonPressed, setOrderStatus} = {...props};
    const orderClient = new OrderClient(auth);

    const [disabled, setDisabled] = useState(false);

    const wrappedCancelOrder = wrapWithMessages( async () => {
        await orderClient.changeOrderStatus(orderId, "CANCELLED")
    }, () => {
        setDisabled(true);
        setCancelButtonPressed(true);
        setOrderStatus("CANCELLED");
    });

    return (
        <>
        {
            disabled === true ? 
                <Button type="primary" danger disabled={disabled}>Cancel order</Button> 
            :
                <Popconfirm title="Are you sure?" okText="Yes" cancelText="No" onConfirm={wrappedCancelOrder}>
                    <Button type="primary" danger disabled={disabled}>
                        Cancel order
                    </Button>
                </Popconfirm>
        } 
        </>
    );
}

export default OrderCancelButton;