import React, { useState, useEffect } from "react";
import Sidebar from "./Sidebar";
import Navbar from "./Navbar";
import ToggleBar from "./ToggleBar";
import "./CSS/ReturnedPage.css";
import { getUserId } from "../../utilities/SessionStorage";
import apiClient from "../../api";


export default function ReturnedPage() {
  const [returnedBooks, setReturnedBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  
  const fetchReturnedBooks = async () => {
    const userId = getUserId();
    if (!userId) {
      setError("No user logged in!");
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

      const userReturnedTransactions = transactions.filter(
        (transaction) =>
          transaction.status === 'Returned'
      );

      const booksWithDetails = userReturnedTransactions.map(transaction => {
        const book = books.find(b => b.bookID === transaction.bookID);
        return {
          ...transaction,
          title: book ? book.title : 'N/A',
        };
      });

      setReturnedBooks(booksWithDetails);
    } catch (err) {
      console.error(err);
      setError("An error occurred while fetching data!");
    } finally {
      setLoading(false);
    }};

  useEffect(() => {
    fetchReturnedBooks();
  },[]);

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value);
  };

  const filteredBooks = returnedBooks.filter((book) =>
    book?.title?.toLowerCase().includes(searchQuery.trim().toLowerCase())
  );

  if (error) {
    return console.log(error);
  }

  return (
    <div className="appLayout">
      <Sidebar />

      <div className="main">
        <Navbar />
        <ToggleBar searchQuery={searchQuery} onSearchChange={handleSearchChange} />

        <div className="contentRow">
          <div className="tableCard">
            <div className="rcardTitle">Returned Books List</div>

            <div className="tableWrap">
              <table className="rtable">
                <thead>
                  <tr>
                    <th><center>Book ID</center></th>
                    <th><center>Book Title</center></th>
                    <th><center>Issue Date</center></th>
                    <th><center>Returned Date</center></th>
                  </tr>
                </thead>

                <tbody>
                  {filteredBooks.length > 0 ? (
                        filteredBooks.map((book) => (
                            <tr key={book.transactionID}>
                                <td>{book.bookID}</td>
                                <td>{book.title}</td>
                                <td>{book.borrowDate}</td>
                                <td>{book.returnDate}</td>
                            </tr>
                        ))
                  ) : (
                      <tr>
                          <td colSpan="4">No books have been returned yet</td>
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
