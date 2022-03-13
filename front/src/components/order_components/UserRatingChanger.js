import React from "react"
import { Rate, message, Space } from "antd";
import Config from "../../api/Config";
import { patchFetch } from "../../helpers/fetchers";
import './styles/style.css';

const UserRatingChanger = (props) => {
    const config = new Config();
    const {auth, orderStatus, role, orderId, rating, setRating} = {...props};
     
    async function handleRatingChange(newRating){
        if(newRating === rating) return;
        message.loading('In progress...');
        if (config.tokenExpired()) {
            await auth.refreshToken();
        }
        patchFetch(config.ORDER_URL + `/${orderId}/` + (role === 'COURIER' ? 'clientRating' : 'courierRating'),
        config.headersWithAuthorization(), JSON.stringify({rating: newRating}))
        .then( () => {
            setRating(newRating);
            message.destroy();    
            message.success('Changed!', 2.5);
        })
        .catch((err) => {
            console.log(err);
            message.error('Error', 2.5);
        }); 
    }

    return (
            <div>
                <Space size={10}>
                <div style={{marginTop: '4px'}} className="order_details_card_key">{role === 'COURIER' ? 'Client' : 'Delivery'} rating:</div>
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