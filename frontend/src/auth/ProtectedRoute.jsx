import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

export default function ProtectedRoute({ children }) {
    const { accessToken, loading } = useAuth();
    if (loading) return <div>Loading...</div>; // wait for silent refresh
    if (!accessToken) return <Navigate to="/" replace />;

    return children;
}