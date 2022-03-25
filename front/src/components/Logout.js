import {useNavigate} from 'react-router-dom';
import {useEffect} from 'react';
import useProfile from '../hooks/useProfile';

const Logout = ({auth}) => {
  const navigate = useNavigate();
  const {setProfile} = useProfile();
  const logout = async () => {
    setProfile(null);
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
