import useToken from "./hooks/useToken";

import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Layout } from 'antd';

import Auth from "./api/Auth";
import HeaderCustom from "./components/HeaderCustom";
import FooterCustom from "./components/FooterCustom";
import Home from "./components/Home";
import LoginForm from './components/LoginForm'
import NotFound from "./components/NotFound";
import Profile from "./components/Profile";
import Logout from "./components/Logout";
import RegistrationForm from "./components/RegistrationForm";
import OrderHistory from "./components/orderComponents/OrderHistory";
import OrderDetails from "./components/orderComponents/OrderDetails";

import './App.css';
import WarehouseList from "./components/warehouseComponents/WarehouseList";
import PendingOrders from "./components/warehouseComponents/PendingOrders";


function App() {
  const { token, setToken } = useToken();
  const auth = new Auth(token, setToken);
  const userRole = token?.user.role;

  return (
    <Layout className="App" style={{minHeight: "100vh"}}>
       <Router>
          <HeaderCustom auth={auth} user={token?.user}/>
          <Routes>
            <Route
              path="/"
              exact
              element={<Home auth={auth}/>}
            />
            <Route
              path="/profile"
              element={<Profile auth={auth}/>}
            />

            <Route path="/order" />          
            
            <Route path="/order/:orderId" element={<OrderDetails auth={auth} />}/>

            <Route
              path="/profile/orderHistory"
            >
              <Route index={true} element={<OrderHistory auth={auth}/>}/>
            </Route>

            <>{
              userRole === "ADMIN" || userRole === "MODERATOR" ?
              <Route path="/warehouses">
                {userRole === 'ADMIN' ?   
                  <Route 
                  index={true} element={<WarehouseList auth={auth}/>}
                  /> : <></>
                }                
                  <Route index={false} path=':warehouseId' element={<PendingOrders auth={auth} />} />
              </Route>
              : <></>  
              }
            </>

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
              element={<Logout auth={auth} />}
            />
            <Route path="*" element={<NotFound/>} />
          </Routes>
        <FooterCustom />
      </Router>
    </Layout>
  );
}

export default App;