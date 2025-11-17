import React, { useState, useEffect, useRef } from "react";
import "./CSS/Navbar.css";
import "./CSS/UserNotification.css";
import { User, Bell, X, Settings, Circle } from "react-feather";
import { getUserName, getUserId, getUserRole } from "../../utilities/SessionStorage";
import apiClient from "../../api";
import toast from "react-hot-toast";

const USERNAME_REGEX = /^[a-zA-Z0-9_.-]{3,20}$/;
const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,30}$/;

export default function Navbar() {
  const [dateTime, setDateTime] = useState(new Date());
  const [userClaims, setUserClaims] = useState({
    userId: null,
    userName: "",
    userRole: "",
  });
  const [displayName, setDisplayName] = useState("");
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const notificationRef = useRef(null);
  const [showSettingsPopup, setShowSettingsPopup] = useState(false);
  const [newUserName, setNewUserName] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [popupError, setPopupError] = useState("");
  const [validationErrors, setValidationErrors] = useState({});
  const settingsPopupRef = useRef(null);

  useEffect(() => {
    const fetchUserData = async () => {
      const id = getUserId();
      const userName = getUserName();
      const role = getUserRole();

      if (id && userName && role) {
        setUserClaims({ userId: id, userName, userRole: role });

        try {
          const memberResponse = await apiClient.get(`/member/getbyid/${id}`);
          const memberData = memberResponse.data;

          if (memberData && memberData.name) {
            setDisplayName(memberData.name);
          } else {
            setDisplayName(userName);
          }
        } catch (error) {
          console.error("Error fetching member name by ID:", error);
          setDisplayName(userName);
        }
      } else {
        setUserClaims({ userId: null, userName: "", userRole: "" });
        setDisplayName("");
      }
    };
    fetchUserData();
  }, []);

  const validateField = (fieldName, value) => {
    let message = "";
    let isValid = true;

    if (fieldName === "newUserName" && value && !USERNAME_REGEX.test(value)) {
      message = "Username must be atleast 3 characters";
      isValid = false;
    } else if (
      fieldName === "newPassword" &&
      value &&
      !PASSWORD_REGEX.test(value)
    ) {
      message = "Password must be atleast 8 characters including a lowercase, an uppercase, a number, and a special character";
      isValid = false;
    }

    setValidationErrors((prev) => ({ ...prev, [fieldName]: message }));
    return isValid;
  };

  const handleInputChange = (e, setter, fieldName) => {
    const value = e.target.value;
    setter(value);
    validateField(fieldName, value);
  };

  useEffect(() => {
    const interval = setInterval(() => {
      setDateTime(new Date());
    }, 60000);
    setDateTime(new Date());
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    const fetchNotifications = async () => {
      const id = userClaims.userId;

      if (id) {
        try {
          const [notificationResponse, fineResponse] = await Promise.all([
            apiClient.get(`/notification/getbymemberid/${id}`),
            apiClient.get(`/fine/getbymemberid/${id}`),
          ]);

          const allNotifications = notificationResponse.data;
          const allFines = fineResponse.data;

          const dueFineTransactionIDs = new Set(
            allFines
              .filter((fine) => fine.status === "Due")
              .map((fine) => fine.transactionId)
          );

          const activeNotifications = allNotifications.filter((notification) =>
            dueFineTransactionIDs.has(notification.transactionID)
          );

          setNotifications(activeNotifications);
        } catch (err) {
          console.error("Error fetching notifications:", err);
          setNotifications([]);
        }
      } else {
        setNotifications([]);
      }
    };

    fetchNotifications();
  }, [userClaims.userId]);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        notificationRef.current &&
        !notificationRef.current.contains(event.target)
      ) {
        setShowNotifications(false);
      }
      if (
        settingsPopupRef.current &&
        !settingsPopupRef.current.contains(event.target) &&
        !event.target.closest(".settings-container")
      ) {
        setShowSettingsPopup(false);
        setPopupError("");
        setValidationErrors({});
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [notificationRef, settingsPopupRef]);

  const formattedTime = dateTime.toLocaleString(undefined, {
    hour: "2-digit",
    minute: "2-digit",
  });

  const dat = dateTime.toLocaleString(undefined, {
    year: "numeric",
    month: "short",
    day: "numeric",
  });

  const handleSettingsSubmit = async (e) => {
    e.preventDefault();
    setPopupError(null);
    setValidationErrors({});

    const isUsernameValid = validateField("newUserName", newUserName);
    const isPasswordValid = newPassword
      ? validateField("newPassword", newPassword)
      : true;

    if (!isUsernameValid || !isPasswordValid) {
      setPopupError("Please fix the validation errors before submitting");
      return;
    }

    if (newPassword && newPassword !== confirmPassword) {
      setPopupError("New Password and Confirm Password do not match");
      return;
    }

    const isChangingUsername =
      newUserName && newUserName !== userClaims.userName;

    try {
      const id = userClaims.userId;

      if (isChangingUsername) {
        try {
          await apiClient.get(`/member/getbyusername/${newUserName}`);

          setPopupError(`Username "${newUserName}" already exists. Please choose another.`);
          return;
        } catch (checkError) {
          if (checkError.response && checkError.response.status === 404) {
            console.log("Username is unique. Proceeding...");
          } else {
            console.error("Error checking username uniqueness:", checkError);
            setPopupError("Error checking username. Please try again later.");
            return;
          }
        }
      }

      const updatedDetails = {};
      if (isChangingUsername) {
        updatedDetails.userName = newUserName;
      }
      if (newPassword) {
        updatedDetails.password = newPassword;
      }

      if (Object.keys(updatedDetails).length === 0) {
        setShowSettingsPopup(false);
        return;
      }

      const response = await apiClient.patch(`/member/updatepatch/${id}`, updatedDetails);
      const updatedUser = response.data;

      setUserClaims((prev) => ({
        ...prev,
        userName: updatedUser.userName || prev.userName,
      }));

      setNewUserName(updatedUser.userName || userClaims.userName);
      setDisplayName(updatedUser.name || displayName);

      setNewPassword("");
      setConfirmPassword("");
      setValidationErrors({});

      setShowSettingsPopup(false);
      toast.success(`User details updated successfully!`, { duration: 5000 });
    } catch (error) {
      console.error("Error updating user details:", error);
      const errorMessage = error.response
        ? error.response.data.message || error.message
        : error.message;
      setPopupError("Failed to update. " + errorMessage);
    }
  };

  useEffect(() => {
    if (showSettingsPopup) {
      setNewUserName(userClaims.userName);
      setNewPassword("");
      setConfirmPassword("");
      setPopupError("");
      setValidationErrors({});
    }
  }, [showSettingsPopup, userClaims.userName]);

  return (
    <header className="navbar">
      <div className="navbar-left">
        <div className="user-info-container">
          <User size={35} />
          <div className="bracket">
            <p className="bracket-name">{displayName}</p>
            <p className="bracket-role">{userClaims.userRole}</p>
          </div>
        </div>
      </div>
      <div className="navbar-right">
        <div className="time-date-container">
          <span className="time">{formattedTime}</span>
          <span className="date">{dat}</span>
        </div>
        <div className="divider"></div>
        <div className="notification-container" ref={notificationRef}>
          <Bell
            size={25}
            onClick={() => setShowNotifications(!showNotifications)}
            className="notification-bell"
          />
          {notifications.length > 0 && (
            <span className="notification-dot"></span>
          )}
          {showNotifications && (
            <div className="notification-popup">
              <div className="popup-header">
                <h4>Notifications</h4>
                <button
                  className="close-popup"
                  onClick={() => setShowNotifications(false)}
                >
                  <X size={18} />
                </button>
              </div>
              <div className="popup-content">
                {notifications.length > 0 ? (
                  notifications.map((notification, index) => (
                    <div key={index} className="notification-item">
                      <Circle size={8} fill="red" color="red" />
                      <Circle size={8} fill="white" color="white" />
                      {notification.message}
                    </div>
                  ))
                ) : (
                  <div className="no-notifications">No new notifications</div>
                )}
              </div>
            </div>
          )}
        </div>
        <div className="settings-container">
          <Settings size={25} onClick={() => setShowSettingsPopup(true)} />
          {showSettingsPopup && (
            <div className="settings-popup-overlay">
              <div className="settings-popup" ref={settingsPopupRef}>
                <div className="popup-header">
                  <h4>Update Profile</h4>
                  <button
                    className="close-popup"
                    onClick={() => setShowSettingsPopup(false)}
                  >
                    <X size={18} />
                  </button>
                </div>
                <div className="popup-content">
                  <form
                    className="settings-form"
                    onSubmit={handleSettingsSubmit}
                  >
                    <div className="form-group">
                      <label htmlFor="username">Username:</label>
                      <input
                        type="text"
                        id="username"
                        value={newUserName}
                        onChange={(e) =>
                          handleInputChange(e, setNewUserName, "newUserName")
                        }
                        required
                        className="settings-input"
                      />
                      {validationErrors.newUserName && (
                        <p className="validation-error">
                          {validationErrors.newUserName}
                        </p>
                      )}
                    </div>
                    <div className="form-group">
                      <label htmlFor="password">New Password:</label>
                      <input
                        type="password"
                        id="password"
                        value={newPassword}
                        onChange={(e) =>
                          handleInputChange(e, setNewPassword, "newPassword")
                        }
                        className="settings-input"
                        required
                      />
                      {validationErrors.newPassword && (
                        <p className="password-error-message">
                          {validationErrors.newPassword}
                        </p>
                      )}
                    </div>
                    <div className="form-group">
                      <label htmlFor="confirm-password">
                        Confirm Password:
                      </label>
                      <input
                        type="password"
                        id="confirm-password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        className="settings-input"
                        required
                      />
                    </div>
                    {popupError && (
                      <p className="error-message">{popupError}</p>
                    )}
                    <div className="form-button">
                      <button type="submit" className="submit-button">
                        Update
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
