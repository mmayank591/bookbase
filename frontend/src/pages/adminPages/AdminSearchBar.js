import React from "react";
import "./CSS/AdminSearchBar.css"

export default function AdminSearchBar({ searchQuery, onSearchChange }) {
    return (
        <div className="adminsearch-bar">
        <div className="admintabs">
        </div>
        <div className="adminspacer"></div>
        <div className="adminsearch">
            <input type="text" placeholder="Search by Name" value={searchQuery} onChange={onSearchChange} />
        </div>
        </div>
    );
}