import { Layout, Menu, Badge, Typography } from 'antd';
import { Link } from 'react-router-dom';
import { useCartContext } from "../hooks/CartContext";

const { Header } = Layout;
const { Item } = Menu;
const { Text } = Typography;

const HeaderCustom = ({ auth }) => {
  const { cartItems } = useCartContext();

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
        {auth.token ? (
          <Item key={3}>
            <Link to="/products">Products</Link>
          </Item>)
          : <></>
        }
        {auth.token ? (
          <Item key={4}>
              <Link to="/profile/cart">
                Cart
                {
                  cartItems == null
                    ? <></>
                    : <Badge style={{margin:"0px 0px 20px 0px"}} size="small"
                          count={Object.keys(cartItems).length} />
                }

              </Link>
          </Item>)
          : <></>
        }
        <Item key={auth.token ? 5 : 2}>
          <Link to={auth.token ? "/signout" : "/signin"}>
            {auth.token ? "SignOut" : "SignIn"}
          </Link>
        </Item>
      </Menu>
    </Header>
  );
}

export default HeaderCustom;
