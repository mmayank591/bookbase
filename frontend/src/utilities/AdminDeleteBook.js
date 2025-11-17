import apiClient from "../api";
import toast from "react-hot-toast";

export const handleDeleteBook = async (bookId, onBookDeleted) => {
    if (window.confirm("Are you sure you want to delete this book? This action cannot be undone.")) {
        try {
            const response = await apiClient.delete(`/book/deletebook/${bookId}`);

            console.log(`Book with id ${bookId} successfully deleted.`);
            toast.success(`Book deleted Successfully!`, { duration:4000 });
            onBookDeleted();
        } catch (error) {
            console.error("Error deleting book:", error);
            toast.error(`Error deleting book!`, { duration: 4000 });
        }
    }
};