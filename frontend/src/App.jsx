import { useState,useEffect } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Login from './Login'
import { Routes, Route, Navigate } from 'react-router-dom'

function Home({ user }) {
  const [count, setCount] = useState(0)
  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Welcome, {user.email}</h1>
      <div className="card">
        <button onClick={() => setCount((c) => c + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

function RequireAuth({ user, children }) {
  if (!user) return <Navigate to="/login" replace />
  return children
}

function App() {
  const [user, setUser] = useState(null)
  useEffect(() => {
    console.log('user changed:', user)
  }, [user])
  return (
    <Routes>
      <Route path="/login" element={<Login onSuccess={(u) => setUser(u)} />} />
      <Route
        path="/"
        element={
          <RequireAuth user={user}>
            <Home user={user} />
          </RequireAuth>
        }
      />
      {/* fallback to home */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
