import '../App.css'
import Login from './Login'
import Register from './Register'
import Home from './Home'
import TopBar from './TopBar'
import { Routes, Route, useLocation } from 'react-router-dom'
import ProtectedRoute from '../auth/ProtectedRoute.jsx'
import GuestRoute from '../auth/GuestRoute.jsx'

function App() {
  const location = useLocation();
  const hideTopBar = location.pathname === '/' || location.pathname === '/signup';

  return (
    <div className={`App ${!hideTopBar ? 'pht-has-layout' : ''}`}>
      {!hideTopBar && <TopBar />}
      <Routes>
        <Route path="/" element={<GuestRoute><Login /></GuestRoute>} />
        <Route path="/signup" element={<GuestRoute><Register /></GuestRoute>} />
        <Route path="/home" element={<ProtectedRoute><Home /></ProtectedRoute>} />
      </Routes>
    </div>
  );
}

export default App
