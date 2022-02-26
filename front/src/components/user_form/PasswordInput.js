import {Form, Input} from "antd";

const PasswordInput = ({inputName, label}) => {

  return (
    <Form.Item name={inputName == null ? "password" : inputName}
      label={label == null ? "Password" : label}
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
  );
}

export default PasswordInput;
