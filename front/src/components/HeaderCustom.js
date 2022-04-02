import { Layout, Menu } from 'antd';
import { Link } from 'react-router-dom';
const { Header } = Layout;
const { Item } = Menu;

const HeaderCustom = ({ auth }) => {
  const user = auth.token?.user;
  return (
    <Header>
      <Menu theme="dark" mode="horizontal">
        <Item key={1}>
          <Link to="/">Home</Link>
        </Item>
        {auth.token ? (
          <Item key={2}>
            <Link to="/profile">Profile</Link>
          </Item>)
          : <></>
        }
        { user?.role === 'MODERATOR' ? 
          <Item key={3}>
            <Link to={`/warehouses/${user.warehouseId}`}>Warehouse</Link>
          </Item>
          : user?.role === 'ADMIN' ?
          <Item key={3}>
            <Link to="/warehouses">Warehouses</Link>
          </Item>
          : <></>          
        }
        <Item key={user?.role === "COURIER" || user?.role === "MODERATOR" || 
                   user?.role === "ADMIN" ? 4 : 3}>
          <Link to={auth.token ? "/signout" : "/signin"}>
            {auth.token ? "SignOut" : "SignIn"}
          </Link>
        </Item>
      </Menu>
    </Header>
  );
}

export default HeaderCustom;
