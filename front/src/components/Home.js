import { Layout, Button } from 'antd';
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
        <h1>Hello from React, {auth.token ? auth.token.user.fullName : "Guest"}!</h1>
        <h3 style={{overflowWrap: 'break-word',}}>YOUR TOKEN IS {auth.token ? auth.token.accessToken : "nothing"}</h3>
        <Button type = "primary"
          disabled = {auth.token ? false : true}
          onClick = {handleRefreshToken}>
            Refresh token
        </Button>
      </Content>
  );
}

export default Home;
