import { useState } from "react";
import apiClient from "../api";
import toast from "react-hot-toast";
import './AdminAddUsers.css';

export const AddUserModal = ({ isOpen, onClose, onUserAdded }) => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('MEMBER');

    if (!isOpen) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newUser = {
            name: name,
            email: email,
            userName: username,
            password: password,
            role: role,
            membershipStatus: "Active",
        };

        try {
            const response = await apiClient.post(`/member/createnew`, newUser);

            console.log("New user added successfully:", await response.data);
            toast.success(`New User Added: ${newUser.name}`, { duration: 5000 });
            onUserAdded(); 
            onClose(); 
        } catch (error) {
            console.error("Error adding user:", error);
            toast.error(`Error Adding User: ${error}`, { duration: 4000 });
        }
    };

    return (
        <div className="adminaddusers-modal-overlay">
            <div className="adminaddusers-modal-content">
                <div className="adminaddusers-modal-header">
                    <h2>Add New User</h2>
                    <button className="adminaddusers-modal-close-btn" onClick={onClose}>&times;</button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="adminaddusers-modal-form-group">
                        <label>Name:</label>
                        <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
                    </div>
                    <div className="adminaddusers-modal-form-group">
                        <label>Email:</label>
                        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    </div>
                    <div className="adminaddusers-modal-form-group">
                        <label>Username:</label>
                        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                    </div>
                    <div className="adminaddusers-modal-form-group">
                        <label>Password:</label>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    <div className="adminaddusers-modal-form-group">
                        <label>Role:</label>
                        <select id="adminusers-select" value={role} onChange={(e) => setRole(e.target.value)}>
                            <option value="Member">MEMBER</option>
                            <option value="Admin">ADMIN</option>
                        </select>
                    </div>
                    <div className="adminaddusers-modal-actions">
                        <button type="submit" className="adminaddusers-modal-submit-btn">Add User</button>
                    </div>
                </form>
            </div>
        </div>
    );
};