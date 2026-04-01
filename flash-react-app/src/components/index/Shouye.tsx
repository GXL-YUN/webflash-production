// src/App.tsx
import React from 'react';
import { ConfigProvider, Button, Space, message } from 'antd';
import { useNavigate, Link, NavLink } from 'react-router-dom';
import zhCN from 'antd/es/locale/zh_CN';
import AppCardList from './AppCardList';
import { AppCardItem } from './type';
import 'antd/dist/reset.css';
import './styles.css';


const App: React.FC = () => {
    const navigate = useNavigate();
    // 示例数据
    const mockData: AppCardItem[] = [
        {
            key: 'schedule',
            title: '项目管理',
            description: '查看和管理个人日程安排',
            icon: 'calendar',
            group:'common',
            iconColor: '#1890ff',
            url:'/project/demandList',
            onClick: () => message.info('点击了日程管理')
        },
        {
            key: 'dashboard',
            title: '数据看板',
            description: '实时数据监控与分析',
            icon: 'dashboard',
            iconColor: '#faad14',
            iconType: 'custom',
            url:'',            group:'sys',
            onClick: () => message.info('点击了数据看板')
        },

        {
            key: 'database',
            title: '测试',
            description: '内容测试',
            icon: 'database',
            iconColor: '#52c41a',
            url: '/test', group: 'test',
            onClick: () => message.info('点击了数据中心')
        },
        {
            key: 'database',
            title: '数据中心',
            description: '企业数据存储与分析',
            icon: 'database',
            iconColor: '#52c41a',
            disabled: true,
            url:'',            group:'disabled',
            onClick: () => message.info('点击了数据中心')
        }
    ];

    const handleCardClick = (item: AppCardItem) => {
        console.log('点击卡片:', item);
        if (item.url && !item.disabled) {
            // 1. 普通跳转
            navigate(item.url);

            // // 2. 带参数的跳转
            // navigate('/target-page', {
            //     state: {
            //         id: 123,
            //         name: 'example'
            //     }
            // });
            //
            // // 3. 带查询参数的跳转
            // navigate('/target-page?id=123&name=example');
            //
            // // 4. 替换当前历史记录
            // navigate('/target-page', { replace: true });
            //
            // // 5. 后退
            // navigate(-1);
            //
            // // 6. 前进
            // navigate(1);
        }
    };

    return (
        <ConfigProvider locale={zhCN}>
            <div style={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
                {/* 顶部工具栏 */}
                <div style={{
                    padding: '16px 24px',
                    backgroundColor: '#fff',
                    borderBottom: '1px solid #f0f0f0',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
                    position: 'sticky',
                    top: 0,
                    zIndex: 100
                }}>
                    <div>
                        <h2 style={{ margin: 0, fontSize: 20, fontWeight: 600 }}>应用中心</h2>
                        <div style={{ color: '#666', fontSize: 12, marginTop: 4 }}>
                            当前已安装应用 {mockData.filter(item => !item.disabled).length} 个
                        </div>
                    </div>
                    <Space>
                        <Button type="primary" size="large">新增应用</Button>
                        <Button size="large">管理应用</Button>
                        <Button size="large">刷新</Button>
                    </Space>
                </div>

                {/* 主要内容区 */}
                <div style={{ maxWidth: 1400, margin: '0 auto', padding: 24 }}>
                    {/* 常用应用 */}
                    <AppCardList
                        title="常用应用"
                        subtitle={`共 ${mockData.slice(0, 6).filter(item =>item.group=="common").length} 个可用应用`}
                        data={mockData.filter(item => item.group=="common")}
                        onItemClick={handleCardClick}
                        columns={{ xs: 1, sm: 2, md: 3, lg: 4, xl: 6 }}
                    />

                    {/* 系统应用 */}
                    <AppCardList
                        title="系统应用"
                        subtitle={`共 ${mockData.filter(item =>item.group=="sys").length} 个应用`}
                        data={mockData.filter(item => item.group=="sys")}
                        onItemClick={handleCardClick}
                        columns={{ xs: 1, sm: 2, md: 3, lg: 4, xl: 6 }}
                    />
                    <AppCardList
                        title="测试"
                        subtitle={`共 ${mockData.filter(item =>item.group=="test").length} 个应用`}
                        data={mockData.filter(item => item.group=="test")}
                        onItemClick={handleCardClick}
                        columns={{ xs: 1, sm: 2, md: 3, lg: 4, xl: 6 }}
                    />
                    {/* 已停用应用 */}
                    <AppCardList
                        title="已停用"
                        subtitle={`共 ${mockData.filter(item => item.group=="disabled").length} 个应用`}
                        data={mockData.filter(item => item.group=="disabled")}
                        onItemClick={handleCardClick}
                        columns={{ xs: 1, sm: 2, md: 3, lg: 4, xl: 6 }}
                        emptyText="暂无已停用应用"
                    />
                </div>
            </div>
        </ConfigProvider>
    );
};

export default App;