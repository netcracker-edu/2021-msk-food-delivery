import { Form, Input} from "antd";

const EmailInput = ({initValue}) => {
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
      <Input placeholder="example@domain.com"
        value={initValue == null ? "" : initValue}/>
    </Form.Item>
  );
}

export default EmailInput;
