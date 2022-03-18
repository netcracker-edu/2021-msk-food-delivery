import React from "react"
import { Rate, message, Space } from "antd";
import Config from "../../api/Config";
import { patchFetch } from "../../helpers/fetchers";
import './styles/style.css';
import useWrapWithMessages from "../../hooks/useWrapWithMessages.ts";

const UserRatingChanger = (props) => {
    const config = new Config();
    const {auth, orderStatus, role, orderId, rating, setRating} = {...props};
     
    async function changeRating(newRating){
        if(newRating === rating) return;
        if (config.tokenExpired()) {
            await auth.refreshToken();
        }
        return patchFetch(config.ORDER_URL + `/${orderId}/` + (role === 'COURIER' ? 'clientRating' : 'courierRating'),
        config.headersWithAuthorization(), JSON.stringify({rating: newRating}));
    }

    function useWrappedChangeRating(newRating){
        const wrapped = useWrapWithMessages(() => changeRating(newRating), () => setRating(newRating));
        if(newRating === rating) return;
        wrapped();
    }

    return (
            <div>
                <Space size={10}>
                <div style={{marginTop: '4px'}} className="order_details_card_key">{role === 'COURIER' ? 'Client' : 'Delivery'} rating:</div>
                {orderStatus === "DELIVERING" || orderStatus === "DELIVERED" ? 
                    <Rate allowHalf allowClear={false} onChange={useWrappedChangeRating}  value={rating} defaultValue={rating ? rating : 0}></Rate>
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