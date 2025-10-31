import React ,{useState} from 'react';
import { Layout, Menu } from 'antd';
import Home from '../../routes';
import {
    BrowserRouter as Router,
    Routes,
    Route,
    useNavigate,
    useLocation
} from 'react-router-dom';

import {
    MenuUnfoldOutlined,
    MenuFoldOutlined,
    UserOutlined,
    VideoCameraOutlined,
    UploadOutlined,
} from '@ant-design/icons';

const { Header, Sider, Content } = Layout;

interface AppLayoutProps {
    children: React.ReactNode;
}

const AppLayout: React.FC<AppLayoutProps> = ({ children }) => {

    const [activeContent, setActiveContent] = useState('home');

    const [collapsed, setCollapsed] = React.useState(false);
    const menuItems = [
        { id: '/', label: '首页' , icon: <UserOutlined />,},
        { id: '/about', label: '功能页面说明', icon: <UserOutlined />,},
        { id: '/system/users', label: '产品说明' , icon: <UserOutlined />,},
        { id: '/dashboard', label: '测试u' , icon: <UserOutlined />,},

       /* { id: 'about', label: '关于', content: <Index /> }*/
    ];
    // 获取当前活动内容
  /*  const getActiveContent = () => {
        const activeItem = menuItems.find(item => item.id === activeContent);
        return activeItem ? activeItem.content : <div>页面未找到</div>;
    };*/

    const navigate = useNavigate();
    const location = useLocation();
    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider trigger={null} collapsible collapsed={collapsed}>
                <div className="logo"  />

                <Menu
                    theme="dark"
                    mode="inline"
                    selectedKeys={[activeContent]}
                    items={menuItems.map(item => ({
                        key: item.id,
                        icon: item.icon,
                        label: item.label,
                    }))}
                    onClick={({ key }) => navigate(key)}
                />
                {/*<Menu theme="dark" mode="inline">
                    <Menu.Item key="/" icon={<UserOutlined />}>
                        功能页面介绍
                    </Menu.Item>
                    <Menu.Item key="/about" icon={<VideoCameraOutlined />}>
                        功能详情列表
                    </Menu.Item>
                    <Menu.Item key="/system/users" icon={<UploadOutlined />}>
                        其他功能
                    </Menu.Item>
                </Menu>*/}
            </Sider>
            <Layout className="site-layout">
                <Header className="site-layout-background" style={{ padding: 0 }}>
                    {React.createElement(collapsed ? MenuUnfoldOutlined : MenuFoldOutlined, {
                        className: 'trigger',
                        onClick: () => setCollapsed(!collapsed),
                    })}
                </Header>
                <Content
                    className="site-layout-background"
                    style={{
                        margin: '24px 16px',
                        padding: 24,
                        minHeight: 280,
                    }}
                >
                   {children}
                </Content>
            </Layout>
        </Layout>
    );
};

export default AppLayout;