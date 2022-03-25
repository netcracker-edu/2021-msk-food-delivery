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
        { auth.token && profile?.role === 'COURIER' ? 
          <Item key={3}>
            <Link to="/deliverySessions">Deliveries</Link>
          </Item>
          : <></>
        }
        <Item key={auth.token ? (profile?.role === "COURIER" ? 4 : 3) : 2}>
          <Link to={auth.token ? "/signout" : "/signin"}>
            {auth.token ? "SignOut" : "SignIn"}
          </Link>
        </Item>
      </Menu>
    </Header>
  );
}

export default HeaderCustom;
