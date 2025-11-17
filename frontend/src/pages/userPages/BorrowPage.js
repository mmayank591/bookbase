import React, { useEffect, useState } from "react";
import toast from "react-hot-toast";
import Sidebar from "./Sidebar";
import Navbar from "./Navbar";
import ActionBar from "./ActionBar";
import "./CSS/BorrowPage.css";
import { handleCheckout } from "../../utilities/Checkout";
import apiClient from "../../api";
import { getUserId } from "../../utilities/SessionStorage";

export default function BorrowPage() {
  const [books, setBooks] = useState([]);
  const [borrowedBookIds, setBorrowedBookIds] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedBooks, setSelectedBooks] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");

  const userId = getUserId();

  const fetchBooksAndBorrowed = async () => {
    if (!userId) {
      setError("No user logged in.");
      setLoading(false);
      return;
    }

    try {
      const [booksRes, transactionsRes] = await Promise.all([
        apiClient.get(`/book/getallbooks`),
        apiClient.get(`/transaction/getbymemberid/${userId}`),
      ]);

      const allBooks = booksRes.data;
      const transactions = transactionsRes.data;

      const currentlyBorrowedBookIds = transactions
        .filter((tx) => tx.status === "Borrowed" || tx.status === "Overdue")
        .map((tx) => tx.bookID);

      setBooks(allBooks);
      setBorrowedBookIds(currentlyBorrowedBookIds); 
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBooksAndBorrowed();
  }, []);

  const handleCheckboxChange = (bookID) => {
    setSelectedBooks((prev) =>
      prev.includes(bookID)
        ? prev.filter((id) => id !== bookID)
        : [...prev, bookID]
    );
  };

  const onCheckoutClick = async () => {
    if (selectedBooks.length === 0) {
      toast.error(`Please select at least one book to checkout!`, {
        duration: 4000,
      });
      return;
    }
    try {
      await handleCheckout(selectedBooks);
      toast.success(
        `Checkout successful! You have borrowed ${selectedBooks.length} books`,
        { duration: 4000 }
      );
      setSelectedBooks([]);
      fetchBooksAndBorrowed(); // Refresh list
    } catch (err) {
      toast.error(`Checkout failed: ${err.message}`, { duration: 5000 });
    }
  };

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value);
  };

  const filteredBooks = books.filter((book) =>
    book?.title?.toLowerCase().includes(searchQuery.trim().toLowerCase())
  );

  // if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="appLayout">
      <Sidebar />
      <div className="main">
        <Navbar />
        <ActionBar
          onCheckout={onCheckoutClick}
          selectedCount={selectedBooks.length}
          searchQuery={searchQuery}
          onSearchChange={handleSearchChange}
        />
        <div className="content">
          <div className="contentRow">
            <div className="tableCard">
              <div className="ccardTitle">bookbase Books List</div>
              <div className="tableWrap">
                <table>
                  <thead>
                    <tr>
                      <th>
                        <center>Book ID</center>
                      </th>
                      <th>
                        <center>Name</center>
                      </th>
                      <th>
                        <center>Author</center>
                      </th>
                      <th>
                        <center>Genre</center>
                      </th>
                      <th>
                        <center>Status</center>
                      </th>
                      <th>
                        <center>Add to Cart</center>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredBooks.map((book) => {
                      const isBorrowed = borrowedBookIds.includes(book.bookID);
                      return (
                        <tr
                          key={book.bookID}
                          className={isBorrowed ? "disabled-row" : ""}
                        >
                          <td>{book.bookID}</td>
                          <td>{book.title}</td>
                          <td>{book.author}</td>
                          <td>{book.genre}</td>
                          <td>
                            {borrowedBookIds.includes(book.bookID)
                              ? "Borrowed"
                              : book.availableCopies > 0
                              ? `Available (${book.availableCopies})`
                              : "Unavailable"}
                          </td>
                          <td>
                            {book.availableCopies > 0 ? (
                              <input
                                type="checkbox"
                                checked={selectedBooks.includes(book.bookID)}
                                onChange={() =>
                                  handleCheckboxChange(book.bookID)
                                }
                                disabled={isBorrowed}
                                title={
                                  isBorrowed
                                    ? "You have already borrowed this book"
                                    : ""
                                }
                              />
                            ) : null}
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
