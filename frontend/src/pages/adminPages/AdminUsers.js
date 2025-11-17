import React, { useState, useEffect, useCallback } from "react";
import AdminSidebar from "./AdminSidebar";
import AdminNavbar from "./AdminNavbar";
import "./CSS/AdminUsers.css";
import { Edit, Trash, PlusSquare } from 'react-feather';
import apiClient from "../../api";
import toast from "react-hot-toast";
import AdminSearchBar from "./AdminSearchBar";

import { AddUserModal } from '../../utilities/AdminAddUsers';
import { EditUserModal } from '../../utilities/AdminEditUsers';
import { handleDeleteUser } from '../../utilities/AdminDeleteUsers';

export default function AdminUsers() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');

    
        const fetchUsers = useCallback(async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await apiClient.get(`/member/getallusers`);
                
                const usersData = response.data;
                setUsers(usersData);
            } catch (err) {
                console.error("Error fetching users:", err);
                setError("An error occurred while fetching users.");
                toast.error(`Failed to fetch users!`, { duration:4000 });
            } finally {
                setLoading(false);
            }
        }, []);

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    const handleEditClick = (user) => {
        setCurrentUser(user);
        setIsEditModalOpen(true);
    };

    const handleDeleteClick = (userId) => {
        handleDeleteUser(userId, fetchUsers);
    };

    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    // const filteredUsers = users.filter((user) => {
    //     if (!user || user.name === undefined || user.name === null) {
    //         return false;
    //     }
        
    //     return user.name.includes(searchQuery.trim())
    // });

    const filteredUsers = users.filter((user) =>
    user?.name?.toLowerCase().includes(searchQuery.trim().toLowerCase())
  );

    // if (loading) {
    //     return console.log();
    // }

    if (error) {
        return console.log(error);
    }

    return (
        <div className="adminusersappLayout">
            <AdminSidebar />

            <div className="adminusersappmain">
                <AdminNavbar />
                <AdminSearchBar searchQuery={searchQuery} onSearchChange={handleSearchChange} />

                <div className="adminusersappcontentRow">
                    <div className="adminusersapptableCard">
                        <div className="adminusersappHeader">
                            <div className="adminusersappTitle">All Users List</div>
                            <div className="adminusersappIcon"><PlusSquare size={28} onClick={() => setIsAddModalOpen(true)} /></div>
                        </div>
                        <div className="adminusersapptableWrap">
                            <table className="adminusersTable">
                                <thead>
                                    <tr>
                                        <th><center>Member ID</center></th>
                                        <th><center>Name</center></th>
                                        <th><center>Email</center></th>
                                        <th><center>Role</center></th>
                                        <th><center>Membership Status</center></th>
                                        <th><center>Action</center></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {filteredUsers.length > 0 ? (
                                        filteredUsers.map((user) => (
                                            <tr key={user.memberID}>
                                                <td>{user.memberID}</td>
                                                <td>{user.name}</td>
                                                <td>{user.email}</td>
                                                <td>{user.role}</td>
                                                <td>{user.membershipStatus}</td>
                                                <td><Edit size={20} onClick={() => handleEditClick(user)} /> <Trash size={20} onClick={() => handleDeleteClick(user.memberID)} /></td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="6" className="adminusersappno-data-message">No users found</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <AddUserModal isOpen={isAddModalOpen} onClose={() => setIsAddModalOpen(false)} onUserAdded={fetchUsers} />
            <EditUserModal isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)} user={currentUser} onUserUpdated={fetchUsers} />
        </div>
    );
}