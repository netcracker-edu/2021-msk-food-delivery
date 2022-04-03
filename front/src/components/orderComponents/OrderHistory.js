import {useState, useEffect, React} from 'react';
import { Layout, Pagination, Row, Col, Empty } from 'antd';
import OrderHistoryCard from './OrderHistoryCard';
import './styles/style.css';
import { useLocation } from 'react-router-dom';
import OrderClient from '../../api/OrderClient';
import OrderRowColList from './OrderRowColList';

const OrderHistory = ({auth}) => {

  const { Content } = Layout;

  const location = useLocation();

  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(location.state?.page ?? 1);
  const [size, setSize] = useState(location.state?.size ?? 5);
  const [overallOrdersAmount, setOverallOrderAmount] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const orderClient = new OrderClient(auth);

  async function fetchData(){
    let response = await orderClient.getOverallOrdersAmount();
    if (response && response.success) {
      setOverallOrderAmount(response.data.amount);
      if(overallOrdersAmount === 0){
        setIsLoading(false);
        return;
      }
    } else {
      console.log(JSON.stringify(response.error));
    }

    setOrders(await orderClient.fetchOrderPage(page, size)
    .then(result => [...result.data]));
    setIsLoading(false);
  }

  useEffect(() => {
    fetchData();
  }, []);

  const onPageChange = async (newPageNumber, newOrdersPerPage) => {
    setPage(newPageNumber);
    setSize(newOrdersPerPage);
    setOrders(await orderClient.fetchOrderPage(newPageNumber, newOrdersPerPage)
    .then(result => [...result.data]));
  }

  return (
  <Content className='order_history_wrapper'>
      <><h1>История заказов</h1>
      {isLoading ? <></> : !orders.length ? 
        <div className='empty_wrapper'><Empty description='Пока заказов нет'/></div> :
      <>

        <OrderRowColList page={page} size={size} orders={orders} />

        <div className='pagination' style={{margin: '15px 0'}}>
          <Pagination showSizeChanger current={page} onChange={onPageChange} 
          defaultCurrent={1} defaultPageSize={5} total={overallOrdersAmount}
          pageSizeOptions={[3, 5, 10]} 
          />
        </div>
      </>}
      </>
    </Content>
  );
}

export default OrderHistory;