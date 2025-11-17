import React from "react";
import   "./CSS/ActionBar.css";
import { Plus } from 'react-feather';

export default function ActionBar({ onCheckout, selectedCount, searchQuery, onSearchChange, }){
  return (
    <div className="actionBar">
      <div className="rightGroup">
        <button className="checkoutBtn" onClick={onCheckout} disabled={selectedCount === 0}>
          <Plus size={17}/>
          Checkout ({selectedCount})
        </button>
        <div className="search">
          <input placeholder="Search by Book Name" value={searchQuery} onChange={onSearchChange}/>
        </div>
      </div>
    </div>
  );
}