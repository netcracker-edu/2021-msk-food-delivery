import React from "react"
import { Rate, Space } from "antd";
import OrderClient from "../../api/OrderClient";
import './styles/style.css';
import { wrapWithMessages } from "../../helpers/wrapWithMessages.ts";

const UserRatingChanger = (props) => {
    const {auth, orderStatus, role, orderId, rating, setRating} = {...props};
    const orderClient = new OrderClient(auth);
     
    function handleRatingChange(newRating){
        if(newRating === rating) return;
        const wrapped = wrapWithMessages(async () => await orderClient.changeRating(orderId, role, newRating), () => setRating(newRating));
        wrapped();
    }

    return (
            <div>
                <Space size={10}>
                <div style={{marginTop: '4px'}} className="order_details_card_key">{role === "COURIER" ? 'Рейтинг клиента' : 'Рейтинг доставки'} :</div>
                {orderStatus === "DELIVERING" || orderStatus === "DELIVERED" ? 
                    <Rate allowHalf allowClear={false} onChange={handleRatingChange}  value={rating} defaultValue={rating ? rating : 0}></Rate>
                 : 
                    orderStatus === "CREATED" ?  
                        <Rate disabled defaultValue={0} /> 
                    :
                        <Rate allowHalf disabled value={rating} defaultValue={rating ? rating : 0} />
                    }
                </Space>
            </div>        
    );
}

export default UserRatingChanger;