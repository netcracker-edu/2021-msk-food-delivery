import {useState, useEffect, React} from 'react';
import { Layout, Pagination, Row, Col } from 'antd';
import OrderHistoryCard from './OrderHistoryCard';
import { commonFetch } from '../../helpers/fetchers';
import Config from '../../api/Config';
import ProfileClient from '../../api/ProfileClient';
import './styles/style.css';

const OrderHistory = ({auth}) => {

  const { Content } = Layout;
  const config = new Config();
  const profileClient = new ProfileClient(auth);
  
  const [profile, setProfile] = useState({});
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(5);  // default page size
  const [overallOrdersAmount, setOverallOrderAmount] = useState(null);

  async function getOverallOrdersAmount(){
    return await commonFetch(config.ORDERS_AMOUNT_URL, 'GET', config.headersWithAuthorization(), null);
  }

  function buildPaginationQuery(page, size){
    return config.ORDER_HISTORY_URL + `?page=${page - 1}&size=${size}`;
  }

  async function fetchData(){
    let response = await profileClient.get();
    if (response && response.success) {
      setProfile(response.data);
    }

    response = await getOverallOrdersAmount();
    if (response && response.success) {
      setOverallOrderAmount(response.data.amount);
      if(overallOrdersAmount === 0) return;
    }
    setOrders(await commonFetch(buildPaginationQuery(page, size), 'GET', config.headersWithAuthorization(), null)
    .then(result => [...result.data]));
  }

  useEffect(() => {
    fetchData();
  }, []);

  const onPageChange = async (newPageNumber, newOrdersPerPage) => {
    setPage(newPageNumber);
    setSize(newOrdersPerPage);
    setOrders(await commonFetch(buildPaginationQuery(newPageNumber, newOrdersPerPage), 'GET', config.headersWithAuthorization(), null)
    .then(result => [...result.data]));
  }

  return (
  <Content className='order_history_wrapper'>
      <h1>Order history</h1>
      <Row gutter={[0, 24]}>
        {orders == null ? <h2>No orders currently.</h2>:
        orders.map((order) => <Col span={24}><OrderHistoryCard order={order} profile={profile} auth={auth}/></Col>)}
      </Row>
      <div className='pagination' style={{margin: '15px 0'}}>
        <Pagination showSizeChanger current={page} onChange={onPageChange} 
        defaultCurrent={1} defaultPageSize={5} total={overallOrdersAmount}
        pageSizeOptions={[3, 5, 10]} 
        />
      </div>
    </Content>
  );
}

export default OrderHistory;