import { useState } from "react";
import apiClient from "../api";
import toast from "react-hot-toast";
import "./AdminAddBook.css";

export const AddBookModal = ({ isOpen, onClose, onBookAdded }) => {
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [genre, setGenre] = useState('');
    const [availableCopies, setAvailableCopies] = useState(1);

    if (!isOpen) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newBook = {
            title: title,
            author: author,
            genre: genre,
            availableCopies: availableCopies
        };

        try {
            const response = await apiClient.post(`/book/createnew`, newBook);

            console.log("New book added successfully:", await response.data);
            toast.success(`New Book Added: ${newBook.title}`, { duration: 5000 });
            onBookAdded();
            onClose();
        } catch (error) {
            console.error("Error Adding Book:", error);
            toast.error(`Error Adding Book: ${error}`, { duration: 4000 });
        }
    };

    return (
        <div className="adminaddbook-modal-overlay">
            <div className="adminaddbook-modal-content">
                <div className="adminaddbook-modal-header">
                    <h2>Add New Book</h2>
                    <button className="adminaddbook-modal-close-btn" onClick={onClose}>&times;</button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="adminaddbook-modal-form-group">
                        <label>Title:</label>
                        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
                    </div>
                    <div className="adminaddbook-modal-form-group">
                        <label>Author:</label>
                        <input type="text" value={author} onChange={(e) => setAuthor(e.target.value)} required />
                    </div>
                    <div className="adminaddbook-modal-form-group">
                        <label>Genre:</label>
                        <input type="text" value={genre} onChange={(e) => setGenre(e.target.value)} required />
                    </div>
                    <div className="adminaddbook-modal-form-group">
                        <label>Available Copies:</label>
                        <input type="number" value={availableCopies} onChange={(e) => setAvailableCopies(e.target.value)} required min="0" />
                    </div>
                    <div className="adminaddbook-modal-actions">
                        <button type="submit" className="adminaddbook-modal-submit-btn">Add Book</button>
                    </div>
                </form>
            </div>
        </div>
    );
};