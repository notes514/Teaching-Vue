import axios from 'axios'
import router from './router'
import Element from 'element-ui'
import store from './store'

// 配置baseURL
axios.defaults.baseURL = 'http://localhost:8081';

// 创建axios对象，设置超时时间为5000，请求头返回json
const request = axios.create({
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
});

// 前置拦截器
axios.interceptors.request.use(config => {
    // 请求头带上token
    config.headers['Authorization'] = localStorage.getItem('token');
    return config;
});

// 后置拦截器
axios.interceptors.response.use(response => {
    const res = response.data;
    if (res.code === 200) {
        return response;
    } else {
        // 弹出异常信息
        Element.Message({
            showClose: true,
            message: res.msg,
            type: 'error'
        });
        // 拒接往下返回结果信息
        return Promise.reject(res.msg);
    }
}, error => {
    if (error.response.data) {
        error.message = error.response.data.msg;
    }
    // 未登录没权限
    if (error.response.status === 401) {
        store.commit('REMOVE_INFO');
        router.push("/login").then(r => {
            console.log(r);
        });
        error.message = '请重新登录！';
    }
    if (error.response.status === 403) {
        error.message = '权限不足，无法访问！';
    }
    Element.Message({
        showClose: true,
        message: error.message,
        type: "error"
    });
    return Promise.reject(error.message);
});