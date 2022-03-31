import { Layout } from 'antd';
import ClientMenu from "./topMenu/ClientMenu.js";
import GuestMenu from "./topMenu/GuestMenu.js";
import AdminMenu from "./topMenu/AdminMenu.js";

const { Header } = Layout;

const HeaderCustom = ({ auth, address, setAddress }) => {

  const renderSwitch = param => {
    switch (param) {
      case 'CLIENT':
        return (<ClientMenu auth={auth} address={address}
                  setAddress={setAddress}
        />);
        break;
      case 'ADMIN':
        return (<AdminMenu auth={auth} address={address}
                  setAddress={setAddress}
        />);
        break;
      default:
        return (<GuestMenu/>);
    };
  }

  return (
    <Header>
        {auth.token
          ? renderSwitch(auth.token.user.role)
          : renderSwitch('')
        }
    </Header>
  );
}

export default HeaderCustom;
