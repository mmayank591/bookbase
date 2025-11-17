import React, { useState, useEffect, useCallback } from "react";
import AdminSidebar from "./AdminSidebar";
import AdminNavbar from "./AdminNavbar";
import "./CSS/AdminBooks.css";
import { Edit, Trash, PlusSquare } from 'react-feather';
import apiClient from "../../api";
import toast from "react-hot-toast";
import AdminSearchBar from "./AdminSearchBar";

import { AddBookModal } from '../../utilities/AdminAddBook';
import { EditBookModal } from '../../utilities/AdminEditBook';
import { handleDeleteBook } from '../../utilities/AdminDeleteBook';

export default function AdminBooks() {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [currentBook, setCurrentBook] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');

    
    const fetchBooks = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await apiClient.get(`/book/getallbooks`);
                
            const booksData = response.data;
            setBooks(booksData);
        } catch (err) {
            console.error("Error fetching books:", err);
            setError("An error occurred while fetching books.");
            toast.error(`Failed to fetch the books!`, { duration:4000 });
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchBooks();
    }, [fetchBooks]);

    const handleEditClick = (book) => {
        setCurrentBook(book);
        setIsEditModalOpen(true);
    };

    const handleDeleteClick = (bookId) => {
        handleDeleteBook(bookId, fetchBooks);
    };

    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    // const filteredBooks = books.filter((book) => {
    //     if (!book || book.title === undefined || book.title === null) {
    //         return false;
    //     }
        
    //     return book.title.includes(searchQuery.trim())
    // });

    const filteredBooks = books.filter((book) =>
    book?.title?.toLowerCase().includes(searchQuery.trim().toLowerCase())
  );

    // if (loading) {
    //     return console.log();
    // }

    if (error) {
        return console.log(error);
    }

    return (
        <div className="adminbookappLayout">
            <AdminSidebar />

            <div className="adminbookmain">
                <AdminNavbar />
                <AdminSearchBar searchQuery={searchQuery} onSearchChange={handleSearchChange} />

                <div className="adminbookcontentRow">
                    <div className="adminbooktableCard">
                        <div className="adminbookHeader">
                            <div className="adminbookcardTitle">All Books List</div>
                            <div className="adminbookcardIcon"><PlusSquare size={28} onClick={() => setIsAddModalOpen(true)} /></div>
                        </div>
                        <div className="adminbooktableWrap">
                            <table className="adminBooks">
                                <thead>
                                    <tr>
                                        <th><center>Book ID</center></th>
                                        <th><center>Title</center></th>
                                        <th><center>Author</center></th>
                                        <th><center>Genre</center></th>
                                        <th><center>Available Copies</center></th>
                                        <th><center>Action</center></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {filteredBooks.length > 0 ? (
                                        filteredBooks.map((book) => (
                                            <tr key={book.bookID}>
                                                <td>{book.bookID}</td>
                                                <td>{book.title}</td>
                                                <td>{book.author}</td>
                                                <td>{book.genre}</td>
                                                <td>{book.availableCopies}</td>
                                                <td><Edit size={20} onClick={() => handleEditClick(book)}/> <Trash size={20} onClick={() => handleDeleteClick(book.bookID)}/></td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="6" className="adminbookno-data-message">No books found</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <AddBookModal isOpen={isAddModalOpen} onClose={() => setIsAddModalOpen(false)} onBookAdded={fetchBooks} />
            <EditBookModal isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)} book={currentBook} onBookUpdated={fetchBooks} />
        </div>
    );
}