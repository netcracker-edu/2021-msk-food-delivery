import {Form, Input} from "antd";

const FullNameInput = () => {
  return (
    <Form.Item name="fullName" label="Полное имя"

      rules={[
        {
          required: true,
          message: 'Пожалуйста, введите полное имя!',
          whitespace: true,
        },
        {
          max: 50,
          message: 'Имя слишком длинное!',
        },
        {
          min: 6,
          message: 'Имя слишком короткое!',
        },
      ]}
    >
      <Input placeholder="Полное имя"/>
    </Form.Item>
  );
}

export default FullNameInput;
