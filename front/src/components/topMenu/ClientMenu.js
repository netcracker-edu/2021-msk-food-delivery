import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Menu, Badge } from 'antd';
import { useCartContext } from "../../hooks/CartContext";
import ClientMapModal from "./ClientMapModal.js";

const { Item } = Menu;

const ClientMenu = ({auth, address, setAddress}) => {
  const { cartItems } = useCartContext();
  const [isMapVisible, setIsMapVisible] = useState(false);
  return (
      <>
      <Menu theme="dark" mode="horizontal">
        <Item key="home">
          <Link to="/">Домой</Link>
        </Item>
        <Item key="profile">
          <Link to="/profile">Профиль</Link>
        </Item>
        <Item key="products">
          <Link to="/products">Продукты</Link>
        </Item>
        <Item key="cart">
            <Link to="/profile/cart">
              Корзина
              {
                cartItems == null
                  ? <></>
                  : <Badge style={{margin:"0px 0px 20px 0px"}} size="small"
                        count={Object.keys(cartItems).length} />
              }

            </Link>
        </Item>
        <Item key="address" onClick={()=>setIsMapVisible(true)}>
          {address && Object.keys(address).length == 0
            ? <>Адрес</>
            : <>{address.shortAddress}</>
          }
        </Item>
        <Item key="sign">
          <Link to="/signout" >
            Выйти
          </Link>
        </Item>
      </Menu>
      <ClientMapModal auth={auth} isMapVisible={isMapVisible}
               setIsMapVisible={setIsMapVisible}
               address={address}  setAddress={setAddress}
      />
      </>
  );
}

export default ClientMenu;
