import {Form, Input} from "antd";

const FullNameInput = () => {
  return (
    <Form.Item name="fullName" label="Full name"

      rules={[
        {
          required: true,
          message: 'Please input your full name!',
          whitespace: true,
        },
        {
          max: 50,
          message: 'Full name is too long!',
        },
        {
          min: 6,
          message: 'Full name is too small!',
        },
      ]}
    >
      <Input placeholder="Full name"/>
    </Form.Item>
  );
}

export default FullNameInput;
