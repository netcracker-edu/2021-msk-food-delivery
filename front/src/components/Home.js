import { Layout, Button } from 'antd';
import { Link } from 'react-router-dom';
const { Content } = Layout;

const Home = ({ auth }) => {
  const handleRefreshToken = async (event) => {
    let response = await auth.refreshToken();
    if (response.success) {
      console.log(response.data);
    } else {
      console.log(response.error);
    }
  }

  return (
      <Content className="wrapper">
        <h1>Привет, {auth.token ? auth.token.user.fullName : "гость"}!</h1>
        {auth?.token?.accessToken ?        
          <>
          <h3 style={{overflowWrap: 'break-word',}}>Текущий токен: {auth.token.accessToken}</h3> 
          <Button type = "primary"
          disabled = {auth.token ? false : true}
          onClick = {handleRefreshToken}>
            Обновить токен
          </Button>
          </>
          : <Link to="/signin">Войти/зарегистрироваться</Link>}

      </Content>
  );
}

export default Home;
