import React, { useState, useEffect } from "react";
import AdminSidebar from "./AdminSidebar";
import AdminNavbar from "./AdminNavbar";
import AdminDashboardChart from "./AdminDashboardChart";
import "./CSS/AdminDashboard.css"; 
import { User, Database, Layers, Circle, PlusSquare, Activity, AlertCircle } from 'react-feather';
import apiClient from "../../api";
import { getUserId } from "../../utilities/SessionStorage";
import { AddAdminModal } from "../../utilities/AdminAddAdmin";
import toast from "react-hot-toast";

export default function AdminDashboardPage() {
    const [totalUsers, setTotalUsers] = useState(0);
    const [totalBooks, setTotalBooks] = useState(0);
    const [totalTransactions, setTotalTransactions] = useState(0);
    const [overdueBooksCount, setOverdueBooksCount] = useState(0);
    const [admins, setAdmins] = useState([]);
    const [overdueBorrowers, setOverdueBorrowers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const loggedInUserId = getUserId();
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [formError, setFormError] = useState(null);
    const [newAdminData, setNewAdminData] = useState({
        name: '',
        email: '',
        userName: '',
        password: ''
    });

    
    const fetchData = async () => {
            setLoading(true);
            setError(null);
            
            try {
                const [ membersResponse, booksResponse, transactionsResponse] = await Promise.all([
                    apiClient.get(`/member/getallusers`),
                    apiClient.get(`/book/getallbooks`),
                    apiClient.get(`/transaction/getalltransactions`)
                ]);


                const members = membersResponse.data;
                const books = booksResponse.data;
                const transactions = transactionsResponse.data;


                setTotalUsers(members.length);
                setTotalBooks(books.length);
                setTotalTransactions(transactions.length);

                
                const adminUsers = members.filter(member => member.role === 'ADMIN');
                setAdmins(adminUsers);

                
                const today = new Date();

                const overdueTransactions = transactions.filter(t => {
                    return t.status === 'Overdue';
                });

                setOverdueBooksCount(overdueTransactions.length);

                
                const uniqueOverdueMemberIds = [...new Set(overdueTransactions.map(t => t.memberID))];
                
                
                const overdueMembersDetails = uniqueOverdueMemberIds.map(memberId => {
                    
                    const member = members.find(m => m.memberID === memberId);
                    
                    const overdueBookCount = overdueTransactions.filter(t => t.memberID === memberId).length;
                    return member ? { ...member, overdueBookCount } : null;
                }).filter(Boolean); 

                setOverdueBorrowers(overdueMembersDetails);

            } catch (err) {
                console.error("Error fetching dashboard data:", err);
                setError("Failed to load dashboard data.");
            } finally {
                setLoading(false);
            }
        };

        useEffect(() => {
            fetchData();
        }, []);

    const openModal = () => setIsModalOpen(true);
    
    const closeModal = () => {
        setIsModalOpen(false);
        setFormError(null); 
        setNewAdminData({ name: '', email: '', userName: '', password: '' }); 
    };

    const handleAdminSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setFormError(null);

        try {
            
            const adminPayload = { ...newAdminData, role: "ADMIN", membershipStatus: "Active" };

            const response = await apiClient.post(`/member/createnew`, adminPayload);
            console.log(response);
            toast.success(`New admin, ${adminPayload.name} added!`, { duration:5000 });
            
            closeModal();
            fetchData(); 

        } catch (err) {
            setFormError(err.message);
            toast.error(`Failed! Please try again later`, { duration:4000 });
        } finally {
            setIsSubmitting(false);
        }
    };

    // if (loading) {
    //     return console.log();
    // }

    if (error) {
        return console.log(error);
    }

    return (
        <>
        <div className="adminappLayout">
            <AdminSidebar />
            <div className="adminmain">
                <AdminNavbar />
                <div className="admindashboard-content">
                    <div className="admindashboard-cards">
                        <div className="admincard ctu">
                            <Database size={40} className="admincard-icon tu" />
                            <div className="admincard-info">
                                <h2 className="htu">{String(totalUsers)}</h2>
                                <p>Total User Base</p>
                            </div>
                        </div>
                        <div className="admincard ctb">
                            <Layers size={40} className="admincard-icon tb" />
                            <div className="admincard-info">
                                <h2 className="htb">{String(totalBooks)}</h2>
                                <p>Total Book Count</p>
                            </div>
                        </div>
                        <div className="admincard cts">
                            <AlertCircle size={40} className="admincard-icon ts" />
                            <div className="admincard-info">
                                <h2 className="hts">{String(overdueBooksCount)}</h2>
                                <p>Total Overdue Books</p>
                            </div>
                        </div>
                        <div className="admincard ctq">
                            <Activity size={40} className="admincard-icon tq" />
                            <div className="admincard-info">
                                <h2 className="htq">{String(totalTransactions)}</h2>
                                <p>Total Transactions</p>
                            </div>
                        </div>
                    </div>

                    
                    <div className="admindashboard-lists">
                        
                        <div className="admin-list-card">
                            <div className="admin-list-header">
                                <div className="adminlist-card-title">Admins</div>
                                
                                    <div style={{display : "flex"} }>
                                        <div style={{marginTop : "5px"} }>Add Admin</div>
                                <div className="admin-plus" onClick={openModal}>
                                    <PlusSquare size={20} />
                                    </div>
                                    </div>
                                    
                            </div>
                            {admins.map(admin => (
                                <div className="admin-item" key={admin.memberID}>
                                    <div className="admin-info">
                                        {String(admin.memberID) === loggedInUserId ? (
                                            <User size={20} color="green" />
                                        ) : (<User size={20}/>)}
                                        <span>{admin.userName}</span>
                                        <p>Admin ID: {admin.memberID}</p>
                                        {String(admin.memberID) === loggedInUserId && (
                                            <Circle size={9} color="green" fill="green"/>
                                        )}
                                    </div>
                                </div>
                            ))}
                            </div>
                            <AdminDashboardChart totalUsers={totalUsers} totalBooks={totalBooks} overdueBooks={overdueBooksCount} />
                    </div>
                </div>
            </div>
        </div>
        <AddAdminModal isOpen={isModalOpen} onClose={closeModal} onSubmit={handleAdminSubmit} newAdminData={newAdminData} setNewAdminData={setNewAdminData} isSubmitting={isSubmitting} formError={formError} />
        </>
    );
}