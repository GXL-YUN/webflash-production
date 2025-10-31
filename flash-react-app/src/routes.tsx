// App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ConfigProvider } from 'antd';



import AppLayout from './components/Layout/index';
import Home from './pages/Home';
import About from './pages/Index';
/*
import Products from './pages/Products';
import Settings from './pages/Settings';
*/

// 页面组件
const Dashboard = () => <div><Home/></div>;
const UserManagement = () => <div>用户管理页面</div>;
const ProductManagement = () => <div>产品管理页面</div>;
const ArticleManagement = () => <div>文章管理页面</div>;
const SalesStatistics = () => <div>销售统计页面</div>;



const App = () => {
    return (
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/about" element={<About />} />
                    <Route path="*" element={<NotFound />} />


                    <Route path="/dashboard" element={<Dashboard />} />

                </Routes>
    );
};

// 404页面
const NotFound = () => (
    <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404</h1>
        <p>页面未找到</p>
    </div>
);

export default App;