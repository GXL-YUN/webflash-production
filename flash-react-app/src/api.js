// api.js
import axios from 'axios';

const api = axios.create({
    baseURL: 'https://api.example.com',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 请求拦截器
api.interceptors.request.use(
    (config) => {
        // 在发送请求前做些什么，如添加tokenclea
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 响应拦截器
api.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        // 对响应错误做些什么
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    console.error('未授权，请重新登录');
                    break;
                case 403:
                    console.error('拒绝访问');
                    break;
                case 404:
                    console.error('请求的资源不存在');
                    break;
                case 500:
                    console.error('服务器错误');
                    break;
                default:
                    console.error('请求错误:', error.response.status);
            }
        }
        return Promise.reject(error);
    }
);

export default api;