import React, { useState, useEffect } from "react";
import toast from "react-hot-toast";
import "./CSS/Signin.css";
import black_logo_icon from "../../resources/black_logo_icon.png";
import white_logo_full from "../../resources/white_logo_full.png";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { storeUserSession, clearUserSession } from "../../utilities/SessionStorage";
import setOverdueBooks from "../../utilities/setOverdue";
import { sendNotification } from "../../utilities/NotificationUtil";
import axios from "axios";

function Signin() {
  const navigate = useNavigate();
  const [credentials, setCredentials] = useState({
    userName: "",
    password: "",
  });
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  function decodeBase64Url(str) {
    let base64 = str.replace(/-/g, "+").replace(/_/g, "/");

    while (base64.length % 4) {
      base64 += "=";
    }

    return atob(base64);
  }

  useEffect(() => {
    clearUserSession();
  }, []);

  const handleSignin = async (e) => {
    e.preventDefault();
    setError(null);

    try {                                //http://localhost:8501/auth/login
      const response = await axios.post(`http://localhost:8080/auth/login`, credentials);

      const memberToken = response.data;
      const parts = memberToken.split(".");

      const payloadBase64 = parts[1];
      const decodedPayloadString = decodeBase64Url(payloadBase64);
      const payload = JSON.parse(decodedPayloadString);

      const userName = payload.userName;
      const memberID = payload.userID;
      const role = payload.userRole;

      const finalUserName = userName || payload.sub;

      storeUserSession(memberToken);

      if (memberID && role) {
        toast.success(`Welcome, ${finalUserName}!`, { duration: 5000 });
        storeUserSession(memberToken, finalUserName, memberID, role);

        if (role === "MEMBER") {
          navigate("/dashboard");
          await setOverdueBooks(memberID);
          await sendNotification(memberID);          
        } else if (role === "ADMIN") {
          navigate("/admindashboard");
        } else {
          navigate("/signin");
          console.log("Invalid Login", error);
        }
      } else {
        setError("Login failed!");
        toast.error(`Login failed!`, { duration: 3000 });
      }
    } catch (err) {
      console.error("Login Error:", err);

      let displayMessage = "An unexpected error occurred!";

      if (err.response) {
        if (err.response.status === 401 || err.response.status === 403) {
          displayMessage = `Unauthorized Access Request!`;
          setError(displayMessage);
        } else {
          displayMessage = `Invalid Credentials!`;
          setError(displayMessage);
        }
      } else if (err.request) {
        displayMessage = "Cannot connect to the server! Please ensure the server is running.";
        setError(displayMessage);
      }
      toast.error(displayMessage, { duration: 5000 });
    }
  };

  return (
    <motion.div
      className="container"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={{ duration: 0.3 }}
    >
      <div className="left">
        <div className="signinlogo">
          <img src={black_logo_icon} alt="Logo" />
        </div>
        <div className="welcome-text">
          <h1>
            <strong>WELCOME</strong>
          </h1>
          <p>Please enter your credentials to log in</p>
        </div>
        <div className="signin-creds">
          <form className="credentials" id="signinform" onSubmit={handleSignin}>
            <input
              id="signinusername"
              type="text"
              placeholder="Username"
              name="userName"
              value={credentials.userName}
              onChange={handleChange}
              required
            />
            <input
              id="signinpassword"
              type="password"
              placeholder="Password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              required
            />
            <div className="sign-in-button">
              <button type="submit">
                <strong>SIGN IN</strong>
              </button>
            </div>
          </form>
        </div>
      </div>

      <div className="right">
        <div className="bookbaselogo">
          <img src={white_logo_full} alt="Logo" />
        </div>
        <div className="new-user-text">
          <p>New to our platform? Sign Up now</p>
        </div>
        <div className="sign-up-button">
          <button onClick={() => navigate("/signup")}>
            <strong>SIGN UP</strong>
          </button>
        </div>
      </div>
    </motion.div>
  );
}

export default Signin;
