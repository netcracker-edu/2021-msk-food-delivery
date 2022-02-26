import {Form, Select} from "antd";
const {Option} = Select;

const RoleSelect = ({mode}) => {

  const register = ['Client', 'Courier'];
  const add = ['Client', 'Courier', 'Admin', 'Moderator'];
  const getOptions = (names) => {
    return (
      <>
        {names.map(name => (
          <Option value={name.toUpperCase()} key={name}>{name}</Option>
        ))}
      </>
    );
  };

  return (
    <Form.Item name="role" label="Role"
      rules={[
        {
          required: true,
          message: 'Please select role!',
        },
      ]}
    >
      <Select placeholder="select your role">
        {mode === "register" ? getOptions(register) : getOptions(add)}
      </Select>
    </Form.Item>
  );
}

export default RoleSelect;
