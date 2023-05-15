import axios from 'axios';
import {clearSessionData, getJwtToken, setJwtToken, setRole} from "./JwtTokenUtil";

const existsJwtToken = () => {
    return getJwtToken() != null;
}

const redirectToLoginPage = () => {
    window.location.href = '/login';
}

const addHttpRequestInterceptor = () => {
    axios.interceptors.request.use(
        (config) => {
            config.headers.Authorization = `Bearer ${getJwtToken()}`;
            return config;
        }, (error) => {
            console.error(error);
            redirectToLoginPage();
        }
    );
}

const addHttpResponseInterceptors = () => {
    axios.interceptors.response.use(
        response => response,
        error => {
            if (error?.response?.status === 401) {
                clearSessionData();
                redirectToLoginPage();
            }
        }
    );
}

export const logout = () => {
    clearSessionData();
    redirectToLoginPage();
}

export const isAuthenticated = () => {
    if (existsJwtToken()) {
        addHttpRequestInterceptor();
        addHttpResponseInterceptors();

        return true;
    }

    return false;
}

export const login = (authResponse) => {
    setJwtToken(authResponse.token);
    setRole(authResponse.role);

    if (existsJwtToken()) {
        addHttpRequestInterceptor();
        addHttpResponseInterceptors();
    }

    return existsJwtToken();
}
