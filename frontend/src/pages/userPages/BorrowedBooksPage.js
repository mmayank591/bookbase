import React, { useState, useEffect } from "react";
import toast from "react-hot-toast";
import Sidebar from "./Sidebar";
import Navbar from "./Navbar";
import ToggleBar from "./ToggleBar";
import "./CSS/BorrowedBooks.css";
import { RotateCw } from 'react-feather';
import { getUserId } from "../../utilities/SessionStorage";
import apiClient from "../../api";

export default function BorrowedBooksPage() {
  
  const [borrowedBooks, setBorrowedBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  const fetchBorrowedBooks = async () => {
    const userId = getUserId();
    if (!userId) {
      setError("No user logged in.");
      setLoading(false);
      return;
    }

    try {
                
      const [transactionsResponse, booksResponse] = await Promise.all([
        apiClient.get(`/transaction/getbymemberid/${userId}`),
        apiClient.get(`/book/getallbooks`)
      ]);

      const transactions = transactionsResponse.data;
      const books = booksResponse.data;
                
      const userBorrowedTransactions = transactions.filter(
        (transaction) =>
        transaction.status === 'Borrowed'
      );
                
      const booksWithDetails = userBorrowedTransactions.map(transaction => {
        const book = books.find(b => b.bookID === transaction.bookID);
        return {
          ...transaction,
          title: book ? book.title : 'N/A',
        };
      });

      setBorrowedBooks(booksWithDetails);
    } catch (err) {
      console.error(err);
      setError("An error occurred while fetching data.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBorrowedBooks();
  }, []);

  const handleReturnBook = async (transactionId) => {
    if (window.confirm("Are you sure you want to return this book?")) {
      try {
        const transactionResponse = await apiClient.get(`/transaction/getbyid/${transactionId}`);

        const transactionData = transactionResponse.data;
        const bookId = transactionData.bookID;

        const bookResponse = await apiClient.get(`/book/getbyid/${bookId}`);
        const booksData = bookResponse.data;

        if (booksData.length === 0) {
          throw new Error('Book not found.');
        }

        const newAvailableCopies = { availableCopies: booksData.availableCopies + 1 };
        const updateBookResponse = apiClient.patch(`/book/availablecount/${booksData.bookID}`, newAvailableCopies);
        console.log(updateBookResponse);

        const newStatus = { status: "Returned" };
        const updateTransactionResponse = apiClient.patch(`/transaction/updatestatus/${transactionId}`, newStatus);
        console.log(updateTransactionResponse);

        setBorrowedBooks(currentBooks =>
          currentBooks.filter(book => book.transactionID !== transactionId)
        );

        console.log("Book returned successfully.");
        toast.success(`${booksData.title} returned successfully!`, { duration: 5000 });

      } catch (err) {
        console.error("Error returning book:", err);
        setError("Could not return the book. Please try again later.");
      }
    }
  };


  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value);
  };

  const filteredBooks = borrowedBooks.filter((book) =>
    book?.title?.toLowerCase().includes(searchQuery.trim().toLowerCase())
  );
  
  if (loading) {
    console.log("Loading Borrowed Books");
  }

  if (error) {
    console.log(error);
  }

  return (
    <div className="borrowed-appLayout">
      <Sidebar />

      <div className="borrowed-main">
        <Navbar />
        <ToggleBar searchQuery={searchQuery} onSearchChange={handleSearchChange} />

        <div className="borrowed-contentRow">
          <div className="borrowed-tableCard">

            <div className="borrowed-tableWrap">
              <table className="borrowedTable">
                <thead>
                  <tr>
                    <th><center>ID</center></th>
                    <th><center>Book Title</center></th>
                    <th><center>Due Date</center></th>
                    <th><center>Issue Date</center></th>
                    <th><center>Action</center></th>
                  </tr>
                </thead>

                <tbody>
                  {filteredBooks.length > 0 ? (
                      filteredBooks.map((book) => (
                          <tr key={book.bookID}>
                              <td>{book.bookID}</td>
                              <td>{book.title}</td>
                              <td>{book.returnDate}</td>
                              <td>{book.borrowDate}</td>
                              <td><RotateCw size={15} onClick={() => handleReturnBook(book.transactionID)} /></td>
                          </tr>
                      ))
                  ) : (
                      <tr>
                          <td colSpan="5">No books currently borrowed</td>
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