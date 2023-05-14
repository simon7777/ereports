import React from 'react';

import axios from 'axios';
import {getJwtToken, removeJwtToken, setJwtToken} from "./JwtTokenUtil";

const existsJwtToken = () => {
    return getJwtToken() != null;
}

const redirectToLoginPage = () => {
    window.location.href = '/login';
}

const addHttpRequestInterceptor = () => {
    axios.interceptors.request.use(
        (config) => {
            return config.headers.Authorization = `Bearer ${getJwtToken()}`;
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
            if (error.response.status === 401) {
                redirectToLoginPage();
            }
            return Promise.reject(error);
        }
    )
}

export const logout = () => {
    removeJwtToken();
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

export const login = (jwtToken) => {
    setJwtToken(jwtToken);

    if (existsJwtToken()) {
        addHttpRequestInterceptor();
        addHttpResponseInterceptors();
    }

    return existsJwtToken();
}
