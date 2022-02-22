import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserClient from '../api/UserClient';
import EmailInput from './user_form/EmailInput';
import PasswordInput from './user_form/PasswordInput';
import FullNameInput from './user_form/FullNameInput';
import PhoneInput from './user_form/PhoneInput';
import RoleSelect from './user_form/RoleSelect';
import PasswordInputConfirm from './user_form/PasswordInputConfirm';
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
    //backend not support unknown fields
    delete values["confirm"];
    const response = await userClient.register(values);
    if (response && response.success) {
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
          <EmailInput/>
          <PasswordInput inputName="password" label="Password" />
          <PasswordInputConfirm dependency="password"/>
          <FullNameInput/>
          <PhoneInput />
          <RoleSelect mode="register" />

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
