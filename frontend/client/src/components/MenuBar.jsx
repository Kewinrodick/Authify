import React, { useContext, useEffect, useRef, useState } from 'react' 
import { useNavigate } from 'react-router-dom'; 
import Button from './Button'; import { AppContext } from '../context/AppContext'; 
import axios from 'axios'; 
import { toast } from 'react-toastify';
const MenuBar = () => { 
  const{userData,backEndUrl} = useContext(AppContext); 
const navigate = useNavigate();

 const {isLogedIn , setIsLogedIn , setUserData,isUserVerified} = useContext(AppContext);
  const[dropDown,setDropDown] = useState(false); 
  
  const dropDownRef = useRef(null);


  const sendVerifyOtp = async()=>{
      try{
        const response = await axios.post(`${backEndUrl}/send-otp`,{},{withCredentials:true});

        if(response.status === 200){
          toast.success("OTP is sent to your email");
        }
        else{
          toast.error("Something Went Wrong");
        }
      }catch(error){
        toast.error(error.message);
      }
  }
   const onLogoutHandler = async() =>{ 
    try{ 
      const response = await axios.post(`${backEndUrl}/logout`,{},{withCredentials:true});
       setIsLogedIn(false); 
       setUserData(null); 
       toast.success("Logged out successfully"); 
      }
    catch(err){
       toast.error(err.message); 
    } 
  }

  useEffect(()=>{
    function handleClick(e){
      if(dropDownRef.current && !dropDownRef.current.contains(e.target)){
        setDropDown(false);
      }
    }

    document.addEventListener("mousedown",handleClick);

    return(()=>{
      document.removeEventListener("mousedown",handleClick);
    })

  },[])
       return ( 
       <nav className='navbar mx-5 my-2'> 
          <div className="flex items-center gap-2">
             <img src="\public\favicon.png" width={32} height={32} alt="logo" /> 
                <span className='fw-bold fs-4 text-dark'>Authify</span> 
          </div> 
          
          {userData && isLogedIn?
             <div className='relative' ref={dropDownRef}>
               <div className="bg-dark text-white rounded-full flex justify-center items-center w-[40px] h-[40px] cursor-pointer user-select-none"
                 onClick={()=>{setDropDown((prev)=>!prev)}} > {userData.name[0].toUpperCase()} 
                </div> 
                
                {dropDown && (
                     <div className='absolute shadow bg-white rounded p-2 top-[50px] right-0 z-20'>
                         {!isUserVerified && (
                          
                          <div className='dropdown-item py-1 px-2 cursor-pointer'
                            onClick={()=>{
                              sendVerifyOtp()
                              navigate("/email-verify")
                            }
                            }>
                              Verify Email 
                          </div>
                         )}
                        <div className='dropdown-item py-1 px-2 cursor-pointer text-danger' 
                          onClick={onLogoutHandler}> 
                          Logout 
                        </div>
                     </div> 
                )} 
              </div>    
              :  
              <div onClick={() => navigate('/login')}> 
                <Button msg="Login" /> 
              </div> 
              } 
                  
                </nav> 
    ) 
} 
                
  export default MenuBar