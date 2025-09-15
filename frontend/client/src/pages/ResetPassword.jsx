import React, { useContext, useRef, useState } from 'react'
import {Link, useNavigate} from 'react-router-dom'
import { AppContext } from '../context/AppContext'
import axios from 'axios';
import { toast } from 'react-toastify';

 import { FaEye, FaEyeSlash, FaLock, FaRegEnvelope } from 'react-icons/fa';
const ResetPassword = () => {
  const {backEndUrl} = useContext(AppContext);

  const navigate = useNavigate();

  const inputRef = useRef([]);
  const[loading,setLoading] = useState(false);
  const[isOtpSent,setIsOtpSent] = useState(false);
  const[otp,setOtp]=useState("");
  const[isOtpSubmitted,setIsOtpSubmitted]=useState(false);
  const[email,setEmail] = useState(false);
  const[newPassword,setNewpassword]= useState("");
  const[showPassword,setShowPassword] = useState(false);

   axios.defaults.withCredentials = true;

   const handleChange = (element, index) => {
    const value = element.value.replace(/[^0-9]/g, "");
    element.value = value;

    if(value && index<5){
      inputRef.current[index+1].focus();
    }
  };

  const handleKeyDown = (event,index)=>{
    console.log(event.key)
    if(event.key == 'Backspace' && !event.target.value && index>0){
      inputRef.current[index-1].focus();
    }
  }

  const handlePaste = (e)=>{
    e.preventDefault();
    console.log(e.clipboardData);
    const paste = e.clipboardData.getData("text").trim()
    .slice(0,7).split("");

    console.log(paste)
    paste.forEach((element,i) => {
      if(inputRef.current[i]){
        inputRef.current[i].value = element;
      }
    })

    const next = paste.length<6? paste.length:5;
    inputRef.current[next].focus();
  }


    const handleSendOtp = async (e)=>{
      setLoading(true)
      e.preventDefault();
      try{
        const response = await axios.post(`${backEndUrl}/send-reset-otp`,{},{
          params:{
            email:email
          },withCredentials:true
        });

        if(response.status === 200){
          toast.success("OTP is sent to your email");
          setIsOtpSent(true);
        
        }
        else{
          toast.error("Something Went Wrong");
        }
      }catch(err){
        toast.error(err.response?.data?.message || err.message || "Something went wrong");
      }finally{
        setLoading(false)
      }
    }
    const handleSetOtp = ()=>{
       const otpString = inputRef.current.map(input =>input.value).join('');

        if(otpString.length!=6){
          toast.error('Enter a 6 digit otp');
          return;
        }
        setOtp(otpString);
    }

    const handleResetPassword = async(e)=>{
      e.preventDefault();
      setLoading(true)
      try{
        const response = await axios.post(`${backEndUrl}/reset-password`,{
          email: email,
          otp : otp,
          newPassword : newPassword

        })
  
        if(response.status === 200){
          
          navigate('/');
          toast.success("Password has been reseted successfully");

          
        }
      }catch(err){
        toast.error(err.response?.data?.message || err.message || "Something went wrong");

      }finally{
        setLoading(false);
      }
    }
  return (
    <div className='flex h-screen min-h-[100vh] items-center justify-center bg-gradient-to-r  from-blue-700 via-blue-600 to-indigo-600 relative'>
      <div className="logo absolute md:left-11 md:top-7.5 top-5">
        <Link
          to="/"
          className="items-center flex font-bold gap-2 text-decoration-none"
        >
          <img src="/logo-w.png" width={30} height={30} alt="logo" />
          <span className="text-white font-bold text-2xl">Authify</span>
        </Link>
      </div>


      {!isOtpSent && (
        <div className="bg-white mx-4 w-[25em] md:w-[30em] rounded-xl text-center p-5">
          <p className="text-3xl font-bold">Forgot Password?</p>
          
          <form className="flex flex-col" onSubmit={(e) => handleSendOtp(e)} >
            <label htmlFor="" className="">Enter your registered email address</label><br />
            <div className="relative mt-3">
              <input
                type="email"
                required
                placeholder="Enter your email"
                className="w-full border rounded-pill bg-gray-100 text-black px-5 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 h-10"
                onChange={(e) => setEmail(e.target.value)}
              />
              <FaRegEnvelope className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" />
            </div>

            <button
              type="submit"
              className={`border rounded bg-gradient-to-b  bg-blue-600 hover:bg-blue-700 text-white transition active:scale-95 p-2 mt-5 w-full ${loading ? "cursor-not-allowed opacity-50" : "cursor-pointer"}`}
              disabled={loading}
            >
              Submit
            </button>
          </form>
        </div>
      )}
      {!isOtpSubmitted && isOtpSent &&(
        <div className="bg-white mx-4 w-[25em] md:w-[30em] rounded-xl text-center p-5">
          <p className="font-bold text-2xl">Email Verify OTP</p>
          <p className="mt-2 text-gray-600">Enter the 6-digit code sent to your email</p>

          <form onSubmit={(e)=>{e.preventDefault();setIsOtpSubmitted(true);handleSetOtp()}}>
            <div className="flex gap-2 justify-center p-4">
              {[...Array(6)].map((digit, index) => (
                <input
                  key={index}
                  type="number"
                  inputMode="numeric"
                  pattern="\d*"
                  maxLength={1}
                  value={digit}
                  ref = {(el)=>(inputRef.current[index] = el)}
                  onChange={(e) => handleChange(e.target, index)}
                  onKeyDown={(e)=>handleKeyDown(e,index)}
                  onPaste={handlePaste}
                  className="w-12 h-12 text-xl p-3 border border-gray-300 rounded-md text-center focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 no-spinner"
                  autoComplete="one-time-code"
                />
              ))}

            </div>

            <button
              type="submit"
              className={`rounded w-full h-10 cursor-pointer bg-gradient-to-b  bg-blue-600 hover:bg-blue-700 text-white transition active:scale-95 ${loading ? "cursor-not-allowed opacity-50" : "cursor-pointer"}`}
              disabled = {loading}
            >
              Verify
            </button>
          </form>
        </div>


      )}

      {isOtpSubmitted && isOtpSent && (
                <div className='bg-white mx-4  md:w-[30em] max-w-2xl rounded-xl text-center p-5'>
                  <p className='text-3xl font-bold'>New Password</p>
                  

                  <form className='flex flex-col justify-center items-center' onSubmit={(e)=>{handleResetPassword(e)}}>
                      <label htmlFor="" className="mb-3">Enter the new password below</label><br />
                    <div className="relative w-full max-w-80">
                      <input
                         type={showPassword?"text":"password"}
                        required
                        placeholder="Enter your new password"
                        className="w-full max-w-100 border rounded-pill bg-gray-100 text-black px-5 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 h-10    "
                        onChange={(e) => setNewpassword(e.target.value)}
                        autoComplete="new-password"
                      />
                      <FaLock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" />
                       {showPassword?<FaEye className="absolute top-2/7 right-4  cursor-pointer"onClick={()=>{setShowPassword(false)}}/>:<FaEyeSlash className="absolute top-2/7 right-4 cursor-pointer"onClick={()=>{setShowPassword(true)}}/>}
                      
                    </div>

                      <button
                        type="submit"
                        className={`rounded w-full max-w-90 h-10 mt-5  cursor-pointer  bg-blue-600 hover:bg-blue-700 text-white transition active:scale-95 ${loading ? "cursor-not-allowed opacity-50" : "cursor-pointer"}`}
                        disabled = {loading}
                      >
                       Submit
                      </button>
                    
                  </form>
                </div>
      )}
    </div>
  )
}

export default ResetPassword