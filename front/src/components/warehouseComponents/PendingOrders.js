import { Collapse, Empty, Pagination} from "antd";
import { useEffect, useState } from "react";
import { useParams, useLocation } from "react-router-dom";

import OrderClient from "../../api/OrderClient";
import OrderRowColList from "../orderComponents/OrderRowColList";

const { Panel } = Collapse;

const PendingOrders = ({auth}) => {
    const orderClient = new OrderClient(auth);

    const location = useLocation();
    
    const params = useParams();
    const warehouseId = params.warehouseId;

    const [courierAppointedOrders, setCourierAppointedOrders] = useState([]);
    const [packingOrders, setPackingOrders] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    
    const [courierAppointedPage, setCourierAppointedPage] = useState(1);
    const [courierAppointedSize, setCourierAppointedSize] = useState(5);
    const [courierAppointedOrdersAmount, setAppointedOrdersAmount] = useState(null);

    const [packingPage, setPackingPage] = useState(1);
    const [packingSize, setPackingSize] = useState(5);
    const [packingOrdersAmount, setPackingOrdersAmount] = useState(null);
    
    async function fetchOrders(){
        setIsLoading(true);
        const appointedOrders = await orderClient.fetchFilteredOrderPage(warehouseId, "COURIER_APPOINTED", 
        courierAppointedPage, courierAppointedSize);
        setCourierAppointedOrders(appointedOrders.data);
        const appointedAmount = await orderClient.fetchFilteredAmount(warehouseId, "COURIER_APPOINTED");
        setAppointedOrdersAmount(appointedAmount.data.amount);

        const packingOrders = await orderClient.fetchFilteredOrderPage(warehouseId, "PACKING", 
        packingPage, packingSize);
        setPackingOrders(packingOrders.data);
        const packingAmount = await orderClient.fetchFilteredAmount(warehouseId, "PACKING");
        setPackingOrdersAmount(packingAmount.data.amount);
        setIsLoading(false);
    }

    const onAppointedPageChange = async (newPageNumber, newOrdersPerPage) => {
        setCourierAppointedPage(newPageNumber);
        setCourierAppointedSize(newOrdersPerPage);
        const response = await orderClient.fetchFilteredOrderPage(warehouseId, 
        "COURIER_APPOINTED", newPageNumber, newOrdersPerPage);
        setCourierAppointedOrders(response.data);
    }
    
    const onPackingPageChange = async (newPageNumber, newOrdersPerPage) => {
        setPackingPage(newPageNumber);
        setPackingSize(newOrdersPerPage);
        const response = await orderClient.fetchFilteredOrderPage(warehouseId, 
        "PACKING", newPageNumber, newOrdersPerPage);
        setPackingOrders(response.data);
    }

    useEffect(() => {
        fetchOrders()
    }, []);

    return (
        <div className="pending_orders_wrapper">
            <h1>Текущие заказы</h1>
            {
                isLoading ? <></> : 
                !courierAppointedOrders.length && !packingOrders.length ? 
                <div className='empty_wrapper'><Empty description='Сейчас заказов нет'/></div> 
            :
            <Collapse>
                {
                   !courierAppointedOrders.length ? <></> : 
                    <Panel header="Передать в упаковку">
                        <div className="appointed_orders_wrapper">
                            <OrderRowColList orders={courierAppointedOrders} page={courierAppointedPage}
                            size={courierAppointedSize} customPrevPath={location.pathname}/>
                        </div>
                        <div style={{margin: '15px 0'}}>
                            <Pagination showSizeChanger current={courierAppointedPage} 
                            onChange={onAppointedPageChange} defaultCurrent={1} defaultPageSize={5} 
                            total={courierAppointedOrdersAmount} pageSizeOptions={[3, 5, 10]} 
                            />
                        </div>
                    </Panel>
                }
                
                {
                   !packingOrders.length ? <></> : 
                    <Panel header="Передать в доставку">
                        <div className="packing_orders_wrapper">
                            <OrderRowColList orders={packingOrders} page={packingPage}
                            size={packingSize} customPrevPath={location.pathname} />
                        </div>
                        <div style={{margin: '15px 0'}}>
                            <Pagination showSizeChanger current={packingPage} 
                            onChange={onPackingPageChange} defaultCurrent={1} defaultPageSize={5} 
                            total={packingOrdersAmount} pageSizeOptions={[3, 5, 10]} 
                            />
                        </div>
                    </Panel>    
                }
                
            </Collapse>
             }   
        </div>
    );
}

export default PendingOrders;