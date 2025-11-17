import React, { useState, useEffect } from "react";
import AdminSidebar from "./AdminSidebar";
import AdminNavbar from "./AdminNavbar";
import AdminToggleBar from "./AdminToggleBar";
import "./CSS/AdminOverdue.css";
import apiClient from "../../api";
import toast from "react-hot-toast";

export default function AdminOverdue() {
    const [overdueBooks, setOverdueBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOverdueBooks = async () => {
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

                const today = new Date();

                
                const allOverdueTransactions = transactions.filter(t => {
                    return t.status === 'Overdue';
                });

                
                const overdueBooksWithDetails = allOverdueTransactions.map(transaction => {
                    const book = books.find(b => b.bookID === transaction.bookID);
                    const member = members.find(m => m.memberID === transaction.memberID);
                    
                    return {
                        ...transaction,
                        bookTitle: book ? book.title : 'N/A',
                        borrowerName: member ? member.name : 'N/A',
                    };
                });

                setOverdueBooks(overdueBooksWithDetails);
            } catch (err) {
                console.error("Error fetching data:", err);
                setError("An error occurred while fetching the data.");
                toast.error(`Failed to fetch the overdue borrowers!`, { duration:4000 });

            } finally {
                setLoading(false);
            }
        };

        fetchOverdueBooks();
    }, []);

    // if (loading) {
    //     return console.log();
    // }

    if (error) {
        return console.log(error);
    }

    return (
        <div className="aoverdueappLayout">
            <AdminSidebar />

            <div className="aoverduemain">
                <AdminNavbar />
                <AdminToggleBar />

                <div className="aoverduecontentRow">
                    <div className="aoverduetableCard">
                        <div className="aoverduecardTitle">All Overdue Books</div>
                        <div className="aoverduetableWrap">
                            <table className="aoverduetable">
                                <thead>
                                    <tr>
                                        <th><center>Book Title</center></th>
                                        <th><center>Borrower</center></th>
                                        <th><center>Borrow Date</center></th>
                                        <th><center>Return Date</center></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {overdueBooks.length > 0 ? (
                                        overdueBooks.map((book, index) => (
                                            <tr key={index}>
                                                <td>{book.bookTitle}</td>
                                                <td>{book.borrowerName}</td>
                                                <td>{book.borrowDate}</td>
                                                <td>{book.returnDate}</td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="4" className="aoverdueno-data-message">No overdue books</td>
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