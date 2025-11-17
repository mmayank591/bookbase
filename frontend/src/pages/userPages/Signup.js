import React, { useState, useMemo } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import "./CSS/Signup.css";
import black_logo_icon from "../../resources/black_logo_icon.png";
import white_logo_full from "../../resources/white_logo_full.png";
import { motion } from "framer-motion";
import axios from "axios";

const NAME_REGEX = /^[A-Za-z\s]{2,50}$/;
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const USERNAME_REGEX = /^[a-zA-Z0-9_-]{3,20}$/;
const PASSWORD_REGEX =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,30}$/;

function Signup() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const validateField = (fieldName, value) => {
    let message = "";
    let isValid = true;

    switch (fieldName) {
      case "name":
        if (!NAME_REGEX.test(value)) {
          message = "Full Name must be at least 2 characters";
          isValid = false;
        }
        break;
      case "email":
        if (!EMAIL_REGEX.test(value)) {
          message = "Please enter a valid email address";
          isValid = false;
        }
        break;
      case "username":
        if (!USERNAME_REGEX.test(value)) {
          message = "Username must be at least 3 characters";
          isValid = false;
        }
        break;
      case "password":
        if (!PASSWORD_REGEX.test(value)) {
          message =
            "Password must be at least 8 characters including a lowercase, an uppercase, a number and a special character";
          isValid = false;
        }
        break;
      default:
        break;
    }

    setValidationErrors((prev) => ({ ...prev, [fieldName]: message }));
    return isValid;
  };

  const handleInputChange = (e, setter, fieldName) => {
    const value = e.target.value;
    setter(value);
    validateField(fieldName, value);
  };

  const isFormValid = useMemo(() => {
    const allFieldsFilled =
      name.trim() !== "" &&
      email.trim() !== "" &&
      username.trim() !== "" &&
      password !== "" &&
      confirmPassword !== "";

    const passwordsMatch = password === confirmPassword;

    const hasValidationErrors = Object.values(validationErrors).some(
      (msg) => msg !== ""
    );

    return allFieldsFilled && passwordsMatch && !hasValidationErrors;
  }, [name, email, username, password, confirmPassword, validationErrors]);

  const handleSignup = async (e) => {
    e.preventDefault();
    setError(null);

    const isNameValid = validateField("name", name);
    const isEmailValid = validateField("email", email);
    const isUsernameValid = validateField("username", username);
    const isPasswordValid = validateField("password", password);

    if (!isNameValid || !isEmailValid || !isUsernameValid || !isPasswordValid) {
      setError("Please correct the validation errors before submitting");
      return;
    }

    if (password !== confirmPassword) {
      setError("Password and Confirm Password must match");
      return;
    }

    const newMember = {
      name: name,
      email: email,
      userName: username,
      password: password,
      role: "MEMBER",
      membershipStatus: "Active",
    };

    try {
      //http://localhost:8501/auth/register
      const response = await axios.post(
        `http://localhost:8080/auth/register`,
        newMember
      );

      if (response.status === 201 || response.status === 200) {
        toast.success(
          `Signup successful! Please sign in with your new credentials`,
          { duration: 5000 }
        );
        navigate("/signin");
      }
    } catch (err) {
      console.error("Signup Error:", err);

      let displayMessage = "An unexpected error occurred.";

      if (err.response) {
        const status = err.response.status;

        if (status === 409) {
          const message = err.response.data;
          if (typeof message === "string") {
            if (message.toLowerCase().includes("email")) {
              displayMessage = "An account with this email already exists!";
              setEmail("");
            } else if (message.toLowerCase().includes("username")) {
              displayMessage = "Username already taken! Please try a new one";
              setUsername("");
            } else {
              displayMessage = message;
            }
          } else {
            displayMessage = "Username or email already exists!";
          }
        } else if (status === 400) {
          displayMessage = "Invalid data provided! Check your inputs";
        } else {
          displayMessage = `Server Error: ${status}.`;
        }
      } else if (err.request) {
        displayMessage =
          "Cannot connect to the backend server. Please ensure the server is running.";
      }

      setError(displayMessage);
      toast.error(`${displayMessage}`, { duration: 5000 });
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
      <div className="login-section">
        <img src={white_logo_full} alt="Logo" className="logo-full" />
        <p>Already have an account? Sign In now.</p>
        <button className="sign-in" onClick={() => navigate("/signin")}>
          <strong>SIGN IN</strong>
        </button>
        {/* */}
      </div>

      <div className="signup-section">
        <img src={black_logo_icon} alt="Logo" className="logo-icon" />
        <h1>
          <strong>SIGN UP</strong>
        </h1>
        <p>Please provide your information to sign up.</p>

        <form id="signupform" onSubmit={handleSignup}>
          <div id="signupform-container">
            <input
              type="text"
              placeholder="Full Name"
              value={name}
              onChange={(e) => handleInputChange(e, setName, "name")}
            />
            {validationErrors.name && (
              <p className="error-message">{validationErrors.name}</p>
            )}

            <input
              type="text"
              placeholder="Email ID"
              value={email}
              onChange={(e) => handleInputChange(e, setEmail, "email")}
            />
            {validationErrors.email && (
              <p className="error-message">{validationErrors.email}</p>
            )}

            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => handleInputChange(e, setUsername, "username")}
            />
            {validationErrors.username && (
              <p className="error-message">{validationErrors.username}</p>
            )}

            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => handleInputChange(e, setPassword, "password")}
            />
            {validationErrors.password && (
              <p className="error-message">{validationErrors.password}</p>
            )}

            <input
              type="password"
              placeholder="Confirm Password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            {password !== confirmPassword && confirmPassword !== "" && (
              <p className="error-message">Passwords do not match</p>
            )}

            {error && <p className="error-message general-error">{error}</p>}

            <button
              type="submit"
              className="signup"
              id="signup"
              disabled={!isFormValid}
            >
              <strong>SIGN UP</strong>
            </button>
          </div>
        </form>
      </div>
    </motion.div>
  );
}

export default Signup;
