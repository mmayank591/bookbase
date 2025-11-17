import apiClient from "../api";

export default async function setOverdueBooks(memberId) {
  try {
    
    const [transactionsResponse, booksResponse] = await Promise.all([
      apiClient.get(`/transaction/getbymemberid/${memberId}`),
      apiClient.get(`/book/getallbooks`),
    ]);

    const transactions = transactionsResponse.data;
    const books = booksResponse.data;

    const today = new Date();
    today.setHours(0, 0, 0, 0); 

    
    const overdueTransactions = transactions.filter(t => {
      const returnDate = new Date(t.returnDate);
      return t.status === 'Borrowed' && returnDate < today;
    });

    
    if (overdueTransactions.length === 0) {
      return [];
    }

    
    const updatePromises = overdueTransactions.map(t =>
      apiClient.patch(`/transaction/updatestatus/${t.transactionID}`, { status: "Overdue" })
    );
    
    await Promise.all(updatePromises);
    
    const overdueBookDetails = overdueTransactions.map(transaction => {
      const book = books.find(b => b.bookID === transaction.bookID);
      
      const returnDate = new Date(transaction.returnDate);
      
      const diffTime = today.getTime() - returnDate.getTime();
      const overdueDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
      
      return {
        ...transaction,
        status: 'Overdue', 
        bookTitle: book ? book.title : 'N/A',
        overdueDays: overdueDays > 0 ? overdueDays : 0, 
      };
    });

    console.log("Successfully processed overdue books:", overdueBookDetails);
    return overdueBookDetails;

  } catch (err) {
    console.error("Error processing overdue books:", err);
    return [];
  }
}