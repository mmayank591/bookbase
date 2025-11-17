import React from 'react';
import './App.css';
import { BrowserRouter as Router } from 'react-router-dom';
import AnimatedRoutes from './AnimatedRoutes';
import { Toaster } from 'react-hot-toast';

function App() {
  return (
    <Router>
      <Toaster position="top-center" reverseOrder={false} />
      <AnimatedRoutes />
    </Router>
  );
}

export default App;
