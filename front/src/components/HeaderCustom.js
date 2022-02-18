import { Layout, Menu } from 'antd';
import { Link } from 'react-router-dom';
const { Header } = Layout;

const HeaderCustom = ({ auth }) => {
  return (
    <Header>
      <Menu theme="dark" mode="horizontal">
        <Menu.Item key="1">
          <Link to="/">Home</Link>
        </Menu.Item>
        {auth.token ? (
          <Menu.Item key="2">
            <Link to="/profile">Profile</Link>
          </Menu.Item>)
          : <></>
        }
        <Menu.Item key="3">
          <Link to={auth.token ? "/signout" : "/signin"}>
            {auth.token ? "SignOut" : "SignIn"}
          </Link>
        </Menu.Item>

      </Menu>
    </Header>
  );
}

export default HeaderCustom;
