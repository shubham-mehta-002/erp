import { Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import SetPassword from './components/SetPassword';
import LoginPage from './pages/LoginPage';
import Home from "./components/Home";
import ProtectedRoute from './components/ProtectedRoute';
import { useState } from 'react';
import { CircularProgress } from '@mui/material';
import Loading from './pages/Loading';
import ChatPage from './pages/ChatPage';

const App = () => {
  const [loading, setLoading] = useState(false)

    return (
        <>
            <Toaster />
            {loading && <Loading />}
            <Routes>
                <Route path="/" element={
                    <ProtectedRoute>
                        <Home setLoading={setLoading} />
                    </ProtectedRoute>
                } />
                <Route path="/login" element={<LoginPage setLoading={setLoading} />} />
                <Route path="/set-password" element={<SetPassword setLoading={setLoading} />} />
                <Route path="/chat" element={
                    <ProtectedRoute>
                        <ChatPage setLoading={setLoading}/>
                    </ProtectedRoute>
                } />
              
            </Routes>
        </>
    );
};



export default App;