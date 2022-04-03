import {Form, Input} from "antd"

const PhoneInput = () => {

  return (
    <Form.Item name="phoneNumber" label="Номер телефона"
      tooltip="Формат номера: +7 (XXX) XXX-XX-XX"
      rules={[
        {
          required: false,
          message: 'Пожалуйста, введите Ваш номер!',
        },
      ]}
    >
      <Input placeholder="+7 (XXX) XXX-XX-XX"/>
    </Form.Item>
  );
}

export default PhoneInput;
