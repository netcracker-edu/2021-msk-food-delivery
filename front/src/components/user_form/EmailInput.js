import { Form, Input} from "antd";

const EmailInput = () => {
  return (
    <Form.Item name="email" label="E-mail"
      rules={[
        {
          type: 'email',
          message: 'Некорректный E-mail!',
        },
        {
          required: true,
          message: 'Пожалуйста, введите E-mail!',
        },
      ]}
    >
      <Input placeholder="example@domain.com"/>
    </Form.Item>
  );
}

export default EmailInput;
