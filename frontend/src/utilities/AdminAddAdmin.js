import React, { useState } from "react";
import { X } from "react-feather";
import "./AdminAddAdmin.css";

export const AddAdminModal = ({ isOpen, onClose, onSubmit, newAdminData, setNewAdminData, isSubmitting, formError }) => {
    if (!isOpen) return null;

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewAdminData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Add New Admin</h2>
                    <button onClick={onClose} className="modal-close-button"><X size={24} /></button>
                </div>
                <form onSubmit={onSubmit} className="add-admin-form">
                    <div className="form-group">
                        <label htmlFor="name">Full Name</label>
                        <input type="text" id="name" name="name" value={newAdminData.name} onChange={handleInputChange} required />
                    </div>
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input type="email" id="email" name="email" value={newAdminData.email} onChange={handleInputChange} required />
                    </div>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input type="text" id="username" name="userName" value={newAdminData.userName} onChange={handleInputChange} required />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input type="password" id="password" name="password" value={newAdminData.password} onChange={handleInputChange} required />
                    </div>
                    {formError && <p className="form-error">{formError}</p>}
                    <button type="submit" className="submit-admin-button">
                        {isSubmitting ? 'Creating...' : 'Create Admin'}
                    </button>
                </form>
            </div>
        </div>
    );
};