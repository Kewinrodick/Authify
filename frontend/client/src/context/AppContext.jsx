import { createContext, useState, useEffect ,useMemo, useCallback} from "react";

import { AppConstants } from "../util/constants";
import { toast } from "react-toastify";
import axios from 'axios';


export const AppContext = createContext();

export const AppContextProvider = (props) => {
    
    const backEndUrl = AppConstants.BACKEND_URL;
    const [isLogedIn,setIsLogedIn] = useState(false);
    const[userData,setUserData]= useState(null);
    const[isUserVerified,setIsUserVerified] = useState(false);

    const getUserData = useCallback(async () => {
    try {
        const response = await axios.get(backEndUrl + "/profile", {
            withCredentials: true,
        });
        if (response.status === 200) {
            const user = response.data.data;
                    console.log(user);
            setUserData(user);
            setIsUserVerified(user.isAccountVerified);
            setIsLogedIn(true);
        }
    } catch (err) {
        if (err.response) {
            if (err.response.status === 401) {
                // Expected if user is not logged in. Do nothing.
                console.log("No active session — user not logged in.");
            } else {
                // Unexpected error, show notification
                toast.error(err.response.data?.message || err.message || "Something went wrong");
            }
        } else {
            toast.error(err.message || "Something went wrong");
        }
        setIsLogedIn(false);
        setUserData(null);
    }
}, []);


    useEffect(() => {
            getUserData();
    }, []);


    const contextValue = useMemo(() => ({
        backEndUrl,
        isLogedIn,
        setIsLogedIn,
        userData,
        setUserData,
        getUserData,
        isUserVerified,
        setIsUserVerified
    }), [backEndUrl, isLogedIn, userData, isUserVerified]);

    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    );
}