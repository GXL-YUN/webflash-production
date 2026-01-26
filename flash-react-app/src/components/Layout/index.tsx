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
    SettingOutlined,
    DashboardOutlined,
    TeamOutlined,
    ProfileOutlined,
    ProjectOutlined,
    AuditOutlined,
    AppstoreOutlined
} from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import './AppLayout.css';

const { Header, Sider, Content } = Layout;

interface AppLayoutProps {
    children: React.ReactNode;
}

type MenuItem = Required<MenuProps>['items'][number];

interface MenuItemWithChildren {
    key: string;
    label: React.ReactNode;
    icon?: React.ReactNode;
    children?: MenuItemWithChildren[];
    className?: string;
}

const AppLayout: React.FC<AppLayoutProps> = ({ children }) => {
    const [collapsed, setCollapsed] = useState(false);
    const [activeKey, setActiveKey] = useState<string>('/');
    const [openKeys, setOpenKeys] = useState<string[]>([]);
    const [isHovered, setIsHovered] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const { token } = theme.useToken();

    // 监听路由变化，更新激活菜单
    useEffect(() => {
        const currentPath = location.pathname;
        setActiveKey(currentPath);

        // 自动展开当前菜单项的父级菜单
        const parentKeys = menuItems
            .filter((item: any) =>
                item?.children?.some((child: any) => child?.key === currentPath)
            )
            .map(item => item?.key as string);

        if (parentKeys.length > 0 && !collapsed) {
            setOpenKeys(parentKeys);
        }
    }, [location.pathname, collapsed]);

    // 处理菜单展开/收起
    const onOpenChange = (keys: string[]) => {
        const latestOpenKey = keys.find(key => openKeys.indexOf(key) === -1);
        if (latestOpenKey && (menuItems as any).find((item: any) => item?.key === latestOpenKey)?.children) {
            setOpenKeys([latestOpenKey]);
        } else {
            setOpenKeys([]);
        }
    };

    // 定义菜单项
    const menuItems: MenuItem[] = [
        {
            key: '/',
            label: '首页概览',
            icon: <DashboardOutlined style={{ fontSize: '18px' }} />,
            className: 'main-menu-item'
        },
        {
            key: '/data',
            label: '数据报表',
            icon: <BarChartOutlined style={{ fontSize: '18px' }} />,
            className: 'main-menu-item',
            children: [
                {
                    key: '/data/adout',
                    label: '数据看板',
                    icon: <DashboardOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/data/analysis',
                    label: '数据分析',
                    icon: <BarChartOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/data/report',
                    label: '数据报表',
                    icon: <ProfileOutlined />,
                    className: 'sub-menu-item'
                }
            ]
        },

        {
            key: '/table',
            label: '表单表格',
            icon: <BarChartOutlined style={{ fontSize: '18px' }} />,
            className: 'main-menu-item',
            children: [
                {
                    key: '/table/RoomTable',
                    label: '模板表格',
                    icon: <DashboardOutlined />,
                    className: 'sub-menu-item'
                }


            ]
        },
        {
            key: '/demand',
            label: '需求管理',
            icon: <ProjectOutlined style={{ fontSize: '18px' }} />,
            className: 'main-menu-item',
            children: [
                {
                    key: '/demand/list',
                    label: '需求列表',
                    icon: <UnorderedListOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/demand/create',
                    label: '新建需求',
                    icon: <FileTextOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/demand/approval',
                    label: '需求审批',
                    icon: <AuditOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/demand/archive',
                    label: '需求归档',
                    icon: <ProfileOutlined />,
                    className: 'sub-menu-item'
                }
            ]
        },
        {
            key: '/template',
            label: '模板中心',
            icon: <AppstoreOutlined style={{ fontSize: '18px' }} />,
            className: 'main-menu-item',
            children: [
                {
                    key: '/template/query',
                    label: '模板查询',
                    icon: <TableOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/template/create',
                    label: '创建模板',
                    icon: <FileTextOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/template/manage',
                    label: '模板管理',
                    icon: <ProfileOutlined />,
                    className: 'sub-menu-item'
                }
            ]
        },
        {
            key: '/system',
            label: '系统设置',
            icon: <SettingOutlined style={{ fontSize: '18px' }} />,
            className: 'main-menu-item',
            children: [
                {
                    key: '/system/user',
                    label: '用户管理',
                    icon: <TeamOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/system/role',
                    label: '角色权限',
                    icon: <UnorderedListOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/system/config',
                    label: '系统配置',
                    icon: <SettingOutlined />,
                    className: 'sub-menu-item'
                },
                {
                    key: '/system/log',
                    label: '操作日志',
                    icon: <AuditOutlined />,
                    className: 'sub-menu-item'
                }
            ]
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
                        darkItemSelectedBg: 'linear-gradient(90deg, #1890ff, #52c41a)',
                        darkItemSelectedColor: '#ffffff',
                        darkSubMenuItemBg: 'rgba(0, 0, 0, 0.2)',
                        itemHeight: 44,
                        itemMarginBlock: 6,
                        itemMarginInline: 8,
                        itemBorderRadius: 8,
                        subMenuItemBorderRadius: 6,
                        horizontalItemSelectedBg: '#1890ff',
                        horizontalItemSelectedColor: '#ffffff',
                        horizontalItemHoverBg: 'rgba(255, 255, 255, 0.1)',
                        horizontalItemHoverColor: '#ffffff',
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
                    transition: 'all 0.3s cubic-bezier(0.2, 0, 0, 1)',
                    marginLeft: collapsed ? 80 : 240,
                } as CSSProperties}>
                    <Sider
                        width={240}
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
                            boxShadow: '2px 0 20px rgba(0, 0, 0, 0.2)',
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
                            transition: 'all 0.3s cubic-bezier(0.2, 0, 0, 1)',
                        } as CSSProperties}>
                            {collapsed ? (
                                <div style={{
                                    fontSize: 24,
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
                                    gap: 12,
                                } as CSSProperties}>
                                    <div style={{
                                        width: 36,
                                        height: 36,
                                        borderRadius: 8,
                                        background: 'linear-gradient(135deg, #1890ff, #722ed1)',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        fontSize: 20,
                                        fontWeight: 'bold',
                                        color: '#fff',
                                        boxShadow: '0 4px 12px rgba(24, 144, 255, 0.3)',
                                    } as CSSProperties}>
                                        S
                                    </div>
                                    <div style={{
                                        display: 'flex',
                                        flexDirection: 'column',
                                    } as CSSProperties}>
                                        <span style={{
                                            fontSize: 18,
                                            fontWeight: 'bold',
                                            color: '#fff',
                                            opacity: 0.95,
                                            lineHeight: 1.2,
                                        } as CSSProperties}>
                                            Admin Pro
                                        </span>
                                        <span style={{
                                            fontSize: 12,
                                            color: 'rgba(255, 255, 255, 0.6)',
                                            opacity: 0.8,
                                            lineHeight: 1.2,
                                        } as CSSProperties}>
                                            v2.0.0
                                        </span>
                                    </div>
                                </div>
                            )}
                        </div>

                        {/* 导航菜单 */}
                        <div style={{
                            height: 'calc(100vh - 128px)',
                            overflowY: 'auto',
                            overflowX: 'hidden',
                        } as CSSProperties}>
                            <Menu
                                theme="dark"
                                mode="inline"
                                selectedKeys={[activeKey]}
                                openKeys={collapsed ? [] : openKeys}
                                onOpenChange={onOpenChange}
                                items={menuItems}
                                onClick={handleMenuClick}
                                style={{
                                    border: 'none',
                                    background: 'transparent',
                                    padding: '12px 8px',
                                    transition: 'all 0.3s',
                                } as CSSProperties}
                                className="custom-menu"
                                inlineIndent={20}
                            />
                        </div>

                        {/* 折叠按钮 */}
                        <div style={{
                            position: 'absolute',
                            bottom: 0,
                            width: '100%',
                            borderTop: '1px solid rgba(255, 255, 255, 0.1)',
                            padding: '16px 0',
                            display: 'flex',
                            justifyContent: 'center',
                            background: 'rgba(0, 0, 0, 0.2)',
                        } as CSSProperties}>
                            {collapsed ? (
                                <MenuUnfoldOutlined
                                    style={{
                                        fontSize: 20,
                                        color: 'rgba(255, 255, 255, 0.85)',
                                        cursor: 'pointer',
                                        padding: 10,
                                        borderRadius: 8,
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
                                        fontSize: 20,
                                        color: 'rgba(255, 255, 255, 0.85)',
                                        cursor: 'pointer',
                                        padding: 10,
                                        borderRadius: 8,
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

                    <Content style={{
                        margin: 0,
                        padding: 0,
                        background: 'transparent',
                        minHeight: 280,
                        position: 'relative',
                        overflow: 'hidden',
                    } as CSSProperties}>
                        {children}
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
          height: 44px !important;
          line-height: 44px !important;
          border-radius: 8px !important;
          margin: 6px 8px !important;
        }
        
        .custom-menu .ant-menu-item:hover {
          transform: translateX(4px) !important;
          box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2) !important;
        }
        
        .custom-menu .ant-menu-item-selected {
          box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3) !important;
          background: linear-gradient(90deg, #1890ff, #52c41a) !important;
        }
        
        .custom-menu .ant-menu-submenu {
          border-radius: 8px !important;
          margin: 6px 8px !important;
        }
        
        .custom-menu .ant-menu-submenu-title {
          transition: all 0.3s cubic-bezier(0.2, 0, 0, 1) !important;
          height: 44px !important;
          line-height: 44px !important;
          border-radius: 8px !important;
        }
        
        .custom-menu .ant-menu-submenu-title:hover {
          transform: translateX(2px) !important;
        }
        
        .custom-menu .ant-menu-submenu-open > .ant-menu-submenu-title {
          background: rgba(255, 255, 255, 0.05) !important;
        }
        
        .custom-menu .ant-menu-sub {
          background: rgba(0, 0, 0, 0.2) !important;
          border-radius: 0 0 8px 8px !important;
          padding: 8px 0 !important;
        }
        
        .custom-menu .ant-menu-sub .ant-menu-item {
          margin: 2px 8px !important;
          padding-left: 40px !important;
          background: transparent !important;
        }
        
        .custom-menu .ant-menu-sub .ant-menu-item:hover {
          background: rgba(255, 255, 255, 0.1) !important;
        }
        
        .custom-menu .ant-menu-sub .ant-menu-item-selected {
          background: linear-gradient(90deg, #1890ff, #52c41a) !important;
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
        
        /* 侧边栏滚动条样式 */
        .sidebar::-webkit-scrollbar {
          width: 4px;
        }
        
        .sidebar::-webkit-scrollbar-track {
          background: rgba(255, 255, 255, 0.05);
          border-radius: 2px;
        }
        
        .sidebar::-webkit-scrollbar-thumb {
          background: rgba(255, 255, 255, 0.2);
          border-radius: 2px;
        }
        
        .sidebar::-webkit-scrollbar-thumb:hover {
          background: rgba(255, 255, 255, 0.3);
        }
        
        /* 菜单项图标动画 */
        .main-menu-item .anticon {
          transition: all 0.3s cubic-bezier(0.2, 0, 0, 1) !important;
        }
        
        .main-menu-item:hover .anticon {
          transform: scale(1.1);
        }
        
        .sub-menu-item .anticon {
          transition: all 0.3s ease !important;
        }
        
        .sub-menu-item:hover .anticon {
          transform: translateX(2px);
        }
        
        /* 折叠按钮动画 */
        .collapse-btn {
          transition: all 0.3s cubic-bezier(0.2, 0, 0, 1) !important;
        }
        
        .collapse-btn:hover {
          transform: scale(1.1) rotate(5deg) !important;
          box-shadow: 0 8px 16px rgba(255, 255, 255, 0.1) !important;
        }
      `}</style>
        </ConfigProvider>
    );
};

export default AppLayout;