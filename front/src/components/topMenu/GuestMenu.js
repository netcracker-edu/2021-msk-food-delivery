import { Link } from 'react-router-dom';
import { Menu } from 'antd';

const { Item } = Menu;

const GuestMenu = () => {
  return (
      <Menu theme="dark" mode="horizontal">
        <Item key="home">
          <Link to="/">Домой</Link>
        </Item>
        <Item key="sign">
          <Link to="/signin" >
            Войти
          </Link>
        </Item>
      </Menu>
  );
}

export default GuestMenu;
