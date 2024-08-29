export function checkAuth() {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        window.location.href = '/login';
    }
}
// check login and register page if logged in redirect to home
export function checkLogin() {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
        window.location.href = '/dashboard';
    }
}

// export function checkAuth() {
//     return !!localStorage.getItem('accessToken');
// }

export function logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    window.location.href = '/auth/login';
}

export function getAccessToken() {
    return localStorage.getItem('accessToken');
}
export function getRefreshToken() {
    return localStorage.getItem('refreshToken');
}