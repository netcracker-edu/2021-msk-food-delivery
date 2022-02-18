import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserClient from '../api/UserClient';
import {
  Form, Input, Select, Button, Alert, Layout, Card
} from 'antd';
const { Option } = Select;
const { Content } = Layout;

const RegistrationForm = () => {
  const [errMsg, setErrMsg] = useState();
  const userClient = new UserClient();
  const navigate = useNavigate();

  const handleFinish = async (values) => {
    delete values["confirm"];
    const response = await userClient.register(values);
    if (response && response.success) {
       console.log(response.data);
       setErrMsg(null);
       navigate("/signin");
    } else {
      setErrMsg("Registration error");
    }
  }

  return (
    <Content className='wrapper'>
      <h1>REGISTRATION FORM</h1>
      <span style={{display: errMsg ? "block" : "none"}}>
        <Alert message={errMsg} type="error"/>
      </span>
      <Card>
      <Form name="register" scrollToFirstError
        onFinish={handleFinish}
      >
        <Form.Item name="email" label="E-mail"
          rules={[
            {
              type: 'email',
              message: 'The input is not valid E-mail!',
            },
            {
              required: true,
              message: 'Please input your E-mail!',
            },
          ]}
        >
          <Input placeholder="example@domain.com"/>
        </Form.Item>

        <Form.Item name="password" label="Password"
          rules={[
            {
              required: true,
              message: 'Please input your password!',
            },
            {
              max: 64,
              message: 'Password too small!',
            },
            {
              min: 8,
              message: 'Password too small!',
            },
          ]}
          hasFeedback
        >
          <Input.Password placeholder="Enter your password..."/>
        </Form.Item>

        <Form.Item name="confirm" label="Confirm Password"
          dependencies={['password']} hasFeedback
          rules={[
            {
              required: true,
              message: 'Please confirm your password!',
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('password') === value) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error('The two passwords that you entered do not match!'));
              },
            }),
          ]}
        >
          <Input.Password placeholder="Repeat your password..."/>
        </Form.Item>

        <Form.Item name="fullName" label="Full name"
          rules={[
            {
              required: true,
              message: 'Please input your full name!',
              whitespace: true,
            },
          ]}
        >
          <Input placeholder="Full name"/>
        </Form.Item>

        <Form.Item name="phoneNumber" label="Phone Number"
          tooltip="Phone format +7 XXX XXX-XX-XX"
          rules={[
            {
              required: false,
              message: 'Please input your phone number!',
            },
          ]}
        >
          <Input placeholder="+7 XXX XXX-XX-XX"/>
        </Form.Item>

        <Form.Item name="role" label="Role"
          rules={[
            {
              required: true,
              message: 'Please select role!',
            },
          ]}
        >
          <Select placeholder="select your role">
            <Option value="CLIENT">Client</Option>
            <Option value="COURIER">Courier</Option>
          </Select>
        </Form.Item>

        <Form.Item >
          <Button type="primary" htmlType="submit" size="large">
            Register
          </Button>
        </Form.Item>
      </Form>
      </Card>
    </Content>
  );
}

export default RegistrationForm;
