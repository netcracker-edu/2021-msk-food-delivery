import useToken from "./hooks/useToken";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import { Layout } from 'antd';
import './App.css';
import Auth from "./api/Auth";
import HeaderCustom from "./components/HeaderCustom";
import FooterCustom from "./components/FooterCustom";
import Home from "./components/Home";
import LoginForm from './components/LoginForm'
import NotFound from "./components/NotFound";
import Profile from "./components/Profile";
import Logout from "./components/Logout";
import RegistrationForm from "./components/RegistrationForm";

function App() {
  const { token, setToken } = useToken();
  const auth = new Auth(token, setToken);

  return (
    <Layout className="App">
       <Router>
        <HeaderCustom auth={auth} />
          <Routes>
            <Route
              path="/"
              element={<Home auth={auth}/>}
            />
            <Route
              path="/profile"
              element={<Profile auth={auth}/>}
            />
            <Route
              path="/signin"
              element={<LoginForm auth={auth}/>}
            />
            <Route
              path="/signup"
              element={<RegistrationForm />}
            />
            <Route
              path="/signout"
              element={<Logout auth={auth}/>}
            />
            <Route path="*" element={<NotFound/>} />
          </Routes>
        <FooterCustom />
      </Router>
    </Layout>
  );
}

export default App;