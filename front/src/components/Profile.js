import ProfileClient from "../api/ProfileClient";
import {useState, useEffect} from 'react';
import {useNavigate} from "react-router-dom";
import { Card, Layout, Avatar, Menu, Dropdown, Modal, Form, Input} from 'antd';
import { EditOutlined, EllipsisOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import FullNameInput from './user_form/FullNameInput';
import PhoneInput from './user_form/PhoneInput';
import EmailInput from './user_form/EmailInput';
import PasswordInput from './user_form/PasswordInput';
import PasswordInputConfirm from './user_form/PasswordInputConfirm';

const { Meta } = Card;
const { Content } = Layout;
const roles = new Map();
roles.set("CLIENT", 'Клиент');
roles.set("COURIER", 'Курьер');
roles.set("MODERATOR", 'Модератор');
roles.set("ADMIN", 'Админ');

const Profile = ({auth}) => {
  const AVATAR_BASE = "http://localhost:8080/api/v1/file/";

  const profileClient = new ProfileClient(auth);
  const [profile, setProfile] = useState({});

  const [isEditInfoVisible, setIsEditInfoVisible] = useState(false);
  const [isEditEmailVisible, setIsEditEmailVisible] = useState(false);
  const [isEditPassVisible, setIsEditPassVisible] = useState(false);
  const [isEditAvatarVisible, setIsEditAvatarVisible] = useState(false);

  const [commonInfoForm] = Form.useForm();
  const [emailForm] = Form.useForm();
  const [passForm] = Form.useForm();
  const [avatarForm] = Form.useForm();
  const navigate = useNavigate();

  const editMenu = (
    <Menu>
      <Menu.Item key="info" onClick={(e) => setIsEditInfoVisible(true)}>
          Редактировать профиль
      </Menu.Item>
      <Menu.Item key="email" onClick={(e) => setIsEditEmailVisible(true)}>
          Изменить E-mail
      </Menu.Item>
      <Menu.Item key="password" onClick={(e) => setIsEditPassVisible(true)}>
          Изменить пароль
      </Menu.Item>
      <Menu.Item key="avatar" onClick={(e) => setIsEditAvatarVisible(true)}>
          Изменить аватар
      </Menu.Item>
    </Menu>
  );

  const moreMenu = (
    <Menu>
      <Menu.Item key="history">
          <Link to="/profile/orderHistory">История заказов</Link>
      </Menu.Item>
      <Menu.Item key="full">
          Полная информация
      </Menu.Item>
      <Menu.Item key="cart" onClick={() => navigate("/profile/cart")}>
          Корзина
      </Menu.Item>
    </Menu>
  );

  const editCommonInfo = async (values) => {
    const response = await profileClient.editCommonInfo(values);
    if (response && response.success) return true;
    return false;
  }

  const editEmail = async (values) => {
    const response = await profileClient.editEmail(values);
    if (response && response.success) return true;
    return false;
  }

  const editPassword = async (values) => {
    const response = await profileClient.editPassword(values);
    if (response && response.success) return true;
    return false;
  }

  const getProfile = async () => {
    const response = await profileClient.get();
    if (response && response.success) {
      setProfile(response.data);
    } else {
      console.error(response.error);
    }
  }

  useEffect(() => {
    getProfile();
  }, []);

  return (
    <Content className="wrapper">
      <h1>{roles.get(profile.role)}</h1>
      <Card
        style={{ width: 500 }}
        actions={[
          <Dropdown overlay={editMenu}>
            <EditOutlined key="edit"
                          className="ant-dropdown-link"
                          onClick={e => e.preventDefault()}/>
          </Dropdown>,
          <Dropdown overlay={moreMenu}>
            <EllipsisOutlined key="ellipsis"
                              className="ant-dropdown-link"
                              onClick={e => e.preventDefault()}/>
          </Dropdown>,
        ]}
      >
        <Meta
          avatar = {<Avatar size={100}
                    src={profile?.avatarId == null
                          ? AVATAR_BASE+"2b5c4886-093b-4054-8fd5-80a6751c1900"
                          : AVATAR_BASE + profile.avatarId}/>}
          title={profile?.fullName}
          description={
            <>
              {profile?.email}<br></br>{profile?.phoneNumber}
            </>
          }/>
      </Card>

      <Modal title="Редактировать профиль"
             visible={isEditInfoVisible}
             okText="Принять"
             cancelText="Отменить"
             onCancel = {() => setIsEditInfoVisible(false)}
             onOk={() => {
                    commonInfoForm.validateFields()
                      .then((values) => {
                        if (values.fullName == profile.fullName) {
                          delete values["fullName"];
                        }
                        if (values.phoneNumber == profile.phoneNumber) {
                          delete values["phoneNumber"];
                        }
                        editCommonInfo(values)
                          .then((res) => {
                            if (res) getProfile();
                          });
                        setIsEditInfoVisible(false);
                      })
                      .catch((info) => {
                        console.log('Validate Failed:', info);
                      });
                  }}>
        <Form name="commonInfo" scrollToFirstError
            form={commonInfoForm}
            initialValues = {
                {
                  "fullName": profile?.fullName,
                  "phoneNumber": profile?.phoneNumber,
                }}
        >
          <FullNameInput/>
          {profile?.phoneNumber != null ? <PhoneInput /> : ""}
        </Form>
      </Modal>

      <Modal title="Изменить email"
             visible={isEditEmailVisible}
             okText="Принять"
             cancelText="Отменить"
             onCancel = {() => {
                          setIsEditEmailVisible(false);
                          emailForm.resetFields();
                        }}
             onOk={() => {
                    emailForm.validateFields()
                      .then((values) => {
                        console.log(values);
                        if (values.email == profile.email) {
                          alert("Email не изменён!");
                          return;
                        }
                        editEmail(values)
                          .then((res) => {
                            emailForm.resetFields();
                            if (res) getProfile();
                          });
                        setIsEditEmailVisible(false);
                      })
                      .catch((info) => {
                        console.log('Validate Failed:', info);
                      });
                  }}
      >
        <Form name="emailEdit" scrollToFirstError
            form={emailForm}
            initialValues = {
                {
                  "email": profile.email,
                  "password": ""
                }}
        >
          <EmailInput />
          <PasswordInput inputName="password" label="Пароль" />
        </Form>
      </Modal>

      <Modal title="Изменить пароль"
             visible={isEditPassVisible}
             okText="Принять"
             cancelText="Отменить"
             onCancel = {() => {
                          passForm.resetFields();
                          setIsEditPassVisible(false);
                        }}
             onOk={() => {
                    passForm.validateFields()
                      .then((values) => {
                        passForm.resetFields();
                        editPassword(values);
                        setIsEditPassVisible(false);
                      })
                      .catch((info) => {
                        console.log('Validate Failed:', info);
                      });
                  }}
      >
        <Form name="passEdit" scrollToFirstError
            form={passForm}
        >
          <PasswordInput inputName="oldPassword" label="Старый пароль" />
          <PasswordInput inputName="newPassword" label="Новый пароль" />
          <PasswordInputConfirm dependency="newPassword" inputName="newPasswordConfirm"/>
        </Form>
      </Modal>

      <Modal title="Изменить аватар"
             visible={isEditAvatarVisible}
             okText="Принять"
             cancelText="Отменить"
             onCancel = {() => {
                          avatarForm.resetFields();
                          setIsEditAvatarVisible(false);
                        }}
             onOk={() => {
                    avatarForm.validateFields()
                      .then((values) => {
                        avatarForm.resetFields();
                        setIsEditAvatarVisible(false);
                      })
                      .catch((info) => {
                        console.log('Validate Failed:', info);
                      });
                  }}
      >
        <Form name="avatarEdit" scrollToFirstError
            form={avatarForm}
        >
            <Input name="file" type="file" placeholder="Выберите новый аватар" />
        </Form>
      </Modal>
    </Content>
  );
}

export default Profile;
