import { Layout } from 'antd';
import ClientMenu from "./topMenu/ClientMenu.js";
import GuestMenu from "./topMenu/GuestMenu.js";
import AdminMenu from "./topMenu/AdminMenu.js";
import ModeratorMenu from './topMenu/ModeratorMenu.js';
import CourierMenu from './topMenu/CourierMenu.js';

const { Header } = Layout;

const HeaderCustom = ({ auth, address, setAddress }) => {
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
      case 'MODERATOR':
        return (<ModeratorMenu auth={auth} address={address}
                 setAddress={setAddress}
        />);
      case 'COURIER':
        return (<CourierMenu/>);

      default:
        return (<GuestMenu/>);
    };
  }

  return (
    <Header>
        {
          renderSwitch(auth?.token?.user.role)
        }
    </Header>
  );
}

export default HeaderCustom;
