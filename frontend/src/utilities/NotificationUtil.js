import apiClient from "../api";
import { fineHandler } from "./FineUtil";


export async function sendNotification(memberId) {
  try {

    const [transactionsResponse, booksResponse, notificationResponse] =
      await Promise.all([
        apiClient.get(`/transaction/getbymemberid/${memberId}`),
        apiClient.get(`/book/getallbooks`),
        apiClient.get(`/notification/getbymemberid/${memberId}`),
      ]);

    const transactions = transactionsResponse?.data ?? [];
    const books = booksResponse?.data ?? [];
    const notifications = notificationResponse?.data ?? [];

    const existingNotificationMap = new Map(
      notifications.map((n) => [n.transactionID, n.notificationID])
    );

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const allOverdueTransactions = transactions.filter((t) => {
      return t.status === "Overdue";
    });

    const notificationPromises = allOverdueTransactions.map((transaction) => {
      const book = books.find((b) => b.bookID === transaction.bookID);

      if (!book) {
        console.warn(`Book not found for transaction ${transaction.transactionID}. Skipping.`);
        return Promise.resolve(); 
      }

      const diffTime =
        today.getTime() - new Date(`${transaction.returnDate}`).getTime();
      const overdueDays = Math.max(1, Math.ceil(diffTime / (1000 * 60 * 60 * 24)));
      const id = transaction.transactionID;

      
      fineHandler(memberId, id, overdueDays);

      
      const payload = {
        memberID: memberId,
        transactionID: id,
        message: `${book.title} is overdue by ${overdueDays} day${
          overdueDays === 1 ? "" : "s"
        }`,
      };

      
      if (existingNotificationMap.has(id)) {
        
        const notificationID = existingNotificationMap.get(id);

        return apiClient.put(`/notification/update/${notificationID}`, payload);

      } else {
        
        return apiClient.post(`/notification/createnew`, payload);
      }
    });

    
    await Promise.all(notificationPromises);
    console.log(`Notifications created/updated successfully for member ${memberId}.`);

  } catch (err) {
    console.error("Error processing notifications:", err);
    return;
  }
}