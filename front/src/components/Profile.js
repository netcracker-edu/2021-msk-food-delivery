import ProfileClient from "../api/ProfileClient";
import {useState, useEffect} from 'react';
import {Row, Col, List, Card} from 'antd';

const { Meta } = Card;

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
    <Row justify="center">
      <Col>
        <h1>{profile?.role} PROFILE</h1>
        <Card
          hoverable
          style={{ width: 300 }}
          cover={<img height="400px" alt="example"
                  src = "http://localhost:8080/api/v1/file/62bbb602-08b0-4b60-b036-8e56c632f861"/>}
        >
          <Meta title={profile?.fullName} description={profile?.email} />

        </Card>
      </Col>
    </Row>
  );
}

export default Profile;
