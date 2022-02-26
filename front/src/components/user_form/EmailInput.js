import { Form, Input} from "antd";

const EmailInput = () => {
  return (
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
  );
}

export default EmailInput;
