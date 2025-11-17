import { useState, useEffect } from "react";
import "./AdminEditBook.css";
import apiClient from "../api";
import toast from "react-hot-toast";

export const EditBookModal = ({ isOpen, onClose, book, onBookUpdated }) => {
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [genre, setGenre] = useState('');
    const [availableCopies, setAvailableCopies] = useState(1);

    useEffect(() => {
        if (book) {
            setTitle(book.title);
            setAuthor(book.author);
            setGenre(book.genre);
            setAvailableCopies(book.availableCopies);
        }
    }, [book]);

    if (!isOpen) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();

        const updatedBook = {
            ...book,
            title: title,
            author: author,
            genre: genre,
            availableCopies: availableCopies
        };

        try {
            const response = await apiClient.put(`/book/updateput/${book.bookID}`, updatedBook);

            console.log("Book updated successfully:", await response.data);
            toast.success(`Book details updated successfully!`, { duration:4000 });
            onBookUpdated();
            onClose();
        } catch (error) {
            console.error("Error updating book:", error);
            toast.error(`Failed to update book!`, { duration:4000 });
        }
    };

    return (
        <div className="admineditbook-modal-overlay">
            <div className="admineditbook-modal-content">
                <div className="admineditbook-modal-header">
                    <h2>Edit Book Details</h2>
                    <button className="admineditbook-modal-close-btn" onClick={onClose}>&times;</button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="admineditbook-modal-form-group">
                        <label>Title</label>
                        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
                    </div>
                    <div className="admineditbook-modal-form-group">
                        <label>Author</label>
                        <input type="text" value={author} onChange={(e) => setAuthor(e.target.value)} required />
                    </div>
                    <div className="admineditbook-modal-form-group">
                        <label>Genre</label>
                        <input type="text" value={genre} onChange={(e) => setGenre(e.target.value)} required />
                    </div>
                    <div className="admineditbook-modal-form-group">
                        <label>Available Copies</label>
                        <input type="number" value={availableCopies} onChange={(e) => setAvailableCopies(e.target.value)} required min="0" />
                    </div>
                    <div className="admineditbook-modal-actions">
                        <button type="submit" className="admineditbook-modal-submit-btn">Update</button>
                    </div>
                </form>
            </div>
        </div>
    );
};