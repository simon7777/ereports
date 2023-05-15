import axios from 'axios';

const hostUrl = `${process.env.HOST_URL}/api/user`;

export const findAll = async () => axios.get(`${hostUrl}`);

export const findUser = async (id) => axios.get(`${hostUrl}/${id}`);

export const createUser = async (user) => axios.post(`${hostUrl}`, user);

export const updateUser = async (id, user) => axios.put(`${hostUrl}/${id}`, user);

export const deleteUser = async (id) => axios.delete(`${hostUrl}/${id}`);
