import ProfileClient from "../api/ProfileClient";
import {useState, useEffect} from 'react';
import { List, Card, Layout, Avatar} from 'antd';
import { EditOutlined, EllipsisOutlined, SettingOutlined } from '@ant-design/icons';

const { Meta } = Card;
const { Content } = Layout;

const Profile = ({auth}) => {
  const profileClient = new ProfileClient(auth);
  const [profile, setProfile] = useState({});

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
            <EditOutlined key="edit" onClick={(e) => alert("GOTCHA")}/>,
            <EllipsisOutlined key="ellipsis" />,
          ]}
        >
          <Meta
            avatar = {<Avatar size={100}
                      src="http://localhost:8080/api/v1/file/62bbb602-08b0-4b60-b036-8e56c632f861"/>}
            title={profile?.fullName} description={profile?.email} />

        </Card>
    </Content>
  );
}

export default Profile;
