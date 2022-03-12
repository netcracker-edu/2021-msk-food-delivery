import React, { useState } from "react";
import { Button, Popconfirm, message } from "antd";
import Config from "../../api/Config";
import { patchFetch} from "../../helpers/fetchers";

const OrderCancelButton = (props) => {
    const config = new Config();
    const {auth, orderId, setCancelButtonPressed, setOrderStatus} = {...props};
    const [disabled, setDisabled] = useState(false);

    async function cancelOrder(){
        message.loading('In progress...');
        if (config.tokenExpired()) {
            await auth.refreshToken();
        }
        await patchFetch(config.ORDER_URL + `/${orderId}/status`, 
        config.headersWithAuthorization(), JSON.stringify({newStatus: "CANCELLED"}))
        .then( () => {
            setDisabled(true);
            setCancelButtonPressed(true);
            setOrderStatus("CANCELLED");
            message.destroy();    
            message.success('Cancelled!', 2.5);
        })
        .catch((err) => {
            message.destroy();
            console.log(err);
            message.error('Error', 2.5);
        });        
    }

    return (
        <>
        {
            disabled === true ? 
                <Button type="primary" danger disabled={disabled}>Cancel order</Button> 
            :
                <Popconfirm title="Are you sure?" okText="Yes" cancelText="No" onConfirm={cancelOrder}>
                    <Button type="primary" danger disabled={disabled}>
                        Cancel order
                    </Button>
                </Popconfirm>
                
        } 
        </>
    );
}

export default OrderCancelButton;