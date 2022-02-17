import { Form, Input, Button, Checkbox, Alert } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useState } from 'react';
import { useNavigate, Link } from "react-router-dom";

const LoginForm = ({ auth }) => {
  const [login, setLogin] = useState();
  const [password, setPassword] = useState();
  const [errMsg, setErrMsg] = useState();
  const navigate = useNavigate();

  const handleFinish = async (values) => {
    console.log(values);
    const res = await auth.loginUser({
      login,
      password,
    });
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
    <div>
    <h1> LOGIN FORM </h1>
    <Form name="normal_login" className="login-form" onFinish={handleFinish}>
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
          placeholder="Login" onChange={(e) => setLogin(e.target.value)}
        />
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
          type="password" placeholder="Password"
          onChange={(e) => setPassword(e.target.value)}
        />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit" className="login-form-button">
          Log in
        </Button>
        Or <Link to="/signup" >register now!</Link>
      </Form.Item>
    </Form>
    </div>
  );
};

export default LoginForm;
