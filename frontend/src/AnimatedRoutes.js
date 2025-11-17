import React from 'react';
import { Routes, Route, useLocation, Navigate } from 'react-router-dom';

import Signin from './pages/userPages/Signin';
import Signup from './pages/userPages/Signup';
import BorrowPage from "./pages/userPages/BorrowPage";
import ReturnedPage from "./pages/userPages/ReturnedPage";
import DashboardPage from "./pages/userPages/DashboardPage";
import BorrowedBooksPage from "./pages/userPages/BorrowedBooksPage";
import OverdueBooksPage from './pages/userPages/OverdueBooksPage';

import AdminDashboardPage from "./pages/adminPages/AdminDashboard";
import AdminBorrowers from './pages/adminPages/AdminBorrowers';
import AdminOverdue from './pages/adminPages/AdminOverdue';
import AdminBooks from './pages/adminPages/AdminBooks';
import AdminUsers from './pages/adminPages/AdminUsers'

import { AnimatePresence } from 'framer-motion';

function AnimatedRoutes() {
    const location = useLocation();

    return (
        <AnimatePresence>
            <Routes location={location} key={location.pathname}>
                <Route index element={<Signin />} />
                <Route path="/signin" element={<Signin />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/" element={<Navigate to="/dashboard" replace />} />
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/borrowed-books" element={<BorrowedBooksPage />} />
                <Route path="/returned" element={<ReturnedPage />} />
                <Route path="/overdue" element={<OverdueBooksPage />} />
                <Route path="/catalogue" element={<BorrowPage />} />
                <Route path="/admin" element={<Navigate to="/admindashboard" replace />} />
                <Route path="/admindashboard" element={<AdminDashboardPage />} />
                <Route path="/adminborrowers" element={<AdminBorrowers />} />
                <Route path="/adminoverdue" element={<AdminOverdue />} />
                <Route path="/adminbooks" element={<AdminBooks />} />
                <Route path="/adminusers" element={<AdminUsers />} />
            </Routes>
        </AnimatePresence>
    );
}

export default AnimatedRoutes;