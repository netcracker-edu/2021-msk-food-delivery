import { Layout, Menu } from 'antd';
import { Link } from 'react-router-dom';
const { Header } = Layout;
const { Item } = Menu;

const HeaderCustom = ({ auth, profile }) => {
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
        { profile?.role === 'MODERATOR' ? 
          <Item key={3}>
            <Link to={`/warehouses/${profile.warehouseId}`}>Warehouse</Link>
          </Item>
          : profile?.role === 'ADMIN' ?
          <Item key={3}>
            <Link to="/warehouses">Warehouses</Link>
          </Item>
          : <></>          
        }
        <Item key={profile?.role === "COURIER" || profile?.role === "MODERATOR" || 
                   profile?.role === "ADMIN" ? 4 : 3}>
          <Link to={auth.token ? "/signout" : "/signin"}>
            {auth.token ? "SignOut" : "SignIn"}
          </Link>
        </Item>
      </Menu>
    </Header>
  );
}

export default HeaderCustom;
