import apiClient from "../api"; 
import { getUserId } from "./SessionStorage";

export const handleCheckout = async (selectedBookIds) => {
  const userId = getUserId();

  if (!userId) {
    throw new Error("No user is logged in. Please log in to borrow books.");
  }
  if (!selectedBookIds || selectedBookIds.length === 0) {
    throw new Error("No books selected. Please select at least one book to checkout.");
  }

  try {
    const [booksResponse, transactionsResponse] = await Promise.all([
      apiClient.get(`/book/getallbooks`),
      apiClient.get(`/transaction/getalltransactions`)
    ]);

    const allBooks = booksResponse.data;
    const allTransactions = transactionsResponse.data;

    const borrowDate = new Date();
    const returnDate = new Date();
    returnDate.setDate(borrowDate.getDate() + 2);

    const formatDate = (date) => {
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const year = date.getFullYear();
      return `${day}-${month}-${year}`;
    };

    const checkoutPromises = selectedBookIds.map(async (bookID) => {
      const bookToUpdate = allBooks.find(b => b.bookID === bookID);
      if (!bookToUpdate || bookToUpdate.availableCopies <= 0) {
        throw new Error(`Book with ID ${bookID} is no longer available.`);
      }

      const newTransaction = {
        bookID: bookID,
        memberID: userId,
        borrowDate: formatDate(borrowDate),
        returnDate: formatDate(returnDate),
        status: "Borrowed",
      };

      const addTransactionPromise = apiClient.post(`/transaction/createnew`, newTransaction);
      const patchData = { availableCopies: bookToUpdate.availableCopies - 1 };
      const updateBookPromise = apiClient.patch(`/book/availablecount/${bookToUpdate.bookID}`, patchData);
     
      return Promise.all([addTransactionPromise, updateBookPromise]);
    });

    await Promise.all(checkoutPromises);
    return true;

  } catch (error) {
    console.error("Checkout failed:", error);
    throw error;
  }
};