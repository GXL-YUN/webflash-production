// src/components/Tree/index2.tsx 或 TreeApp.tsx
import React, {useState, useEffect,useCallback} from 'react';
import MultiLevelTable from './components/MultiLevelTable2';
import { TreeNode } from './types/data2';
import {Layout, Drawer, message, Button, Space, type DrawerProps} from 'antd';

const { Header, Sider, Content } = Layout;

// 示例数据
const mockData: TreeNode[] = [
    {
        key: '0',
        title: '系统管理',
        level: 0,
        children: [
            {
                key: '0-0',
                title: '用户管理',
                level: 1,
                children: [
                    {
                        key: '0-0-0',
                        title: '用户列表',
                        level: 2,
                        url: '/user/list'
                    },
                    {
                        key: '0-0-1',
                        title: '角色管理',
                        level: 2,
                        url: '/user/roles'
                    }
                ]
            },
            {
                key: '0-1',
                title: '菜单管理',
                level: 1,
                url: '/menu'
            }
        ]
    },
    {
        key: '1',
        title: '内容管理',
        level: 0,
        children: [
            {
                key: '1-0',
                title: '文章管理',
                level: 1,
                url: '/article'
            }
        ]
    }
];

// TreeApp 组件
const TreeApp: React.FC = () => {
    const [placement, setPlacement] = useState<DrawerProps['placement']>('right');
    const [editopen, setEditOpen] = useState(false);
    const [drawerSize, setDrawerSize] = useState<number>(1500);
    const [nowTree, setNowTree] = useState<TreeNode>(); //当前节点

    // 消息相关状态
    const [shouldShowMessage, setShouldShowMessage] = useState(false);
    const [messageContent, setMessageContent] = useState('');
    const [messageApi, contextHolder] = message.useMessage();

    const [loading, setLoading] = useState(false);

    //初始化树层级
    const loadChartData = useCallback(async (filterParams?: any) => {
        try {
            setLoading(true);
            const requestData = filterParams ? {
                ...filterParams,
            } : {
                FD_TEMPLATE_ID: "1iankgkm0w35wroogw1ocask5h3nmbt3i7w0",
                fd_subject: "",
                fd_number: ""
            };
            console.log('初始化树',requestData);
            //加载数据
            //const result = await mockDataService.fetchData(requestData)
        } catch (error) {
            console.error('加载模板数据失败:', error);
        } finally {
            setLoading(false);
        }
    }, []);


    // 组件挂载时的初始化
    useEffect(() => {
        loadChartData()
        // 组件挂载时显示消息
        setMessageContent('组件加载成功');
        setShouldShowMessage(true);
    }, []);

    // 处理消息显示
    useEffect(() => {
        if (shouldShowMessage && messageContent) {
            console.log('准备显示消息:', messageContent);
            messageApi.success(messageContent);
            setShouldShowMessage(false);
        }
    }, [shouldShowMessage, messageContent, messageApi]);

    // 处理节点点击
    const handleNodeClick = (node: TreeNode) => {
        console.log('点击节点:', node);
        setNowTree(node);
        setEditOpen(true);

        // 点击节点时显示消息
        setMessageContent(`已选中节点: ${node.title}`);
        setShouldShowMessage(true);
    };

    // 处理按钮点击
    const handleButtonClick = () => {
        setMessageContent('操作成功');
        setShouldShowMessage(true);
    };

    // 关闭抽屉时的处理
    const handleCloseDrawer = () => {
        setEditOpen(false);
        setMessageContent('抽屉已关闭');
        setShouldShowMessage(true);
    };

    return (
        <div style={{ padding: 24 }}>
            {/* 必须渲染 contextHolder */}
            {contextHolder}
            <h1>树形数据展示</h1>
            <MultiLevelTable
                dataSource={mockData}
                defaultExpandAll={false}
                showMergedView={false}
                onNodeClick={handleNodeClick}
                showNodeLink={true}
            />

            {/* 右侧弹出抽屉数据 */}
            <Drawer
                title={`节点详情 - ${nowTree?.title || '未选择'}`}
                placement={placement}
                onClose={handleCloseDrawer}
                open={editopen}
                width={drawerSize}

                loading={loading}
                extra={
                    <Space>
                        <Button onClick={handleCloseDrawer}>取消</Button>
                        <Button type="primary" onClick={handleButtonClick}>
                            确定
                        </Button>
                    </Space>
                }
                styles={{
                    body: {
                        padding: 24,
                    }
                }}
            >
                <div style={{ padding: 20 }}>
                    <h2>节点信息</h2>
                    <p><strong>Key:</strong> {nowTree?.key}</p>
                    <p><strong>标题:</strong> {nowTree?.title}</p>
                    <p><strong>层级:</strong> {nowTree?.level}</p>
                    <p><strong>URL:</strong> {nowTree?.url || '无'}</p>

                    <div style={{ marginTop: 20 }}>
                        <h3>子节点</h3>
                        {nowTree?.children ? (
                            <ul>
                                {nowTree.children.map(child => (
                                    <li key={child.key}>{child.title}</li>
                                ))}
                            </ul>
                        ) : (
                            <p>无子节点</p>
                        )}
                    </div>

                    <Button
                        type="primary"
                        onClick={handleButtonClick}
                        style={{ marginTop: 20 }}
                    >
                        测试消息按钮
                    </Button>
                </div>
            </Drawer>

            {/* 测试区域 */}
            <div style={{ marginTop: 20, padding: 20, background: '#f0f0f0' }}>
                <h3>功能测试</h3>
                <Space>
                    <Button onClick={handleButtonClick}>
                        测试消息显示
                    </Button>
                    <Button onClick={() => {
                        setDrawerSize(500);
                        setMessageContent('抽屉大小已调整为500px');
                        setShouldShowMessage(true);
                    }}>
                        设置抽屉宽度为500
                    </Button>
                    <Button onClick={() => {
                        setDrawerSize(1500);
                        setMessageContent('抽屉大小已调整为1500px');
                        setShouldShowMessage(true);
                    }}>
                        设置抽屉宽度为1500
                    </Button>
                </Space>
            </div>
        </div>
    );
};

export default TreeApp;