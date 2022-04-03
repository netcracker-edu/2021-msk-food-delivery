import { Link } from 'react-router-dom';
import { Menu } from 'antd';

const { Item } = Menu;

const CourierMenu = () => {
  return (
      <>
      <Menu theme="dark" mode="horizontal">
        <Item key="home">
            <Link to="/">Домой</Link>
        </Item>
        <Item key="profile">
            <Link to="/profile">Профиль</Link>
        </Item>
        <Item key="deliveries">
            <Link to="/deliverySessions">Доставки</Link>
        </Item>
        <Item key="sign">
            <Link to="/signout">
                Выйти
            </Link>
        </Item>
      </Menu>
      </>
  );
}

export default CourierMenu;
