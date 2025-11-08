import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './App.css'

export default function Login({ onSuccess }) {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    function validate() {
        if (!email) return 'Email is required'
        // simple email regex
        const re = /^\S+@\S+\.\S+$/
        if (!re.test(email)) return 'Invalid email'
        if (!password) return 'Password is required'
        if (password.length < 6) return 'Password must be at least 6 characters'
        return ''
    }

    const navigate = useNavigate()

    async function handleSubmit(e) {
        e.preventDefault()
        setError('')
        const v = validate()
        if (v) {
            setError(v)
            return
        }
        setLoading(true)
        try {
            await new Promise((r) => setTimeout(r, 600))
            setLoading(false)
            if (onSuccess) {
                onSuccess({ email })
                navigate('/', { replace: true })
            } else {
                alert('Logged in (mock): ' + email)
                navigate('/', { replace: true })
            }
        } catch (err) {
            setLoading(false)
            setError('Login failed')
        }
    }

    return (
        <div className="login-root">
            <form className="login-card" onSubmit={handleSubmit}>
                <h2>Sign in</h2>
                {error && <div className="login-error">{error}</div>}
                <label>
                    Email
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="you@example.com"
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
                <button type="submit" disabled={loading} className="login-btn">
                    {loading ? 'Signing in...' : 'Sign in'}
                </button>
            </form>
        </div>
    )
}
