// components/DemandManagement/DemandDetail.tsx
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import {
    Card,
    Descriptions,
    Tabs,
    Table,
    Tag,
    Timeline,
    Button,
    Space,
    Modal,
    Form,
    Input,
    Select,
    message,
} from 'antd';
import {
    ArrowLeftOutlined,
    EditOutlined,
    HistoryOutlined,
    PlusOutlined,
} from '@ant-design/icons';
import {IDemand, IDevIssue, IOperationIssue, IChangeRequest, Priority} from './types/demand';
import StatusTag from './StatusTag';
import AppLayout from '../../components/Layout'
const { TextArea } = Input;
const { Option } = Select;

const DemandDetail: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [demand, setDemand] = useState<IDemand | null>(null);
    const [activeTab, setActiveTab] = useState('basic');
    const [isIssueModalVisible, setIsIssueModalVisible] = useState(false);
    const [issueType, setIssueType] = useState<'dev' | 'operation' | 'change'>('dev');
    const [form] = Form.useForm();

    // 模拟数据
    const [devIssues, setDevIssues] = useState<IDevIssue[]>([
        {
            id: 'DEV-001',
            demandId: id!,
            title: '登录接口性能问题',
            description: '登录接口在并发情况下响应时间超过3秒',
            status: 'resolved',
            assignee: '王五',
            createdAt: '2023-10-15',
            resolvedAt: '2023-10-18',
            priority: 'high' as Priority, // 使用类型断言
        },
    ]);

    const [operationIssues, setOperationIssues] = useState<IOperationIssue[]>([
        {
            id: 'OPS-001',
            demandId: id!,
            title: '服务器CPU使用率过高',
            description: '上线后服务器CPU使用率持续在80%以上',
            type: 'performance',
            status: 'fixing',
            severity: 'major',
            createdAt: '2023-10-26',
        },
    ]);

    const [changeRequests, setChangeRequests] = useState<IChangeRequest[]>([
        {
            id: 'CHG-001',
            demandId: id!,
            title: '增加短信验证码重发功能',
            description: '用户反馈需要增加验证码重发按钮',
            changeType: 'scope',
            status: 'approved',
            proposer: '李四',
            createdAt: '2023-10-20',
            reviewedAt: '2023-10-22',
            impactAnalysis: '需要修改前端页面和后端验证码服务',
        },
    ]);

    useEffect(() => {
        // 模拟API调用获取需求详情
        const mockDemand: IDemand = {
            id: id!,
            name: '用户登录优化',
            description: '优化用户登录流程，增加短信验证码登录',
            businessValue: '提升用户登录转化率20%，减少用户流失',
            acceptanceCriteria: '1. 支持手机号+验证码登录\n2. 登录成功率达到99.9%\n3. 页面加载时间小于2秒\n4. 支持错误提示本地化',
            status: 'online' as any,
            priority: 'high' as Priority, // 使用类型断言
            creator: '张三',
            createdAt: '2023-10-01',
            scheduledAt: '2023-10-05',
            developStartAt: '2023-10-10',
            developEndAt: '2023-10-20',
            testStartAt: '2023-10-21',
            testEndAt: '2023-10-25',
            onlineAt: '2023-10-26',
            version: 2,
            modules: ['用户中心', '认证服务', '短信服务'],
            relatedRequirements: ['REQ-2023-002', 'REQ-2023-003'],
            rtmId: 'RTM-001',
        };
        setDemand(mockDemand);
    }, [id]);

    if (!demand) {
        return <div>加载中...</div>;
    }

    const devIssueColumns = [
        {
            title: '问题标题',
            dataIndex: 'title',
            key: 'title',
            width: 200,
        },
        {
            title: '描述',
            dataIndex: 'description',
            key: 'description',
            width: 300,
            ellipsis: true,
        },
        {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            render: (status: string) => {
                const statusMap: Record<string, { color: string; text: string }> = {
                    pending: { color: 'default', text: '待处理' },
                    in_progress: { color: 'processing', text: '处理中' },
                    resolved: { color: 'success', text: '已解决' },
                    closed: { color: 'default', text: '已关闭' },
                };
                const config = statusMap[status];
                return config ? <Tag color={config.color}>{config.text}</Tag> : '-';
            },
        },
        {
            title: '优先级',
            dataIndex: 'priority',
            key: 'priority',
            width: 100,
            render: (priority: string) => {
                const priorityMap: Record<string, { color: string; text: string }> = {
                    high: { color: 'red', text: '高' },
                    medium: { color: 'orange', text: '中' },
                    low: { color: 'green', text: '低' },
                };
                const config = priorityMap[priority];
                return config ? <Tag color={config.color}>{config.text}</Tag> : '-';
            },
        },
        {
            title: '负责人',
            dataIndex: 'assignee',
            key: 'assignee',
            width: 100,
        },
        {
            title: '创建时间',
            dataIndex: 'createdAt',
            key: 'createdAt',
            width: 120,
        },
        {
            title: '解决时间',
            dataIndex: 'resolvedAt',
            key: 'resolvedAt',
            width: 120,
        },
    ];

    const operationIssueColumns = [
        {
            title: '问题标题',
            dataIndex: 'title',
            key: 'title',
            width: 200,
        },
        {
            title: '类型',
            dataIndex: 'type',
            key: 'type',
            width: 100,
            render: (type: string) => {
                const typeMap: Record<string, string> = {
                    bug: '缺陷',
                    performance: '性能',
                    security: '安全',
                    other: '其他',
                };
                return typeMap[type] || type;
            },
        },
        {
            title: '严重程度',
            dataIndex: 'severity',
            key: 'severity',
            width: 100,
            render: (severity: string) => {
                const severityMap: Record<string, { color: string; text: string }> = {
                    critical: { color: 'red', text: '严重' },
                    major: { color: 'orange', text: '主要' },
                    minor: { color: 'green', text: '次要' },
                    trivial: { color: 'default', text: '轻微' },
                };
                const config = severityMap[severity];
                return config ? <Tag color={config.color}>{config.text}</Tag> : '-';
            },
        },
        {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            render: (status: string) => {
                const statusMap: Record<string, { color: string; text: string }> = {
                    open: { color: 'red', text: '待处理' },
                    investigating: { color: 'orange', text: '调查中' },
                    fixing: { color: 'processing', text: '修复中' },
                    verified: { color: 'success', text: '已验证' },
                    closed: { color: 'default', text: '已关闭' },
                };
                const config = statusMap[status];
                return config ? <Tag color={config.color}>{config.text}</Tag> : '-';
            },
        },
    ];

    const changeRequestColumns = [
        {
            title: '变更标题',
            dataIndex: 'title',
            key: 'title',
            width: 200,
        },
        {
            title: '变更类型',
            dataIndex: 'changeType',
            key: 'changeType',
            width: 100,
            render: (type: string) => {
                const typeMap: Record<string, string> = {
                    scope: '范围',
                    schedule: '进度',
                    cost: '成本',
                    quality: '质量',
                };
                return typeMap[type] || type;
            },
        },
        {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            render: (status: string) => {
                const statusMap: Record<string, { color: string; text: string }> = {
                    submitted: { color: 'default', text: '已提交' },
                    reviewing: { color: 'processing', text: '评审中' },
                    approved: { color: 'success', text: '已批准' },
                    rejected: { color: 'red', text: '已拒绝' },
                    implemented: { color: 'green', text: '已实施' },
                };
                const config = statusMap[status];
                return config ? <Tag color={config.color}>{config.text}</Tag> : '-';
            },
        },
        {
            title: '影响分析',
            dataIndex: 'impactAnalysis',
            key: 'impactAnalysis',
            width: 300,
            ellipsis: true,
        },
    ];

    const handleAddIssue = () => {
        form.validateFields().then(values => {
            const newIssue = {
                id: `${issueType.toUpperCase()}-${Date.now()}`,
                demandId: id!,
                ...values,
                createdAt: new Date().toISOString().split('T')[0],
            };

            switch (issueType) {
                case 'dev':
                    setDevIssues([...devIssues, newIssue as IDevIssue]);
                    break;
                case 'operation':
                    setOperationIssues([...operationIssues, newIssue as IOperationIssue]);
                    break;
                case 'change':
                    setChangeRequests([...changeRequests, newIssue as IChangeRequest]);
                    break;
            }

            setIsIssueModalVisible(false);
            form.resetFields();
            message.success('问题添加成功');
        });
    };

    return (<AppLayout>
        <div style={{ padding: 24 }}>
            {/* 头部导航 */}
            <div style={{ marginBottom: 16 }}>
                <Space>
                    <Button
                        type="link"
                        icon={<ArrowLeftOutlined />}
                        onClick={() => window.history.back()}
                    >
                        返回列表
                    </Button>
                    <Button icon={<EditOutlined />}>编辑需求</Button>
                    <Button icon={<HistoryOutlined />}>变更历史</Button>
                </Space>
            </div>

            {/* 上三层：基本信息 */}
            <Card title="需求基本信息" style={{ marginBottom: 16 }}>
                <Descriptions bordered column={2}>
                    <Descriptions.Item label="需求编号">{demand.id}</Descriptions.Item>
                    <Descriptions.Item label="需求名称">{demand.name}</Descriptions.Item>
                    <Descriptions.Item label="当前状态">
                        <StatusTag status={demand.status} />
                    </Descriptions.Item>
                    <Descriptions.Item label="优先级">
                        <Tag color={demand.priority === 'high' ? 'red' : demand.priority === 'medium' ? 'orange' : 'green'}>
                            {demand.priority === 'high' ? '高' : demand.priority === 'medium' ? '中' : '低'}
                        </Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="创建人">{demand.creator}</Descriptions.Item>
                    <Descriptions.Item label="创建时间">{demand.createdAt}</Descriptions.Item>
                    <Descriptions.Item label="版本号">V{demand.version}</Descriptions.Item>
                    <Descriptions.Item label="RTM编号">{demand.rtmId || '-'}</Descriptions.Item>
                    <Descriptions.Item label="涉及模块" span={2}>
                        {demand.modules.map(module => (
                            <Tag key={module} color="blue" style={{ margin: '2px' }}>
                                {module}
                            </Tag>
                        ))}
                    </Descriptions.Item>
                    <Descriptions.Item label="关联需求" span={2}>
                        {demand.relatedRequirements.map(req => (
                            <a key={req} style={{ marginRight: 8 }}>
                                {req}
                            </a>
                        ))}
                    </Descriptions.Item>
                </Descriptions>
            </Card>

            {/* 中层：详细描述 */}
            <Card title="需求详细描述" style={{ marginBottom: 16 }}>
                <Tabs activeKey={activeTab} onChange={setActiveTab}>
                    <Tabs.TabPane tab="业务描述" key="business">
                        <div style={{ padding: 16, background: '#fafafa', borderRadius: 4 }}>
                            <h4>需求描述：</h4>
                            <p style={{ whiteSpace: 'pre-wrap' }}>{demand.description}</p>

                            <h4 style={{ marginTop: 16 }}>业务价值：</h4>
                            <p>{demand.businessValue}</p>

                            <h4 style={{ marginTop: 16 }}>验收标准：</h4>
                            <pre style={{ whiteSpace: 'pre-wrap' }}>{demand.acceptanceCriteria}</pre>
                        </div>
                    </Tabs.TabPane>

                    <Tabs.TabPane tab="时间轴" key="timeline">
                        <Timeline style={{ marginTop: 20 }}>
                            <Timeline.Item color="green">
                                <p>创建需求</p>
                                <p>{demand.createdAt}</p>
                            </Timeline.Item>
                            {demand.scheduledAt && (
                                <Timeline.Item color="blue">
                                    <p>已排期</p>
                                    <p>{demand.scheduledAt}</p>
                                </Timeline.Item>
                            )}
                            {demand.developStartAt && (
                                <Timeline.Item color="blue">
                                    <p>开发开始</p>
                                    <p>{demand.developStartAt}</p>
                                </Timeline.Item>
                            )}
                            {demand.developEndAt && (
                                <Timeline.Item color="blue">
                                    <p>开发结束</p>
                                    <p>{demand.developEndAt}</p>
                                </Timeline.Item>
                            )}
                            {demand.testStartAt && (
                                <Timeline.Item color="orange">
                                    <p>测试开始</p>
                                    <p>{demand.testStartAt}</p>
                                </Timeline.Item>
                            )}
                            {demand.testEndAt && (
                                <Timeline.Item color="orange">
                                    <p>测试结束</p>
                                    <p>{demand.testEndAt}</p>
                                </Timeline.Item>
                            )}
                            {demand.onlineAt && (
                                <Timeline.Item color="green">
                                    <p>已上线</p>
                                    <p>{demand.onlineAt}</p>
                                </Timeline.Item>
                            )}
                        </Timeline>
                    </Tabs.TabPane>
                </Tabs>
            </Card>

            {/* 下层：问题跟踪 */}
            <Card title="问题跟踪">
                <Tabs
                    tabBarExtraContent={
                        <Button
                            type="primary"
                            icon={<PlusOutlined />}
                            onClick={() => setIsIssueModalVisible(true)}
                        >
                            新增问题
                        </Button>
                    }
                >
                    <Tabs.TabPane tab="开发进度问题" key="dev">
                        <Table
                            rowKey="id"
                            columns={devIssueColumns}
                            dataSource={devIssues}
                            pagination={false}
                        />
                    </Tabs.TabPane>

                    <Tabs.TabPane tab="运维问题" key="operation">
                        <Table
                            rowKey="id"
                            columns={operationIssueColumns}
                            dataSource={operationIssues}
                            pagination={false}
                        />
                    </Tabs.TabPane>

                    <Tabs.TabPane tab="变更问题" key="change">
                        <Table
                            rowKey="id"
                            columns={changeRequestColumns}
                            dataSource={changeRequests}
                            pagination={false}
                        />
                    </Tabs.TabPane>
                </Tabs>
            </Card>

            {/* 新增问题模态框 */}
            <Modal
                title="新增问题"
                open={isIssueModalVisible}
                onCancel={() => {
                    setIsIssueModalVisible(false);
                    form.resetFields();
                }}
                onOk={handleAddIssue}
                width={600}
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="type"
                        label="问题类型"
                        initialValue="dev"
                        rules={[{ required: true }]}
                    >
                        <Select onChange={setIssueType}>
                            <Option value="dev">开发问题</Option>
                            <Option value="operation">运维问题</Option>
                            <Option value="change">变更问题</Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="title"
                        label="问题标题"
                        rules={[{ required: true, message: '请输入问题标题' }]}
                    >
                        <Input placeholder="请输入问题标题" />
                    </Form.Item>

                    <Form.Item
                        name="description"
                        label="问题描述"
                        rules={[{ required: true, message: '请输入问题描述' }]}
                    >
                        <TextArea rows={4} placeholder="请详细描述问题" />
                    </Form.Item>

                    {issueType === 'dev' && (
                        <>
                            <Form.Item
                                name="priority"
                                label="优先级"
                                initialValue="medium"
                                rules={[{ required: true }]}
                            >
                                <Select>
                                    <Option value="high">高</Option>
                                    <Option value="medium">中</Option>
                                    <Option value="low">低</Option>
                                </Select>
                            </Form.Item>

                            <Form.Item
                                name="assignee"
                                label="负责人"
                                rules={[{ required: true, message: '请选择负责人' }]}
                            >
                                <Input placeholder="请输入负责人姓名" />
                            </Form.Item>
                        </>
                    )}

                    {issueType === 'operation' && (
                        <>
                            <Form.Item
                                name="severity"
                                label="严重程度"
                                initialValue="minor"
                                rules={[{ required: true }]}
                            >
                                <Select>
                                    <Option value="critical">严重</Option>
                                    <Option value="major">主要</Option>
                                    <Option value="minor">次要</Option>
                                    <Option value="trivial">轻微</Option>
                                </Select>
                            </Form.Item>
                        </>
                    )}

                    {issueType === 'change' && (
                        <Form.Item
                            name="impactAnalysis"
                            label="影响分析"
                            rules={[{ required: true, message: '请输入影响分析' }]}
                        >
                            <TextArea rows={3} placeholder="请描述此次变更的影响" />
                        </Form.Item>
                    )}
                </Form>
            </Modal>
        </div></AppLayout>
    );
};

export default DemandDetail;