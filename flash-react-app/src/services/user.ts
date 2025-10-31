import api from './api';

interface User {
    id: number;
    name: string;
    email: string;
}

export const getUsers = async (): Promise<User[]> => {
    return api.get('/users');
};

export const getUserById = async (id: number): Promise<User> => {
    return api.get(`/users/${id}`);
};

export const createUser = async (user: Omit<User, 'id'>): Promise<User> => {
    return api.post('/users', user);
};