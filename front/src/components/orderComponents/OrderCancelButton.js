import React, { useState } from "react";
import { Button, Popconfirm } from "antd";
import Config from "../../api/Config";
import { patchFetch} from "../../helpers/fetchers";
import useWrapWithMessages from "../../hooks/useWrapWithMessages.ts";

const OrderCancelButton = (props) => {
    const config = new Config();
    const {auth, orderId, setCancelButtonPressed, setOrderStatus} = {...props};
    const [disabled, setDisabled] = useState(false);

    async function cancelOrder(){
        if (config.tokenExpired()) {
            await auth.refreshToken();
        }
        return await patchFetch(config.ORDER_URL + `/${orderId}/status`, 
        config.headersWithAuthorization(), JSON.stringify({newStatus: "CANCELLED"}));        
    }

    const wrappedCancelOrder = useWrapWithMessages(cancelOrder, () => {
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