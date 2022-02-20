import ProfileClient from "../api/ProfileClient";
import {useState, useEffect} from 'react';
import { List, Card, Layout, Avatar, Menu, Dropdown, Modal} from 'antd';
import { EditOutlined, EllipsisOutlined, SettingOutlined } from '@ant-design/icons';

const { Meta } = Card;
const { Content } = Layout;

const Profile = ({auth}) => {
  const profileClient = new ProfileClient(auth);
  const [profile, setProfile] = useState({});
  const [isEditInfoVisible, setIsEditInfoVisible] = useState(false);
  const AVATAR_BASE = "http://localhost:8080/api/v1/file/";

  const editMenu = (
    <Menu>
      <Menu.Item onClick={(e) => setIsEditInfoVisible(true)}>
          Change Info
      </Menu.Item>
      <Menu.Item>
          Change E-mail
      </Menu.Item>
      <Menu.Item >
          Change Password
      </Menu.Item>
      <Menu.Item >
          Change Avatar
      </Menu.Item>
    </Menu>
  );

  const moreMenu = (
    <Menu>
      <Menu.Item>
          Orders History
      </Menu.Item>
      <Menu.Item>
          Full info
      </Menu.Item>
      <Menu.Item >
          Shoping Cart
      </Menu.Item>
    </Menu>
  );


  useEffect(() => {
    async function getProfile() {
      const response = await profileClient.get();
      if (response && response.success) {
        console.log(response.data);
        setProfile(response.data);
      } else {
        console.error(response.error);
      }
    }
    getProfile();
  }, []);

  return (
    <Content className="wrapper">
        <h1>{profile?.role} PROFILE</h1>
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
                            ? AVATAR_BASE+"62bbb602-08b0-4b60-b036-8e56c632f861"
                            : AVATAR_BASE + profile.avatarId}/>}
            title={profile?.fullName} description={profile?.email} />

        </Card>
        <Modal title="Edit common info"
               visible={isEditInfoVisible}
               onOk={() => setIsEditInfoVisible(false)}
               onCancel = {() => setIsEditInfoVisible(false)}>
          <p>Some contents...</p>
          <p>Some contents...</p>
          <p>Some contents...</p>
        </Modal>
    </Content>
  );
}

export default Profile;
