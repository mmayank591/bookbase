import React, { useState, useEffect, useCallback } from "react";
import Sidebar from "./Sidebar";
import Navbar from "./Navbar";
import ToggleBar from "./ToggleBar";
import "./CSS/OverduePage.css";
import { AlertCircle } from "react-feather";
import { getUserId } from "../../utilities/SessionStorage";
import apiClient from "../../api";
import { FinePaymentModal } from "../../utilities/PayFineModel";
import getTotalFineAmount from "../../utilities/TotalFineAmount";

export default function OverdueBooksPage() {
  const [overdueBooks, setOverdueBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isPayFineModalOpen, setIsPayFineModalOpen] = useState(false);
  const [currentFine, setCurrentFine] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [totalFine, setTotalFine] = useState(0);

  const fetchOverdueBooks = useCallback(async () => {
    const userId = getUserId();
    if (!userId) {
      setError("No user logged in.");
      setLoading(false);
      return;
    }

    const totalFineAmount = await getTotalFineAmount(userId);
    setTotalFine(totalFineAmount);

    try {
      const [transactionsResponse, booksResponse, fineResponse] =
        await Promise.all([
          apiClient.get(`/transaction/getbymemberid/${userId}`),
          apiClient.get(`/book/getallbooks`),
          apiClient.get(`/fine/getallfines`),
        ]);

      const transactions = transactionsResponse.data;
      const books = booksResponse.data;
      const fines = fineResponse.data;

      const userOverdueTransactions = transactions.filter(
        (transaction) => transaction.status === "Overdue"
      );

      const booksWithDetails = userOverdueTransactions.map((transaction) => {
        const book = books.find((b) => b.bookID === transaction.bookID);
        const fine = fines.find(
          (f) => f.transactionId === transaction.transactionID
        );
        return {
          ...transaction,
          title: book ? book.title : "N/A",
          amount: fine ? fine.amount : 0.0,
          fineID: fine ? fine.fineID : 0,
          availableCopies: book.availableCopies,
        };
      });

      setOverdueBooks(booksWithDetails);
    } catch (err) {
      console.error(err);
      setError("An error occurred while fetching data.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchOverdueBooks();
  }, [fetchOverdueBooks]);

  const handleFinePayment = (r) => {
    setCurrentFine(r);
    setIsPayFineModalOpen(true);
  };

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value);
  };

  const filteredBooks = overdueBooks.filter((book) =>
    book?.title?.toLowerCase().includes(searchQuery.trim().toLowerCase())
  );

  if (loading) {
    console.log("Loading Overdue Books");
  }

  if (error) {
    console.log(error);
  }

  return (
    <div className="overdue-appLayout">
      <Sidebar />

      <div className="overdue-main">
        <Navbar />
        <ToggleBar
          searchQuery={searchQuery}
          onSearchChange={handleSearchChange}
        />

        <div className="overdue-contentRow">
          <div className="overdue-tableCard">
            <div className="overdue-tableWrap">
              <table className="overdueTable">
                <thead>
                  <tr>
                    <th>
                      <center>Transaction ID</center>
                    </th>
                    <th>
                      <center>Book Title</center>
                    </th>
                    <th>
                      <center>Due Date</center>
                    </th>
                    <th>
                      <center>Issue Date</center>
                    </th>
                    <th>
                      <center>Action</center>
                    </th>
                  </tr>
                </thead>

                <tbody>
                  {filteredBooks.length > 0 ? (
                    filteredBooks.map((book) => (
                      <tr key={book.transactionID}>
                        <td>{book.transactionID}</td>
                        <td>{book.title}</td>
                        <td>{book.returnDate}</td>
                        <td>{book.borrowDate}</td>
                        <td>
                          <AlertCircle
                            color="red"
                            size={20}
                            onClick={() => handleFinePayment(book)}
                          />
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="5">No overdue</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
            {totalFine > 0 && (
            <div className="total-fine-display">
              <div className="total-fine-text">
                Total Due Fine: ₹{totalFine}
              </div>
            </div>
            )}
          </div>
        </div>
      </div>
      <FinePaymentModal
        isOpen={isPayFineModalOpen}
        onClose={() => setIsPayFineModalOpen(false)}
        gettransaction={currentFine}
        onPaymentSuccess={fetchOverdueBooks}
      />
    </div>
  );
}
