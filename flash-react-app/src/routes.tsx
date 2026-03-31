// App.js
import { BrowserRouter as Router, Routes, Route,Navigate } from 'react-router-dom';
import React, { useRef, useEffect } from 'react';

import Shouye from './components/index/Shouye';



import ProjectDashboard from  './operation/project/routes/index';
import AppLayout from './components/Layout/index';



import test from  './test';
// 页面组件
const Dashboard = () => <div><Shouye/></div>;
//路由地址
const App = () => {
    // @ts-ignore
    return (
                <Routes>
                    {/*首页*/}
                    <Route path="/" element={<Shouye />} />
                    <Route path="*" element={<NotFound />} />
                    {/*测试路由*/}
                    {test()}
                    {ProjectDashboard()}
                </Routes>
    );
};
const NotFound = () => (
    <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404</h1>
        <p>页面未找到</p>
    </div>
);
export default App;