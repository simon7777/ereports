const JWT_TOKEN_KEY = 'jwt_token';
const USER_ROLE = 'user_role';

export const getJwtToken = () => {
    return sessionStorage.getItem(JWT_TOKEN_KEY);
}

export const setJwtToken = (token) => {
    sessionStorage.setItem(JWT_TOKEN_KEY, token);
}

export const getRole = () => {
    return sessionStorage.getItem(USER_ROLE);
}

export const setRole = (role) => {
    sessionStorage.setItem(USER_ROLE, role);
}

export const clearSessionData = () => {
    sessionStorage.removeItem(JWT_TOKEN_KEY);
    sessionStorage.removeItem(USER_ROLE);
}
