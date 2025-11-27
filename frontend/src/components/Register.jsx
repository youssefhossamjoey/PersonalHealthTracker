import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useApi } from "../api/api";
import { apiRoutes } from "../api/auth";
import '../App.css'


function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role] = useState('MEMBER');
    const [error, setError] = useState('');
    const navigate = useNavigate(); // Get the history object for redirection
    const { api } = useApi();


   async function validate() {
        if (!username) return 'Username is required';
        const re = /^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$/;
        if (!re.test(username)) return 'Invalid username';
        if (!password) return 'Password is required';
        if (password.length < 6) return 'Password must be at least 6 characters';
        if (password !== confirmPassword) return 'Passwords do not match';
        if ( await checkUsernameAvailability(username) === false) return 'Username is already taken';
        return '';
    }
    const checkUsernameAvailability = async (username) => {
        try {
            const { endpoint, options } = apiRoutes.checkUsernameAvailable(username);
            const data = await api(endpoint, options);
            return data;
        } catch (err) {
            console.error('Username availability check failed:', err.response ? err.response.data : err.message);
            return false;
        }
    }

    const handleSignup = async (e) => {
        e.preventDefault();
        try {
            setError('');
            const v = await validate();
            if (v) {
                setError(v);
                return;
            }
            const { endpoint, options } = apiRoutes.register({ username, password, role });
            const response = await api(endpoint, options);
            // handle succesful signup
            navigate('/home');
        }
        catch (err) {
            console.error('Signup failed:', err.response ? err.response.data : err.message);
            setError(err.response ? err.response.data : err.message);
        }
    }
        return (
            <div className="login-root">
                <form className="login-card" onSubmit={handleSignup}>
                    <h2>Create account</h2>
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
                            autoComplete="new-password"
                        />
                    </label>

                    <label>
                        Confirm password
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            placeholder="••••••••"
                            autoComplete="new-password"
                        />
                    </label>

                    <button type="submit" className="login-btn">Sign up</button>

                    <div style={{ marginTop: 12, textAlign: 'center' }}>
                        Already registered? <a href="/">Login</a>
                    </div>
                </form>
            </div>
        );
}
export default Register;