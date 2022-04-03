import { Form, Input, Button, Alert, Layout, Card } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useState } from 'react';
import { useNavigate, Link } from "react-router-dom";
import ProfileClient from '../api/ProfileClient';
const { Content } = Layout;


const LoginForm = ({ auth}) => {
  const [errMsg, setErrMsg] = useState();
  const navigate = useNavigate();

  const profileClient = new ProfileClient(auth);

  const handleFinish = async (values) => {
    const res = await auth.loginUser(values);
    if (res && res.success) {
      setErrMsg(null);
      navigate("/");
    } else {
      setErrMsg(
        res && typeof res === "string" ? res : "Неправильный логин/пароль"
      );
    }
  };

  return (
    <Content className="wrapper">
      <h1> Вход </h1>
      <Card>
        <Form name="loginForm" onFinish={handleFinish}>
          <span style={{display: errMsg ? "block" : "none"}}>
            <Alert message={errMsg} type="error" />
          </span>
          <Form.Item name="login"
            rules={[
              {
                required: true,
                message: 'Пожалуйста, введите Ваш логин!',
              },
            ]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Логин"/>
          </Form.Item>
          <Form.Item name="password"
            rules={[
              {
                required: true,
                message: 'Пожалуйста, введите Ваш пароль!',
              },
            ]}
          >
            <Input prefix={<LockOutlined className="site-form-item-icon" />}
              type="password" placeholder="Пароль"/>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Войти
            </Button>
            <br></br>
            или <Link to="/signup" >зарегистрируйтесь сейчас!</Link>
          </Form.Item>
        </Form>
      </Card>
    </Content>
  );
};

export default LoginForm;
