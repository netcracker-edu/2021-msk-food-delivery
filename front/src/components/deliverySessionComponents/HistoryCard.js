import { useLocation, useNavigate } from "react-router-dom";
import { Card, Row, Col, Divider, Descriptions, Tag} from "antd";

const ACTIVE_CARD_FONT_COLOR = '#00FF7F';

const HistoryCard = ({session, page, size, activeSessionStyle}) => {
    const navigate = useNavigate();
    const location = useLocation();

    return (
      <>{!session ? <></> : 
        <>
        <Card hoverable style={activeSessionStyle} className='history_card'
        onClick={() => navigate(`${location.pathname}/${session.id}`, { state : {
            page: page,
            size: size
        }})}>
        <Row justify="space-between" className="history_card_header">
                <Col>{`Session id: ${session.id}`}</Col>
                <Col>{`Started: ${session.startTime}`}</Col>
        </Row>
        <Divider style={{margin: '14px'}}/>
        <div className="history_card_descriptions">
            <div className="history_card_key_value_wrapper">
                <span>Orders completed: </span><span><strong>{session.ordersCompleted ?? 0}</strong></span> 
            </div>

            <div className="history_card_key_value_wrapper">
                <span>Money earned: </span><span><strong>{session.moneyEarned?.toFixed(2) ?? '0.00'}</strong></span> 
            </div>
       
            {session.averageTimePerOrder ? 
                <div className="history_card_key_value_wrapper">
                    <span>Average time per order: </span><span><strong>{session.averageTimePerOrder}</strong></span> 
                </div>
            :   <></>    
            }

                
            {session.endTime ? 
                <div className="history_card_key_value_wrapper">
                    <span>Finished: </span><span><strong>{session.endTime ?? 0}</strong></span> 
                </div>
                :   
                <div style={{display: "flex", justifyContent: "flex-end"}}>
                    <Tag color="green">In progress</Tag> 
               </div>

            }

            </div>
        </Card>
        </>
    }</>
    );
}

export default HistoryCard;