import React, { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../index.css";
import axios from "axios";
import { AppContext } from "../context/AppContext";
import { toast } from "react-toastify";

 import { FaEye, FaEyeSlash, FaLock, FaRegEnvelope } from 'react-icons/fa';

const Login = () => {
  const [isCreateAccount, setIsCreateAccount] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const[showPassword,setShowPassword] = useState(false);
  const[showConfirmPassword,setShowConfirmPassword] = useState(false);

  const [loading, setLoading] = useState(false);

  const { backEndUrl, setIsLogedIn ,getUserData } = useContext(AppContext);
  const navigate = useNavigate();

  const onSubmitHandler = async (e) => {
    e.preventDefault();

    if (isCreateAccount && confirmPassword !== password) {
      toast.error("Confirm password does not match");
      return;
    }

    axios.defaults.withCredentials = true;
    setLoading(true);

    try {
      if (isCreateAccount) {
        
        const response = await axios.post(`${backEndUrl}/register`, {
          name,
          email,
          password,
        });
        console.log(response.data);
        if (response.status === 201) {
          setIsCreateAccount(false);
          navigate("/");
          toast.success("Account created successfully");
        } else {
          toast.error("Email already exists");
        }
      } else {
        // Login API
        const response = await axios.post(`${backEndUrl}/login`, {
          email,
          password,
        });

        if (response.status === 200) {
          setIsLogedIn(true);
          getUserData();
          navigate("/");
          toast.success("Login successful");
        }
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Something went wrong");
    } finally {
      setLoading(false);
    }
  };
   
  return (
    <div className="flex h-screen items-center justify-center bg-gradient-to-r  from-blue-700 via-blue-600 to-indigo-600 relative">
      <div className="logo absolute md:left-11 md:top-7.5 top-5">
        <Link
          to="/"
          className="items-center flex font-bold gap-2 text-decoration-none"
        >
          <img src="/logo-w.png" width={30} height={30} alt="logo" />
          <span className="text-white font-bold text-2xl">Authify</span>
        </Link>
      </div>

      <div className="w-80 min-w-30 max-w-100 md:w-100 flex flex-col gap-3 bg-white rounded-md shadow-2xl p-4">
        <p className="text-center font-bold text-3xl">
          {isCreateAccount ? "Create Account" : "Login"}
        </p>

        <form
          className="flex flex-col md:gap-3"
          onSubmit={onSubmitHandler}
        >
    
          {isCreateAccount && (
            <div className="mb-3 flex flex-col gap-2">
              <label className="font-semibold">Name</label>
              <input
                type="text"
                autoComplete="name"
                className="form-control border-2 border-gray-200 rounded-md px-2 py-1 w-full"
                required
                placeholder="Enter name"
                onChange={(e) => setName(e.target.value)}
              />
            </div>
          )}

  
          <div className="mb-3 flex flex-col gap-2">
            <label className="font-semibold">Email Id</label>
            <input
              type="email"
              autoComplete="email"
              className="form-control border-2 border-gray-200 rounded-md px-2 py-1 w-full"
              required
              placeholder="Enter email"
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>


          <div className="mb-3 flex flex-col gap-2">
            <label className="font-semibold">Password</label>

            <div className="relative">
              <input
                type={showPassword?"text":"password"}
                autoComplete="password"
                className="form-control border-2 border-gray-200 rounded-md px-2 py-1 w-full "
                placeholder="Enter password"
                onChange={(e) => setPassword(e.target.value)}
              />
              {showPassword?<FaEye className="absolute top-2/7 right-4  cursor-pointer"onClick={()=>{setShowPassword(false)}}/>:<FaEyeSlash className="absolute top-2/7 right-4 cursor-pointer"onClick={()=>{setShowPassword(true)}}/>}
              
            </div>

            {!isCreateAccount && (
              <div>
                <Link
                  to="/reset-password"
                  className="text-blue-600 text-sm hover:text-blue-700 text-decoration-none"
                  
                >
                  Forgot password?
                </Link>
              </div>
            )}
          </div>

          
          {isCreateAccount && (
            <div className="mb-3 flex flex-col gap-2">
              <label className="font-semibold">Confirm password</label>

            <div className="relative">
              <input
                type={showConfirmPassword?"text":"password"}
                className="form-control border-2 border-gray-200 rounded-md px-2 py-1 w-full"
                placeholder="Confirm password"
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
                {showConfirmPassword?<FaEye className="absolute top-2/7 right-4  cursor-pointer"onClick={()=>{setShowConfirmPassword(false)}}/>
                :<FaEyeSlash className="absolute top-2/7 right-4 cursor-pointer"onClick={()=>{setShowConfirmPassword(true)}}/>}
            </div>
            </div>
          )}

          
          <button
            type="submit"
            className="btn btn-primary bg-blue-600 text-white rounded-md py-2 hover:bg-blue-700" disabled={loading}
          >
            {loading
              ? "Loading...": isCreateAccount? "Create Account":"Login"}
          </button>

          {!isCreateAccount ? (
            <div className="text-center">
              Don't have an account?{" "}
              <Link
                to="/login"
                className="text-blue-600 text-sm hover:text-blue-700 text-decoration-none"
                onClick={() => setIsCreateAccount(true)}
              >
                Register
              </Link>
            </div>
          ) : (
            <div className="text-center">
              Already have an account?{" "}
              <Link
                to="/login"
                className="text-blue-600 text-sm hover:text-blue-700 text-decoration-none"
                onClick={() => setIsCreateAccount(false)}
              >
                Login
              </Link>
            </div>
          )}
        </form>
      </div>
    </div>
  );
};

export default Login;
