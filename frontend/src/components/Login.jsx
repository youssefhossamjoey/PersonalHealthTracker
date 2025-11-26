import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApi } from "../api/api";
import { apiRoutes } from "../api/auth";
import { useAuth } from '../auth/AuthContext.jsx';
import axios from 'axios'
import '../App.css'

export default function Login() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const { setAccessToken } = useAuth();
    const navigate = useNavigate();
    const { api } = useApi();


    function validate() {
        if (!username) return 'Username is required'
        // simple email regex
        const re = /^(?=[a-zA-Z0-9._]{5,20}$)(?!.*[_.]{2})[^_.].*[^_.]$/;
        if (!re.test(username)) return 'Invalid username'
        if (!password) return 'Password is required'
        if (password.length < 6) return 'Password must be at least 6 characters'
        return ''
    }

    const handleLogin = async (e) => {
        e.preventDefault()
        setError('')
        try {
            const v = validate()
            if (v) {
                setError(v)
                return
            }
            const { endpoint, options } = apiRoutes.login({
                username,
                password
            });

            const data = await api(endpoint, options);
            if (data.token) {
                setAccessToken(data.token);
                navigate('/home');
            }
        } catch (error) {
            console.error('Login failed:', error.response ? error.response.data : error.message);
            setError('Invalid username or password.');
        }
    };

    return (
        <div className="login-root">
            <form className="login-card" onSubmit={handleLogin}>
                <h2>Sign in</h2>

                {error && <div className="login-error">{error}</div>}

                <label>
                    Username
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="username"
                        autoComplete="username"
                    />
                </label>

                <label>
                    Password
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="••••••••"
                        autoComplete="current-password"
                    />
                </label>

                <button type="submit" className="login-btn" disabled={false}>{/* simple disabled handling can be added */}
                    Sign in
                </button>

                <div style={{ marginTop: 12, textAlign: 'center' }}>
                    Don't have an account? <a href="/signup">Register</a>
                </div>
            </form>
        </div>
    );

}


