import React from "react";
import { Steps } from "antd";

const {Step} = Steps;

const OrderSteps = (props) => {
    const statuses = ["CREATED", "COURIER_APPOINTED", "PACKING", "DELIVERING", "DELIVERED"];
    const steps = [
        <Step title="Заказ создан"/>,
        <Step title="Курьер назначен" />,
        <Step title="Упаковка" />,
        <Step title="Доставка" />,
        <Step title="Доставлено!" status="finish"/>
    ];

    const {status, prevStatus, cancelButtonPressed, finishButtonPressed} = {...props};
    const stage = prevStatus == null? statuses.indexOf(status) : statuses.indexOf(prevStatus);

    return ( 
        <div className="order_steps">
        {
            status === "CANCELLED" && !prevStatus ? 
                <Steps>
                    <Step title="Заказ отменён." status="error" />
                </Steps>
            : 
                finishButtonPressed ? 
                    <Steps status="finish" direction="vertical" current={4}>
                        {steps}
                    </Steps>    
                :

                cancelButtonPressed ? 
                    <Steps direction="vertical" current={stage}>
                        {steps.slice(0, stage).concat(<Step title="Заказ отменён." status="error"/>)}
                    </Steps>    
                : 
                    <Steps status="process" direction="vertical" current={stage}>
                        {steps.slice(0, stage + 1)}
                    </Steps> 
        } 
        </div>       
    );
}

export default OrderSteps;