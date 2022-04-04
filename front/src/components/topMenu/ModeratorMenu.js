import { Link } from 'react-router-dom';
import { Menu } from 'antd';

const { Item } = Menu;

const ModeratorMenu = ({auth}) => {
  
  return (
      <>
      <Menu theme="dark" mode="horizontal">
        <Item key="home">
          <Link to="/">Домой</Link>
        </Item>
        <Item key="profile">
          <Link to="/profile">Профиль</Link>
        </Item>
        <Item key="warehouse">
            <Link to={`/warehouses/${auth.token?.user?.warehouseId}`}>Склад</Link>
        </Item>
        <Item key="sign">
          <Link to="/signout" >
            Выйти
          </Link>
        </Item>
      </Menu>
      </>
  );
}

export default ModeratorMenu;
