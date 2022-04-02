import { Link } from 'react-router-dom';
import { Menu } from 'antd';

const { Item } = Menu;

const CourierMenu = () => {
  return (
      <>
      <Menu theme="dark" mode="horizontal">
        <Item key="home">
            <Link to="/">Home</Link>
        </Item>
        <Item key="profile">
            <Link to="/profile">Profile</Link>
        </Item>
        <Item key="deliveries">
            <Link to="/deliverySessions">Deliveries</Link>
        </Item>
        <Item key="sign">
            <Link to="/signout">
                SignOut
            </Link>
        </Item>
      </Menu>
      </>
  );
}

export default CourierMenu;
