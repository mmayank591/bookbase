import React, { useState, useEffect, useRef } from "react";
import "./CSS/AdminNavbar.css";
import { UserCheck, Settings, X } from 'react-feather';
import { storeUserSession, getUserName, getUserId, getUserRole } from "../../utilities/SessionStorage";
import apiClient from "../../api";
import toast from "react-hot-toast";

export default function AdminNavbar(){

    const [dateTime, setDateTime] = useState(new Date());
    const [userClaims, setUserClaims] = useState({ userId: null, userName: '', userRole: '' });
    const [userName, setUserName] = useState('');
    const [showSettingsPopup, setShowSettingsPopup] = useState(false);
    const [newUserName, setNewUserName] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [popupError, setPopupError] = useState('');
    const [displayName, setDisplayName] = useState('');
    const settingsPopupRef = useRef(null);

    useEffect(() => {
      const fetchData = async () => {
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
            setUserClaims({ userId: null, userName: '', userRole: '' });
            setDisplayName('');
          }
        };
      fetchData();
    }, []);

    useEffect(() => {
        const interval = setInterval(() => {
        setDateTime(new Date());
        }, 60000);

        setDateTime(new Date());

        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        const handleClickOutside = (event) => {
          if (settingsPopupRef.current && !settingsPopupRef.current.contains(event.target) && !event.target.closest('.settings-container')) {
            setShowSettingsPopup(false);
          }
        };
    
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
          document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [settingsPopupRef]);

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
    setPopupError('');

    if (newPassword && newPassword !== confirmPassword) {
      setPopupError("Passwords do not match.");
      return;
    }

    try {
      const id = userClaims.userId;

      const updatedDetails = {};

      if (newUserName && newUserName !== userClaims.userName) {
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

      setUserClaims(prev => ({ 
                ...prev, 
                userName: updatedUser.userName || prev.userName
            }));

      setNewUserName(updatedUser.userName || userClaims.userName);
      setDisplayName(updatedUser.name || displayName);
            
      setNewPassword('');
      setConfirmPassword('');
            
      setShowSettingsPopup(false);
      toast.success(`User details updated successfully!`, { duration: 5000 });
    } catch (error) {
      console.error("Error updating user details:", error);
      setPopupError("Failed to update. Please try again.");
    }
    };

    return (
        <header className="adminnavbar">
            <div className="adminnavbar-left">
                <div className="adminuser-info-container">
                <UserCheck size={35}/>
                    <div className="adminbracket">
                        <p className="adminbracket-name">{displayName}</p>
                        <p className="adminbracket-role">{userClaims.userRole}</p>
                    </div>
                </div>
            </div>

            <div className="adminnavbar-right">
                <div className="admintime-date-container">
                    <span className="admintime">{formattedTime}</span>
                    <span className="admindate">{dat}</span>
                </div>
                <div className="admindivider"></div>
                <div className="adminsettings-container">
                    <Settings size={25} onClick={() => setShowSettingsPopup(true)} />
                    {showSettingsPopup && (
                                <div className="adminsettings-popup-overlay">
                                  <div className="adminsettings-popup" ref={settingsPopupRef}>
                                    <div className="adminpopup-header">
                                      <h4>Update Profile</h4>
                                      <button className="adminclose-popup" onClick={() => setShowSettingsPopup(false)}>
                                        <X size={18} />
                                      </button>
                                    </div>
                                    <div className="adminpopup-content">
                                      <form className="adminsettings-form" onSubmit={handleSettingsSubmit}>
                                        <div className="adminform-group">
                                          <label htmlFor="username">Username:</label>
                                          <input
                                            type="text"
                                            id="username"
                                            placeholder={userClaims.userName}
                                            value={newUserName}
                                            onChange={(e) => setNewUserName(e.target.value)}
                                            className="adminsettings-input"
                                          />
                                        </div>
                                        <div className="adminform-group">
                                          <label htmlFor="password">New Password:</label>
                                          <input
                                            type="password"
                                            id="password"
                                            value={newPassword}
                                            onChange={(e) => setNewPassword(e.target.value)}
                                            className="adminsettings-input"
                                            required
                                          />
                                        </div>
                                        <div className="adminform-group">
                                          <label htmlFor="confirm-password">Confirm Password:</label>
                                          <input
                                            type="password"
                                            id="confirm-password"
                                            value={confirmPassword}
                                            onChange={(e) => setConfirmPassword(e.target.value)}
                                            className="adminsettings-input"
                                            required
                                          />
                                        </div>
                                        {popupError && <p className="adminerror-message">{popupError}</p>}
                                        <div className="adminform-button">
                                          <button type="submit" className="adminsubmit-button">Update</button>
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
