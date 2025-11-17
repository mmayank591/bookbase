import React, { useState, useEffect } from 'react';
import toast from 'react-hot-toast';
import { X } from 'react-feather';
import apiClient from '../api';
import './PayFineModel.css';


export const FinePaymentModal = ({ isOpen, onClose, gettransaction, onPaymentSuccess }) => {
    const [error, setError] = useState(null);

    const [bookId, setBookId] = useState();
    const [bookTitle, setBookTitle] = useState('');
    const [transactionId, setTransactionId] = useState();
    const [newavailableCopies, setNewAvailableCopies] = useState();
    const [returnDate, setReturnDate] = useState();
    const [fineId, setFineId] = useState();
    const [fineAmount, setFineAmount] = useState();

    useEffect(() => {
        if (gettransaction) {
            setBookId(gettransaction.bookID);
            setTransactionId(gettransaction.transactionID);
            setFineId(gettransaction.fineID);
            setReturnDate(gettransaction.returnDate);
            setBookTitle(gettransaction.title);
            setFineAmount(gettransaction.amount);
            setNewAvailableCopies(gettransaction.availableCopies + 1)
        }
    }, [gettransaction]);

    useEffect(() => {
            if (error) {
                const timer = setTimeout(() => {
                setError(null);
                }, 5000);

            return () => clearTimeout(timer);
            }
    }, [error]);

    if (!isOpen) return null;

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const diffTime = today.getTime() - new Date(`${returnDate}`).getTime();
    const overdueDayscount = Math.ceil(diffTime / (1000 * 60 * 60 * 24));


    const handlePaymentConfirmation = async (e) => {
        e.preventDefault();

        const updatedBook = {
            availableCopies: newavailableCopies
        };

        const updatedTransaction = {
            status: 'Returned'
        };

        try {

            const updatedFine = {
                status: 'Paid',
                transactionDate: today,
            };

            console.log(fineId)
            
            await apiClient.patch(`/fine/updatepatch/${fineId}`, updatedFine);
            await apiClient.patch(`/transaction/updatestatus/${transactionId}`, updatedTransaction);
            await apiClient.patch(`/book/availablecount/${bookId}`, updatedBook);
            await apiClient.delete(`notification/deleteByTransactionId/${transactionId}`);
            

            if (onPaymentSuccess) {
                onPaymentSuccess(`Successfully paid fine of Rs. ${fineAmount}`);
            }

            toast.success(`Successfully paid fine ₹${fineAmount}!`, { duration: 5000 });
            onClose();

        } catch (err) {
            console.error("Payment processing error:", err);
            setError("Payment failed! Please try again later.");
        }
    };

    return (
        <div className="finemodal-overlay">
            
        <div className="finemodal-content">
            
            <div className="finemodal-header">
                <h2 className="fineheader-title"> 
                    Fine Payment
                </h2>
                <button onClick={onClose} className="fineclose-button">
                    <X className="fineclose-icon" /> 
                </button>
            </div>

            <div className="finemodal-body">
                <div className="fineitem-details-box">
                    <p className="finebook-title">
                        {bookTitle} 
                    </p>
                    <p className="fineoverdue-info">
                        Overdue by: <span className="fineoverdue-days">{overdueDayscount} days</span>
                    </p>
                </div>
                
                <div className="fineamount-due-section">
                    <p className="fineamount-due-label">Total Amount Due</p>
                    <p className="fine-amount">
                        ₹ {fineAmount}
                    </p>
                </div>
            </div>

            {error && (
                <div className="fineerror-message" role="alert">
                    {error}
                </div>
            )}

            <button onClick={handlePaymentConfirmation} className={`finepay-button`}>
                PAY NOW
            </button>
            <p className="finesecurity-note">
                Payment is handled securely.
            </p>
        </div>
    </div>
    );
}