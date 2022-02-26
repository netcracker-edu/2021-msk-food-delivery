import { Form, Input, Button, Alert, Layout, Card } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useState } from 'react';
import { useNavigate, Link } from "react-router-dom";
const { Content } = Layout;


const LoginForm = ({ auth }) => {
  const [errMsg, setErrMsg] = useState();
  const navigate = useNavigate();

  const handleFinish = async (values) => {
    const res = await auth.loginUser(values);
    if (res && res.success) {
      setErrMsg(null);
      navigate("/");
    } else {
      setErrMsg(
        res && typeof res === "string" ? res : "Invalid Username/Paasword"
      );
    }
  };

  return (
    <Content className="wrapper">
      <h1> LOGIN FORM </h1>
      <Card>
        <Form name="loginForm" onFinish={handleFinish}>
          <span style={{display: errMsg ? "block" : "none"}}>
            <Alert message={errMsg} type="error" />
          </span>
          <Form.Item name="login"
            rules={[
              {
                required: true,
                message: 'Please input your Login!',
              },
            ]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Login"/>
          </Form.Item>
          <Form.Item name="password"
            rules={[
              {
                required: true,
                message: 'Please input your Password!',
              },
            ]}
          >
            <Input prefix={<LockOutlined className="site-form-item-icon" />}
              type="password" placeholder="Password"/>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Log in
            </Button>
            <br></br>
            or <Link to="/signup" >register now!</Link>
          </Form.Item>
        </Form>
      </Card>
    </Content>
  );
};

export default LoginForm;
