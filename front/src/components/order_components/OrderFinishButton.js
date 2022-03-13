import React, { useState } from "react";
import { Button, message } from "antd";
import Config from "../../api/Config";
import { patchFetch } from "../../helpers/fetchers";

const OrderFinishButton = (props) => {
    const config = new Config();
    const [disabled, setDisabled] = useState(false);
    const {auth, orderId, setOrderStatus, setFinishButtonPressed} = {...props};

    async function finishOrder(){
        message.loading('In progress...');
        if (config.tokenExpired()) {
            await auth.refreshToken();
        }
        await patchFetch(config.ORDER_URL + `/${orderId}/status`, 
        config.headersWithAuthorization(), JSON.stringify({newStatus: "DELIVERED"}))
        .then( () => {
            setDisabled(true);
            setOrderStatus("DELIVERED");
            setFinishButtonPressed(true);
            message.destroy();    
            message.success('Finished!', 2.5);
        })
        .catch((err) => {
            message.destroy();
            console.log(err);
            message.error('Error', 2.5);
        });
    }

    return (
        <Button type="primary" onClick={finishOrder} disabled={disabled}>
            Finish delivery
        </Button>
    );
}

export default OrderFinishButton;