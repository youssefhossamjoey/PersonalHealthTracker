import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

export default function GuestRoute({ children }) {
    const { accessToken, loading } = useAuth();
    if (loading) return <div>Loading...</div>; // wait for silent refresh
    if (accessToken) {
        return <Navigate to="/home" replace />;
    }

    return children;
}
