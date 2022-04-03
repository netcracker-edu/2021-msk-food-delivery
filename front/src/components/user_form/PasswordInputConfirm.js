import { Form, Input } from "antd";

const PasswordInputConfirm = ({inputName, dependency}) => {
  const dependencies = dependency == null ? ['password'] : [dependency];

  return (
    <Form.Item name={inputName == null ? "confirm" : inputName}
      label="Подтвердите пароль"
      dependencies={dependencies} hasFeedback
      rules={[
        {
          required: true,
          message: 'Пожалуйста, подтвердите Ваш пароль!',
        },
        ({ getFieldValue }) => ({
          validator(_, value) {
            if (!value || getFieldValue(dependencies[0]) === value) {
              return Promise.resolve();
            }
            return Promise.reject(
              new Error('Введённые пароли не совпадают!'));
          },
        }),
      ]}
    >
      <Input.Password placeholder="Повторите Ваш пароль..."/>
    </Form.Item>
  );
}

export default PasswordInputConfirm;
