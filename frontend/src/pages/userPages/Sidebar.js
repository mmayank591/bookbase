import React from "react";
import { NavLink, useNavigate, useLocation } from "react-router-dom";
import "./CSS/Sidebar.css";
import white_logo_full from "../../resources/white_logo_full.png";
import { Compass, List, Home, LogOut } from "react-feather";
import { clearUserSession } from "../../utilities/SessionStorage";

export default function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  const activePaths = ["/borrowed-books", "/returned", "/overdue"];
  const shouldBeActive = () => {
    return activePaths.includes(location.pathname);
  };  

  const handleSignOut = () => {
    clearUserSession();
    navigate("/signin");
  };

  return (
    <aside className="sidebar">
      <div className="top">
        <img src={white_logo_full} alt="bookbase logo" />
      </div>

      <div className="mid">
        <NavLink
          to="/dashboard"
          className={({ isActive }) =>
            isActive ? "nav-link active" : "nav-link"
          }
        >
          <Home />
          Dashboard
        </NavLink>
        <NavLink
          to="/borrowed-books"
          className={() =>
          shouldBeActive() ? "nav-link active" : "nav-link"}>
      <List />
      My Library
    </NavLink>

        <NavLink
          to="/catalogue"
          className={({ isActive }) =>
            isActive ? "nav-link active" : "nav-link"}>
          <Compass />
          Catalogue
        </NavLink>
      </div>

      <div className="bottom">
        <button onClick={handleSignOut} className="logoutbtn">
          <LogOut /> Log Out
        </button>
      </div>
    </aside>
  );
}
