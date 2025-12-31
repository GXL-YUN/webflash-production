import React, { useState, useEffect, CSSProperties } from 'react';
import { Layout, Menu, theme, ConfigProvider, MenuProps } from 'antd';
import {
    MenuUnfoldOutlined,
    MenuFoldOutlined,
    HomeOutlined,
    BarChartOutlined,
    UnorderedListOutlined,
    FileTextOutlined,
    TableOutlined,
    SettingOutlined
} from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import './AppLayout.css'; // 创建一个新的CSS文件

const { Header, Sider, Content } = Layout;

interface AppLayoutProps {
    children: React.ReactNode;
}

type MenuItem = Required<MenuProps>['items'][number];

const AppLayout: React.FC<AppLayoutProps> = ({ children }) => {
    const [collapsed, setCollapsed] = useState(false);
    const [activeKey, setActiveKey] = useState<string>('/');
    const [isHovered, setIsHovered] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const { token } = theme.useToken();

    // 监听路由变化，更新激活菜单
    useEffect(() => {
        const currentPath = location.pathname;
        setActiveKey(currentPath);
    }, [location.pathname]);

    // 定义菜单项
    const menuItems: MenuItem[] = [
        {
            key: '/',
            label: '首页',
            icon: <HomeOutlined style={{ fontSize: '18px' }} />
        },
        {
            key: '/about',
            label: '数据报表',
            icon: <BarChartOutlined style={{ fontSize: '18px' }} />
        },
        {
            key: '/demand',
            label: '需求管理',
            icon: <UnorderedListOutlined style={{ fontSize: '18px' }} />,
            children: [
                { key: '/demand/list', label: '需求列表', icon: <UnorderedListOutlined /> },
                { key: '/demand/detail:id', label: '需求详情', icon: <FileTextOutlined /> },
            ]
        },
        {
            key: '/table',
            label: '模板查询',
            icon: <TableOutlined style={{ fontSize: '18px' }} />
        },
        {
            key: '/settings',
            label: '系统设置',
            icon: <SettingOutlined style={{ fontSize: '18px' }} />
        },
    ];

    const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
        setActiveKey(key);
        navigate(key);
    };

    // 处理鼠标事件
    const handleMouseEnter = (e: React.MouseEvent<HTMLSpanElement>) => {
        const target = e.currentTarget as HTMLSpanElement;
        target.style.background = 'rgba(255, 255, 255, 0.1)';
        target.style.color = '#fff';
    };

    const handleMouseLeave = (e: React.MouseEvent<HTMLSpanElement>) => {
        const target = e.currentTarget as HTMLSpanElement;
        target.style.background = 'transparent';
        target.style.color = 'rgba(255, 255, 255, 0.85)';
    };

    return (
        <ConfigProvider
            theme={{
                token: {
                    colorPrimary: '#1890ff',
                    borderRadius: 8,
                },
                components: {
                    Layout: {
                        headerBg: '#ffffff',
                        headerPadding: '0 24px',
                        bodyBg: 'transparent',
                    },
                    Menu: {
                        darkItemBg: 'rgba(255, 255, 255, 0.08)',
                        darkItemColor: 'rgba(255, 255, 255, 0.85)',
                        darkItemHoverBg: 'rgba(255, 255, 255, 0.15)',
                        darkItemSelectedBg: '#1890ff',
                        darkItemSelectedColor: '#ffffff',
                        itemHeight: 48,
                        itemMarginBlock: 8,
                        itemMarginInline: 4,
                        itemBorderRadius: 8,
                    },
                },
            }}
        >
            <Layout className="app-layout" style={{
                minHeight: '100vh',
                backgroundImage: 'url("https://images.unsplash.com/photo-1519681393784-d120267933ba?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80")',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundAttachment: 'fixed',
                position: 'relative',
            } as CSSProperties}>
                {/* 模糊背景层 */}
                <div className="background-overlay" />

                <Layout className="content-layout" style={{
                    position: 'relative',
                    zIndex: 1,
                    minHeight: '100vh',
                    transition: 'all 0.3s',
                    marginLeft: collapsed ? 80 : 200,
                } as CSSProperties}>
                    <Sider
                        width={200}
                        collapsedWidth={80}
                        trigger={null}
                        collapsible
                        collapsed={collapsed}
                        style={{
                            height: '100vh',
                            position: 'fixed',
                            left: 0,
                            top: 0,
                            bottom: 0,
                            zIndex: 100,
                            background: 'rgba(0, 21, 41, 0.85)',
                            backdropFilter: 'blur(10px)',
                            WebkitBackdropFilter: 'blur(10px)',
                            borderRight: '1px solid rgba(255, 255, 255, 0.1)',
                            boxShadow: '2px 0 8px rgba(0, 0, 0, 0.15)',
                            transition: 'all 0.3s cubic-bezier(0.2, 0, 0, 1)',
                        } as CSSProperties}
                        className="sidebar"
                        onMouseEnter={() => setIsHovered(true)}
                        onMouseLeave={() => setIsHovered(false)}
                    >
                        {/* Logo区域 */}
                        <div className="logo-container" style={{
                            height: 64,
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
                            overflow: 'hidden',
                            padding: collapsed ? '0 16px' : '0 24px',
                        } as CSSProperties}>
                            {collapsed ? (
                                <div style={{
                                    fontSize: 20,
                                    fontWeight: 'bold',
                                    background: 'linear-gradient(45deg, #1890ff, #722ed1)',
                                    WebkitBackgroundClip: 'text',
                                    WebkitTextFillColor: 'transparent',
                                } as CSSProperties}>
                                    S
                                </div>
                            ) : (
                                <div style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: 8,
                                } as CSSProperties}>
                                    <div style={{
                                        width: 32,
                                        height: 32,
                                        borderRadius: 6,
                                        background: 'linear-gradient(45deg, #1890ff, #722ed1)',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        fontSize: 18,
                                        fontWeight: 'bold',
                                        color: '#fff',
                                    } as CSSProperties}>
                                        S
                                    </div>
                                    <span style={{
                                        fontSize: 18,
                                        fontWeight: 'bold',
                                        color: '#fff',
                                        opacity: 0.9,
                                    } as CSSProperties}>
                    Admin Pro
                  </span>
                                </div>
                            )}
                        </div>

                        {/* 导航菜单 */}
                        <Menu
                            theme="dark"
                            mode="inline"
                            selectedKeys={[activeKey]}
                            openKeys={collapsed && !isHovered ? [] : ['/demand']}
                            items={menuItems}
                            onClick={handleMenuClick}
                            style={{
                                border: 'none',
                                background: 'transparent',
                                padding: '16px 8px',
                                transition: 'all 0.3s',
                            } as CSSProperties}
                            className="custom-menu"
                        />

                        {/* 折叠按钮 */}
                        <div style={{
                            position: 'absolute',
                            bottom: 0,
                            width: '100%',
                            borderTop: '1px solid rgba(255, 255, 255, 0.1)',
                            padding: '16px 0',
                            display: 'flex',
                            justifyContent: 'center',
                        } as CSSProperties}>
                            {collapsed ? (
                                <MenuUnfoldOutlined
                                    style={{
                                        fontSize: 18,
                                        color: 'rgba(255, 255, 255, 0.85)',
                                        cursor: 'pointer',
                                        padding: 8,
                                        borderRadius: 6,
                                        transition: 'all 0.3s',
                                    } as CSSProperties}
                                    className="collapse-btn"
                                    onClick={() => setCollapsed(!collapsed)}
                                    onMouseEnter={handleMouseEnter}
                                    onMouseLeave={handleMouseLeave}
                                />
                            ) : (
                                <MenuFoldOutlined
                                    style={{
                                        fontSize: 18,
                                        color: 'rgba(255, 255, 255, 0.85)',
                                        cursor: 'pointer',
                                        padding: 8,
                                        borderRadius: 6,
                                        transition: 'all 0.3s',
                                    } as CSSProperties}
                                    className="collapse-btn"
                                    onClick={() => setCollapsed(!collapsed)}
                                    onMouseEnter={handleMouseEnter}
                                    onMouseLeave={handleMouseLeave}
                                />
                            )}
                        </div>
                    </Sider>

              {/*      <Header style={{*/}
              {/*          padding: 0,*/}
              {/*          background: 'rgba(255, 255, 255, 0.9)',*/}
              {/*          backdropFilter: 'blur(10px)',*/}
              {/*          WebkitBackdropFilter: 'blur(10px)',*/}
              {/*          borderBottom: '1px solid rgba(0, 0, 0, 0.06)',*/}
              {/*          display: 'flex',*/}
              {/*          alignItems: 'center',*/}
              {/*          justifyContent: 'space-between',*/}
              {/*          paddingLeft: 24,*/}
              {/*          paddingRight: 24,*/}
              {/*          position: 'sticky',*/}
              {/*          top: 0,*/}
              {/*          zIndex: 99,*/}
              {/*          boxShadow: '0 2px 8px rgba(0, 0, 0, 0.06)',*/}
              {/*      } as CSSProperties}>*/}
              {/*          <div style={{ display: 'flex', alignItems: 'center', gap: 8 } as CSSProperties}>*/}
              {/*              <div style={{*/}
              {/*                  width: 8,*/}
              {/*                  height: 8,*/}
              {/*                  borderRadius: '50%',*/}
              {/*                  background: '#52c41a',*/}
              {/*                  animation: 'pulse 2s infinite',*/}
              {/*              } as CSSProperties} />*/}
              {/*              <span style={{ fontSize: 16, fontWeight: 500, color: token.colorText } as CSSProperties}>*/}
              {/*  工作台*/}
              {/*</span>*/}
              {/*          </div>*/}

              {/*          <div style={{ display: 'flex', alignItems: 'center', gap: 16 } as CSSProperties}>*/}
              {/*<span style={{ color: token.colorTextSecondary } as CSSProperties}>*/}
              {/*  {new Date().toLocaleDateString('zh-CN', {*/}
              {/*      weekday: 'long',*/}
              {/*      year: 'numeric',*/}
              {/*      month: 'long',*/}
              {/*      day: 'numeric'*/}
              {/*  })}*/}
              {/*</span>*/}
              {/*          </div>*/}
              {/*      </Header>*/}

                    <Content style={{
                        margin: 24,
                        padding: 24,
                        background: 'rgba(255, 255, 255, 0.85)',
                        backdropFilter: 'blur(20px)',
                        WebkitBackdropFilter: 'blur(20px)',
                        borderRadius: 12,
                        boxShadow: '0 8px 32px rgba(0, 0, 0, 0.08)',
                        border: '1px solid rgba(255, 255, 255, 0.2)',
                        minHeight: 280,
                        position: 'relative',
                        overflow: 'hidden',
                    } as CSSProperties}>
                        {/* 内容装饰元素 */}
                        <div style={{
                            position: 'absolute',
                            top: -100,
                            right: -100,
                            width: 300,
                            height: 300,
                            borderRadius: '50%',
                            background: 'linear-gradient(45deg, rgba(24, 144, 255, 0.1), rgba(114, 46, 209, 0.1))',
                            filter: 'blur(40px)',
                            zIndex: 0,
                        } as CSSProperties} />

                        <div style={{
                            position: 'absolute',
                            bottom: -50,
                            left: -50,
                            width: 200,
                            height: 200,
                            borderRadius: '50%',
                            background: 'linear-gradient(45deg, rgba(82, 196, 26, 0.1), rgba(250, 219, 20, 0.1))',
                            filter: 'blur(40px)',
                            zIndex: 0,
                        } as CSSProperties} />

                        <div style={{ position: 'relative', zIndex: 1 } as CSSProperties}>
                            {children}
                        </div>
                    </Content>
                </Layout>
            </Layout>

            <style>{`
        @keyframes pulse {
          0% { opacity: 0.6; }
          50% { opacity: 1; }
          100% { opacity: 0.6; }
        }
        
        .custom-menu .ant-menu-item {
          transition: all 0.3s cubic-bezier(0.2, 0, 0, 1) !important;
        }
        
        .custom-menu .ant-menu-item:hover {
          transform: translateX(4px);
        }
        
        .custom-menu .ant-menu-item-selected {
          box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
        }
        
        .background-overlay {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.3);
          backdrop-filter: blur(20px);
          z-index: 0;
        }
      `}</style>
        </ConfigProvider>
    );
};

export default AppLayout;