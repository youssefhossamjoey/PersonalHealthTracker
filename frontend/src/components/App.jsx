import '../App.css'
import Login from './Login'
import Register from './Register'
import Home from './Home'
import { Routes, Route } from 'react-router-dom'
import ProtectedRoute from '../auth/ProtectedRoute.jsx'
import GuestRoute from '../auth/GuestRoute.jsx'

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<GuestRoute><Login /></GuestRoute>} />
        <Route path="/signup" element={<GuestRoute><Register /></GuestRoute>} />
        <Route path="/home" element={<ProtectedRoute><Home /></ProtectedRoute>} />
      </Routes>
    </div>
  );
}

export default App
