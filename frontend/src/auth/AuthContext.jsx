import { createContext, useContext, useState,useEffect,useRef } from "react";

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [accessToken, setAccessToken] = useState(null);
    const [loading, setLoading] = useState(true); // track if we're initializing
    const initializedRef = useRef(false);

    useEffect(() => {
            if (initializedRef.current) return; // prevent double initialization in StrictMode
            initializedRef.current = true;
        async function initializeAuth() {
            try {
                const res = await fetch("http://localhost:8080/api/v1/auth/refresh", {
                    method: "POST",
                    credentials: "include", // send HttpOnly refresh cookie
                });

                if (res.ok) {
                    const data = await res.json();
                    setAccessToken(data.token);
                } else {
                    setAccessToken(null);
                }
            } catch (err) {
                setAccessToken(null);
            } finally {
                setLoading(false); // done initializing
            }
        }

        initializeAuth();
    }, []);
    return (
        <AuthContext.Provider value={{ accessToken, setAccessToken }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}