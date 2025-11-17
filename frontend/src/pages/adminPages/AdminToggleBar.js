import React from "react";
import { NavLink } from "react-router-dom";
import "./CSS/AdminToggleBar.css";

export default function AdminToggleBar() {
    return (
        <div className="admintoggle-bar">
        <div className="admintabs">
            <NavLink to="/adminborrowers" className={({ isActive }) => isActive ? "adminbactive" : "admintbb"}>
            Borrowers
            </NavLink>
            <NavLink to="/adminoverdue" className={({ isActive }) => isActive ? "adminractive" : "admintrb"}>
            Overdue
            </NavLink>
        </div>
        <div className="adminspacer">
        </div>
        <div className="adminsearch">
            <input type="text" placeholder="Search by ID" />
        </div>
        </div>
    );
}