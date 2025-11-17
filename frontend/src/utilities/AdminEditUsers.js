import { useState, useEffect } from "react";
import "./AdminEditUsers.css";
import apiClient from "../api";
import toast from "react-hot-toast";

export const EditUserModal = ({ isOpen, onClose, user, onUserUpdated }) => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [role, setRole] = useState('');
    const [membershipStatus, setMembershipStatus] = useState('');

    useEffect(() => {
        if (user) {
            setName(user.name);
            setEmail(user.email);
            setUsername(user.userName);
            setRole(user.role);
            setMembershipStatus(user.membershipStatus);
        }
    }, [user]);

    if (!isOpen || !user) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();

        const updatedUser = {
            ...user,
            name: name,
            email: email,
            userName: username,
            role: role,
            membershipStatus: membershipStatus,
        };

        try {
            const response = await apiClient.put(`/member/updateput/${user.memberID}`, updatedUser);

            console.log("User updated successfully:", await response.data);
            toast.success(`User details updated successfully!`, { duration:4000 });
            onUserUpdated();
            onClose();
        } catch (error) {
            console.error("Error updating user:", error);
            toast.error(`Failed to update user!`, { duration:4000 });
        }
    };

    return (
        <div className="admineditusers-modal-overlay">
            <div className="admineditusers-modal-content">
                <div className="admineditusers-modal-header">
                    <h2>Edit User Details</h2>
                    <button className="admineditusers-modal-close-btn" onClick={onClose}>&times;</button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="admineditusers-modal-form-group">
                        <label>Name:</label>
                        <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
                    </div>
                    <div className="admineditusers-modal-form-group">
                        <label>Email:</label>
                        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    </div>
                    <div className="admineditusers-modal-form-group">
                        <label>Username:</label>
                        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                    </div>
                    <div className="admineditusers-modal-form-group">
                        <label>Role:</label>
                        <select value={role} onChange={(e) => setRole(e.target.value)}>
                            <option value="Member">Member</option>
                            <option value="Admin">Admin</option>
                        </select>
                    </div>
                    <div className="admineditusers-modal-form-group">
                        <label>Membership Status:</label>
                        <select value={membershipStatus} onChange={(e) => setMembershipStatus(e.target.value)}>
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                        </select>
                    </div>
                    <div className="admineditusers-modal-actions">
                        <button type="submit" className="admineditusers-modal-submit-btn">Update</button>
                    </div>
                </form>
            </div>
        </div>
    );
};