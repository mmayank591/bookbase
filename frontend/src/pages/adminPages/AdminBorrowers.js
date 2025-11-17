import React, { useState, useEffect } from "react";
import AdminSidebar from "./AdminSidebar";
import AdminNavbar from "./AdminNavbar";
import AdminToggleBar from "./AdminToggleBar";
import "./CSS/AdminBorrowers.css"; 
import apiClient from "../../api";
import toast from "react-hot-toast";

export default function AdminBorrowers() {
    const [borrowers, setBorrowers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBorrowers = async () => {
            setLoading(true);
            setError(null);
            
            try {
                
                const [ transactionsResponse, booksResponse, membersResponse ] = await Promise.all([
                    apiClient.get(`/transaction/getalltransactions`),
                    apiClient.get(`/book/getallbooks`),
                    apiClient.get(`/member/getallusers`)
                ]);

                const transactions = transactionsResponse.data;
                const books = booksResponse.data;
                const members = membersResponse.data;

                
                const allBorrowedTransactions = transactions.filter(
                    (transaction) => transaction.status === 'Borrowed'
                );

                
                const borrowedBooksWithDetails = allBorrowedTransactions.map(transaction => {
                    const book = books.find(b => b.bookID === transaction.bookID);
                    const member = members.find(m => m.memberID === transaction.memberID);
                    
                    return {
                        ...transaction,
                        bookTitle: book ? book.title : 'N/A',
                        borrowerName: member ? member.name : 'N/A',
                    };
                });

                setBorrowers(borrowedBooksWithDetails);
            } catch (err) {
                console.error("Error fetching data:", err);
                setError("An error occurred while fetching the data.");
                toast.error(`Failed to fetch the borrowers list!`, { duration:4000 });
            } finally {
                setLoading(false);
            }
        };

        fetchBorrowers();
    }, []);

    // if (loading) {
    //     return console.log();
    // }

    if (error) {
        return console.log(error);
    }

    return (
        <div className="adminappLayout">
            <AdminSidebar />

            <div className="adminmain">
                <AdminNavbar />
                <AdminToggleBar />

                <div className="admincontentRow">
                    <div className="admintableCard">
                        <div className="admincardTitle">All Borrowed Books</div>
                        <div className="admintableWrap">
                            <table className="adminTable">
                                <thead>
                                    <tr>
                                        <th><center>Book Title</center></th>
                                        <th><center>Borrower</center></th>
                                        <th><center>Borrow Date</center></th>
                                        <th><center>Return Date</center></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {borrowers.length > 0 ? (
                                        borrowers.map((borrower, index) => (
                                            <tr key={index}>
                                                <td>{borrower.bookTitle}</td>
                                                <td>{borrower.borrowerName}</td>
                                                <td>{borrower.borrowDate}</td>
                                                <td>{borrower.returnDate}</td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="4" className="adminno-data-message">No borrowers found.</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}