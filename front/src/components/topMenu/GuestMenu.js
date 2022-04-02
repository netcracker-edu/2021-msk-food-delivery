import { Link } from 'react-router-dom';
import { Menu } from 'antd';

const { Item } = Menu;

const GuestMenu = () => {
  return (
      <Menu theme="dark" mode="horizontal">
        <Item key="home">
          <Link to="/">Home</Link>
        </Item>
        <Item key="sign">
          <Link to="/signin" >
            SignIn
          </Link>
        </Item>
      </Menu>
  );
}

export default GuestMenu;
