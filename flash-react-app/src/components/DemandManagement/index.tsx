// App.tsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Layout, ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import 'antd/dist/reset.css';
import DemandList from './DemandList';
import DemandDetail from './DemandDetail';

const { Header, Content } = Layout;

const list: React.FC = () => {
    return (
        <ConfigProvider locale={zhCN}>
            <Router>
                <Layout style={{ minHeight: '100vh' }}>
                    <Header style={{ color: 'white', fontSize: 20, fontWeight: 'bold' }}>
                        项目需求管理系统
                    </Header>
                    <Content style={{ padding: '0 24px' }}>
                        <Routes>
                            <Route path="/demand/list" element={<DemandList />} />
                            <Route path="/demand/detail/:id" element={<DemandDetail />} />
                            <Route path="/" element={<Navigate to="/demand/list" replace />} />
                        </Routes>
                    </Content>
                </Layout>
            </Router>
        </ConfigProvider>
    );
};

export default list;