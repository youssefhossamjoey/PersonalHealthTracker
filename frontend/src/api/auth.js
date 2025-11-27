export const apiRoutes = {
    login: (credentials) => ({
        endpoint: "/api/v1/auth/login",
        options: {
            method: "POST",
            body: JSON.stringify(credentials)
        }
    }),

    logout: () => ({
        endpoint: "/api/v1/auth/logout",
        options: { method: "POST" }
    }),

    getUser: () => ({
        endpoint: "/api/v1/user",
        options: {}
    }),

    checkUsernameAvailable: (username) => ({
        endpoint: `/api/v1/auth/username-available?username=${encodeURIComponent(username)}`,
        options: {
            method: "GET"
        }
    }),

    register: (userInfo) => ({
        endpoint: "/api/v1/auth/register",
        options: {
            method: "POST",
            body: JSON.stringify(userInfo)
        }
    }),
};
