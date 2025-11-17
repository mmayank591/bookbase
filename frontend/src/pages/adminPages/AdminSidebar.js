import React from "react";
import { NavLink, useNavigate, useLocation } from "react-router-dom";
import "./CSS/AdminSidebar.css";
import white_logo_full from "../../resources/white_logo_full.png";
import { Book, Bookmark, Home, LogOut, Users } from "react-feather";
import { clearUserSession } from "../../utilities/SessionStorage";

export default function AdminSidebar() {
  const navigate = useNavigate();
  const location = useLocation();

  const isActiveForBorrowers = (match, location) => {
    // 1. Check if the current pathname is the main link
    const isBorrowersPath = location.pathname === "/adminborrowers";
    
    // 2. Check if the current pathname is the additional link (adminoverdue)
    const isOverduePath = location.pathname === "/adminoverdue";
    
    // The link is active if either condition is true
    return isBorrowersPath || isOverduePath;
  };

  const handleSignOut = () => {
    clearUserSession();
    navigate("/signin");
  };
  
  return (
    <aside className="adminsidebar">
      <div className="admintop">
        <img src={white_logo_full} alt="bookbase logo" />
      </div>

      <div className="adminmid">
        <NavLink
          to="/admindashboard"
          className={({ isActive }) =>
            isActive ? "active admin-link" : "admin-link"
          }
        >
          <Home />
          Dashboard
        </NavLink>

        {/* <NavLink
          to="/adminborrowers"
          className={({ isActive }) =>
            isActive ? "active admin-link" : "admin-link"
          }
        >
          <Bookmark />
          Borrowers
        </NavLink> */}

          <NavLink
        to="/adminborrowers" // The target path when clicked
        // Use the custom function in the className prop
        className={({ isActive }) =>
          isActiveForBorrowers(null, location) 
            ? "active admin-link" 
            : "admin-link"
        }
      >
        <Bookmark />
        Borrowers
    </NavLink>

        <NavLink
          to="/adminbooks"
          className={({ isActive }) =>
            isActive ? "active admin-link" : "admin-link"
          }
        >
          <Book />
          Books
        </NavLink>

        <NavLink
          to="/adminusers"
          className={({ isActive }) =>
            isActive ? "active admin-link" : "admin-link"
          }
        >
          <Users />
          Users
        </NavLink>
      </div>

      <div className="adminbottom">
        <button onClick={handleSignOut} className="logoutbtn">
          <LogOut /> Log out
        </button>
      </div>
    </aside>
  );
}
