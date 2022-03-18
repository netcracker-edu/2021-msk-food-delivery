import React, { useState } from "react";
import { Button } from "antd";
import Config from "../../api/Config";
import { patchFetch } from "../../helpers/fetchers";
import useWrapWithMessages from "../../hooks/useWrapWithMessages.ts";

const OrderFinishButton = (props) => {
    const config = new Config();
    const [disabled, setDisabled] = useState(false);
    const {auth, orderId, setOrderStatus, setFinishButtonPressed} = {...props};

    async function finishOrder(){
        if (config.tokenExpired()) {
            await auth.refreshToken();
        }
        return await patchFetch(config.ORDER_URL + `/${orderId}/status`, 
        config.headersWithAuthorization(), JSON.stringify({newStatus: "DELIVERED"}));
    }
    
    const wrappedFinishOrder = useWrapWithMessages(finishOrder, () => {
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