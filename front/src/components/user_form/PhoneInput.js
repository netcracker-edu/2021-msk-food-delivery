import {Form, Input} from "antd"

const PhoneInput = () => {

  return (
    <Form.Item name="phoneNumber" label="Phone Number"
      tooltip="Phone format +7 (XXX) XXX-XX-XX"
      rules={[
        {
          required: false,
          message: 'Please input your phone number!',
        },
      ]}
    >
      <Input placeholder="+7 (XXX) XXX-XX-XX"/>
    </Form.Item>
  );
}

export default PhoneInput;
