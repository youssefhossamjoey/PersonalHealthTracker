import { useAuth } from "../auth/AuthContext";
const BASE_URL = "http://localhost:8080"; // your backend URL
import { useNavigate } from 'react-router-dom';

export function useApi() {
    const { accessToken, setAccessToken } = useAuth();
    const navigate = useNavigate();
    async function refreshToken() {
        const response = await fetch(`${BASE_URL}/api/v1/auth/refresh`, {
            method: "POST",
            credentials: "include", // to include cookies
        });

        if (!response.ok) {
            setAccessToken(null);
            alert("Session expired. Please log in again.");
            navigate('/');
            return null;
        }
        const data = await response.json();
        setAccessToken(data.token);
        return data.token;
    }

    async function api(endpoint, options = {}) {
        let res = await fetch(`${BASE_URL}${endpoint}`, {
            ...options,
            headers: {
                ...(options.headers || {}),
                Authorization: accessToken ? `Bearer ${accessToken}` : null,
                "Content-Type": (options.body && !options.headers?.["Content-Type"] && !(options.body instanceof FormData)) ? "application/json" : undefined,
            },
            credentials: "include", // to include cookies
        });

        if (res.status === 401&&endpoint!='/api/v1/auth/login') {
            const newToken = await refreshToken();
            if (!newToken) {
                throw new Error("Unauthorized");
            }
            res = await fetch(`${BASE_URL}${endpoint}`, {
                ...options,
                headers: {
                    ...(options.headers || {}),
                    Authorization: `Bearer ${newToken}`,
                    "Content-Type": (options.body && !options.headers?.["Content-Type"] && !(options.body instanceof FormData)) ? "application/json" : undefined,
                },
                credentials: "include", // to include cookies
            });
        }

        if (!res.ok) {
            const errorData = await res.json();
            throw new Error(errorData.message || "API request failed");
        }
        const text = await res.text();      // read raw text first
        const data = text ? JSON.parse(text) : null;
        return data;
    }

    return { api };
}
