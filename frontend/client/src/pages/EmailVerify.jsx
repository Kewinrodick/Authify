import React, { useContext, useState, useRef } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Button from '../components/Button';
import { AppContext } from '../context/AppContext';
import axios from 'axios';
import { toast } from 'react-toastify';

const EmailVerify = () => {
  const { backEndUrl ,setIsUserVerified} = useContext(AppContext);
  const navigate = useNavigate();

 
 
  const inputRef = useRef([]);
  const [loading,setLoading]  = useState(false);

  const verifyEmail = async (e) => {
  e.preventDefault();
  try {
   
    const otpString = inputRef.current.map(input => input.value).join('');

    if (otpString.length !== 6) {
      toast.error("Please enter a 6-digit OTP.");
      return;
    }

    setLoading(true);
    axios.defaults.withCredentials = true;
    
    const response = await axios.post(
      `${backEndUrl}/verify-otp`,
      { otp: otpString },
      { withCredentials: true }
    );
    if(response.status === 200){
      setIsUserVerified(true);
      toast.success("Email is verified successfully!");
      navigate("/");
    }
  } catch (err) {
    toast.error(err.response?.data?.message || err.message);
  } finally {
    setLoading(false);
  }
};


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


  return (
    <div className="flex flex-col min-h-screen items-center justify-center bg-gradient-to-r from-blue-700 via-blue-600 to-blue-500 overflow-hidden">
      <div className="logo absolute md:left-11 md:top-7.5 top-5">
        <Link to="/" className="items-center flex font-bold gap-2 text-decoration-none">
          <img src="/logo-w.png" width={30} height={30} alt="logo" />
          <span className="text-white font-bold text-2xl">Authify</span>
        </Link>
      </div>

      <div className="bg-white mx-4 w-[25em] md:w-[30em] rounded-xl text-center p-5">
        <p className="font-bold text-2xl">Email Verify OTP</p>
        <p className="mt-2 text-gray-600">Enter the 6-digit code sent to your email</p>

        <form onSubmit={(e)=>verifyEmail(e)}>
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
            className="rounded-pill w-32 h-10 cursor-pointer bg-gradient-to-b from-blue-700 via-blue-500 to-blue-400 text-white transition active:scale-95"
            disabled = {loading}
          >
            {loading?"Verifying...":"Verify"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default EmailVerify;
