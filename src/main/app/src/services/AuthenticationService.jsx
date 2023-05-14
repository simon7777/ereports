import axios from 'axios';

const hostUrl = `${process.env.HOST_URL}/api/auth`;

export const login = async (authRequest) => axios.post(`${hostUrl}/login`, authRequest);
