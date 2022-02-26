import { Form, Input } from "antd";

const PasswordInputConfirm = ({inputName, dependency}) => {
  const dependencies = dependency == null ? ['password'] : [dependency];

  return (
    <Form.Item name={inputName == null ? "confirm" : inputName}
      label="Confirm Password"
      dependencies={dependencies} hasFeedback
      rules={[
        {
          required: true,
          message: 'Please confirm your password!',
        },
        ({ getFieldValue }) => ({
          validator(_, value) {
            if (!value || getFieldValue(dependencies[0]) === value) {
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
  );
}

export default PasswordInputConfirm;
