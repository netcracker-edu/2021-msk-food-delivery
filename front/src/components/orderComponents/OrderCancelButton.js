import React, { useState } from "react";
import { Button, Popconfirm } from "antd";
import { wrapWithMessages } from "../../helpers/wrapWithMessages.ts";
import OrderClient from "../../api/OrderClient";

const OrderCancelButton = (props) => {
    const {auth, orderId, setCancelButtonPressed, setOrderStatus, orderStatus,
    setPrevOrderStatus} = {...props};
    const orderClient = new OrderClient(auth);

    const [disabled, setDisabled] = useState(false);

    const wrappedCancelOrder = wrapWithMessages( async () => {
        await orderClient.changeOrderStatus(orderId, "CANCELLED")
    }, () => {
        setDisabled(true);
        setCancelButtonPressed(true);
        setPrevOrderStatus(orderStatus.slice());
        setOrderStatus("CANCELLED");

    });

    return (
        <>
        {
            disabled === true ? 
                <Button type="primary" danger disabled={disabled}>Отменить</Button> 
            :
                <Popconfirm title="Are you sure?" okText="Yes" cancelText="No" onConfirm={wrappedCancelOrder}>
                    <Button type="primary" danger disabled={disabled}>
                        Отменить 
                    </Button>
                </Popconfirm>
        } 
        </>
    );
}

export default OrderCancelButton;