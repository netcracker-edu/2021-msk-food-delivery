import React from "react";
import { Steps } from "antd";

const {Step} = Steps;

const OrderSteps = (props) => {
    const statuses = ["CREATED", "COURIER_APPOINTED", "PACKING", "DELIVERING", "DELIVERED"];
    const steps = [
        <Step title="Order is created"/>,
        <Step title="Courier is appointed" />,
        <Step title="We're packing your order" />,
        <Step title="Delivering" />,
        <Step title="Delivered!" status="finish"/>
    ];

    const {status, cancelButtonPressed} = {...props};
    const stage = statuses.indexOf(status);

    return ( 
        <div className="order_steps">
        {
            status === "CANCELLED" ? 
                <Steps>
                    <Step title="Order is cancelled." status="error" />
                </Steps>
            :   
                cancelButtonPressed ? 
                    <Steps direction="vertical" current={stage}>
                        {steps.slice(0, stage).concat(<Step title="Order is cancelled." status="error"/>)}
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