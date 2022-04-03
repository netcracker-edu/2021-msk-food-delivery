import {Form, Input} from "antd";

const PasswordInput = ({inputName, label}) => {

  return (
    <Form.Item name={inputName == null ? "password" : inputName}
      label={label == null ? "Пароль" : label}
      rules={[
        {
          required: true,
          message: 'Пожалуйста, введите Ваш пароль!',
        },
        {
          max: 64,
          message: 'Пароль слишком длинный!',
        },
        {
          min: 8,
          message: 'Пароль слишком короткий!',
        },
      ]}
      hasFeedback
    >
      <Input.Password placeholder="Введите Ваш пароль..."/>
    </Form.Item>
  );
}

export default PasswordInput;
