import { Layout } from 'antd';
import ClientMenu from "./topMenu/ClientMenu.js";
import GuestMenu from "./topMenu/GuestMenu.js";
import AdminMenu from "./topMenu/AdminMenu.js";

const { Header } = Layout;

const HeaderCustom = ({ auth, address, setAddress }) => {
  const userRole = auth.token?.user.role;
  
  const renderSwitch = param => {
    switch (param) {
      case 'CLIENT':
        return (<ClientMenu auth={auth} address={address}
                  setAddress={setAddress}
        />);
      case 'ADMIN':
        return (<AdminMenu auth={auth} address={address}
                  setAddress={setAddress}
        />);
      default:
        return (<GuestMenu/>);
    };
  }

  return (
    <Header>
        {auth.token && userRole === 'CLIENT'
          ? renderSwitch(auth.token.user.role)
          : auth.token && userRole === 'COURIER' 
              ? 
                (<Menu theme="dark" mode="horizontal">
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
                        "SignOut"
                      </Link>
                    </Item> 
                  </Menu>
                )
              : renderSwitch('')
        }
    </Header>
  );
}

export default HeaderCustom;
