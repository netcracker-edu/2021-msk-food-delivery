import {Form, Select} from "antd";
const {Option} = Select;

const RoleSelect = ({mode}) => {

  const register = ['Клиент', 'Курьер'];
  const add = ['Клиент', 'Курьер', 'Админ', 'Модератор'];
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
    <Form.Item name="role" label="Роль"
      rules={[
        {
          required: true,
          message: 'Пожалуйста, выберите роль!',
        },
      ]}
    >
      <Select placeholder="Выберите роль">
        {mode === "register" ? getOptions(register) : getOptions(add)}
      </Select>
    </Form.Item>
  );
}

export default RoleSelect;
