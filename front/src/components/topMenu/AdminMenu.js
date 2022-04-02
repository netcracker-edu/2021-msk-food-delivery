import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Menu, Badge } from 'antd';
import { useCartContext } from "../../hooks/CartContext";
import ClientMapModal from "./ClientMapModal.js";

const { Item } = Menu;

const AdminMenu = ({auth, address, setAddress}) => {
  const { cartItems } = useCartContext();
  const [isMapVisible, setIsMapVisible] = useState(false);
  return (
      <>
      <Menu theme="dark" mode="horizontal">
        <Item key="home">
          <Link to="/">Home</Link>
        </Item>
        <Item key="profile">
          <Link to="/profile">Profile</Link>
        </Item>
        <Item key="warehouses">
            <Link to="/warehouses">Warehouses</Link>
          </Item>
        <Item key="products">
          <Link to="/products">Products</Link>
        </Item>
        <Item key="cart">
            <Link to="/profile/cart">
              Cart
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
            ? <>Address</>
            : <>{address.shortAddress}</>
          }
        </Item>
        <Item key="sign">
          <Link to="/signout" >
            SignOut
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

export default AdminMenu;
