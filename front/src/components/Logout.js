import {useNavigate} from 'react-router-dom';
import {useEffect} from 'react';

const Logout = ({auth}) => {
  const navigate = useNavigate();
  const logout = async () => {
    if (auth.token) {
      let refreshToken = { "refreshToken" : auth.token.refreshToken };
      const response = await auth.logoutUser(refreshToken);
      return response;
    }
  }

  useEffect(() => {
    logout();
    navigate("/");
  });

  return (
    <div>
      logout
    </div>
  )
}

export default Logout;
