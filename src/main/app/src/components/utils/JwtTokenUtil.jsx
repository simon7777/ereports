const JWT_TOKEN_KEY = 'jwt_token';

export const getJwtToken = () => {
    return sessionStorage.getItem(JWT_TOKEN_KEY);
}

export const setJwtToken = (token) => {
    sessionStorage.setItem(JWT_TOKEN_KEY, token);
}

export const removeJwtToken = () => {
    sessionStorage.removeItem(JWT_TOKEN_KEY);
}
