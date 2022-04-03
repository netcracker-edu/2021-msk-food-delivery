import {useNavigate} from 'react-router-dom';
import {useEffect} from 'react';
import {updateCart, useCartContext } from "../hooks/CartContext";

const Logout = ({auth, setAddress}) => {
  const navigate = useNavigate();
  const { cartItems, dispatch } = useCartContext();

  const logout = async () => {
    if (auth.token) {
      let refreshToken = { "refreshToken" : auth.token.refreshToken };
      const response = await auth.logoutUser(refreshToken);
      return response;
    }
  }

  useEffect(() => {
    logout();
    setAddress({});
    dispatch(updateCart({}));
    navigate("/");
  });

  return (
    <div>
      Выйти
    </div>
  )
}

export default Logout;
