import {Layout} from 'antd';
import { CopyrightOutlined } from '@ant-design/icons';
const {Footer} = Layout;

const FooterCustom = () => {
  return (
    <Footer className="footer">
      ncedu/food-delivery <CopyrightOutlined /> {new Date().getFullYear()}
    </Footer>
  );
}

export default FooterCustom;
