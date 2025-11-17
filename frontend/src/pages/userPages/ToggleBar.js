import React from "react";
import { NavLink } from "react-router-dom";
import "./CSS/ToggleBar.css";

export default function ToggleBar({ searchQuery, onSearchChange }) {
  return (
    <div className="toggle-bar">
      <div className="tabs">
        <NavLink to="/borrowed-books" className={({ isActive }) => isActive ? "bactive" : "tbb"}>
        Borrowed Books
        </NavLink>
        <NavLink to="/returned" className={({ isActive }) => isActive ? "ractive" : "trb"}>
        Returned Books
        </NavLink>
        <NavLink to="/overdue" className={({ isActive }) => isActive ? "oactive" : "orb"}>
        Overdue Books
        </NavLink>
      </div>
      <div className="spacer">
      </div>
      <div className="search">
          <input className="tinput" type="text" placeholder="Search by Name" value={searchQuery} onChange={onSearchChange}/>
      </div>
    </div>
  );
}