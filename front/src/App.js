import logo from './logo.svg';
import './App.css';
import { DatePicker } from 'antd';
import LoginForm from './components/LoginForm'
import Auth from "./api/Auth";
import useToken from "./hooks/useToken";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import NotFound from "./components/NotFound";
import Home from "./components/Home";
import Logout from "./components/Logout";
import Profile from "./components/Profile";
import Header from "./components/Header";
import RegistrationForm from "./components/RegistrationForm";


function App() {
  const { token, setToken } = useToken();
  const auth = new Auth(token, setToken);

  return (
    <div className="App">
       <Router>
        <Header auth={auth} />
        <Routes>
          <Route path="/" element={<Home auth={auth}/>}/>
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
      </Router>
    </div>
  );
}

export default App;
