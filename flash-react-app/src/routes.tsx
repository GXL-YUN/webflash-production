// App.js
import { BrowserRouter as Router, Routes, Route,Navigate } from 'react-router-dom';
import { ConfigProvider } from 'antd';
import React, { useRef, useEffect } from 'react';
import AppLayout from './components/Layout/index';
import Home from './pages/Home';
import Index from './pages/Index';
import Table from "../src/components/Table/index";
import MkReview from "../src/components/MkReview/index";
import MkFromDate from "../src/components/MkFromDate/page/ThreeLayerPage";
import DemandList from '../src/components/DemandManagement/DemandList';
import DemandDetail from '../src/components/DemandManagement/DemandDetail';

/*
import Products from './pages/Products';
import Settings from './pages/Settings';
*/
// 页面组件
const Dashboard = () => <div><Home/></div>;

//路由地址
const App = () => {
    return (
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/about" element={<AppLayout><Index /></AppLayout>} />
                    <Route path="*" element={<NotFound />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/table" element={<Table />} />
                    <Route path="/mkReview" element={<MkReview />} />
                    <Route path="/mkFromDate" element={<MkFromDate />} />
                    <Route path="/demand/list" element={<DemandList />} />
                    <Route path="/demand/detail/:id" element={<DemandDetail />} />
                    <Route path="/" element={<Navigate to="/demand/list" replace />} />
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