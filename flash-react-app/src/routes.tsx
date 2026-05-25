// App.js
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import React from 'react';
import Shouye from './components/index/Shouye';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';

// 正确导入路由配置
import testRoutes from './test';  // 假设test.js默认导出一个返回Route数组的函数
import projectRoutes from './operation/project/routes/index';  // 假设index.js默认导出一个返回Route数组的函数

// Ant Design 全局配置
const theme = {
    token: {
        colorPrimary: '#1890ff',
        borderRadius: 4,
        fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
    },
};

const BASENAME = '/ekp_mkpass/mk_limi_table_view';

const NotFound = () => (
    <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404</h1>
        <p>页面未找到</p>
    </div>
);

// 路由组件
const App = () => {
    return (
        <ConfigProvider theme={theme} locale={zhCN}>
            <BrowserRouter basename={BASENAME}>
                <Routes>
                    {/* 首页 */}
                    <Route path="/" element={<Shouye />} />

                    {/* 动态添加其他路由 */}
                    {testRoutes && Array.isArray(testRoutes()) ? testRoutes() : null}
                    {projectRoutes && Array.isArray(projectRoutes()) ? projectRoutes() : null}

                    {/* 404页面 */}
                    <Route path="*" element={<NotFound />} />
                </Routes>
            </BrowserRouter>
        </ConfigProvider>
    );
};

export default App;