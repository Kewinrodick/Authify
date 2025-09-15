
import './index.css'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'bootstrap/dist/js/bootstrap.bundle.js'

import {Routes,Route} from 'react-router-dom'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import EmailVerify from './pages/EmailVerify.jsx'
import ResetPassword from './pages/ResetPassword.jsx'

import {ToastContainer} from 'react-toastify';
const App = () => {
 
  return (
    <>
      <ToastContainer/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/email-verify" element={<EmailVerify/>}/>
        <Route path="/reset-password" element={<ResetPassword/>}/>  
      </Routes>
    </>
  )

}

export default App
