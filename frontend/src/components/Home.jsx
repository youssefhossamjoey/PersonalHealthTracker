import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useApi } from "../api/api";
import { apiRoutes } from "../api/auth";
import { useAuth } from '../auth/AuthContext.jsx';
import NavBar from './NavBar.jsx';

function Home({ username }) {
    const navigate = useNavigate();
    const { setAccessToken } = useAuth();
    const { api } = useApi();

    const handleLogout = async (e) => {
        try {
            const { endpoint, options } = apiRoutes.logout({});

            const res = await api(endpoint, options);
            setAccessToken(null);
            navigate('/');
        } catch (error) {
            console.error('Logout failed:', error.response ? error.response.data : error.message);
        }
    };

    return (
        <div className="pht-main-content" style={{ backgroundColor: '#b3af8f' }}>
            <NavBar />

        </div>
    );
}

export default Home;