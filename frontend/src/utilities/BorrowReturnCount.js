import { useState, useEffect } from "react";
import { getUserId } from "./SessionStorage";
import apiClient from "../api";
 
export const useBooksCount = () => {
    const [counts, setCounts] = useState({ borrowed: 0, returned: 0 });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
   
    useEffect(() => {
        const fetchTransactionData = async () => {
            setLoading(true);
            setError(null);
       
            const userID = getUserId();
            if (!userID) {
                setError("No user is logged in.");
                setLoading(false);
                return;
            }
       
            try {
                const response = await apiClient.get(`/transaction/getbymemberid/${userID}`);
               
                const allTransactions = response.data;

                const borrowedCount = allTransactions.filter((t) => t.status === "Borrowed").length;
                const returnedCount = allTransactions.filter((t) => t.status === "Returned").length;
                const overdueCount = allTransactions.filter((t) => t.status === "Overdue").length;

                setCounts({ borrowed: borrowedCount + overdueCount, returned: returnedCount });
            } catch (err) {
                console.error("Error in useBooksCount hook:", err);
                setError("Failed to fetch transaction data from the server.");
            } finally {
                setLoading(false);
            }
        };
   
        fetchTransactionData();
    }, []);
   
    return {
        borrowed: counts.borrowed,
        returned: counts.returned,
        loading,
        error,
    };
};