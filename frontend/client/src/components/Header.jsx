import React, { useContext } from 'react'
import Button from './Button'
import { AppContext } from '../context/AppContext';


const Header = () => {

  const {userData} = useContext(AppContext);
  return (
    <div className="flex text-center flex-col items-center justify-center py-5 px-3 min-h-[80vh] min-w-100 gap-3">

      <h5 className="font-bold text-5xl">
        
        Hey {userData ? userData.name : 'Developer'} <span role='img' aria-label='wave'>👋</span>
      </h5>
      <p className='font-bold mb-3 text-5xl  '>Welcome to Our Product</p>

      <p className="text-muted text-xl mb-4 max-w-[500px]">
        Let's start with a quick product tour and you can setup the authentication in no time!
      </p>

      <Button msg ="Get Started"/>
    </div>
  )
}

export default Header
