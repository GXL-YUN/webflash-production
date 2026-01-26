

// components/DemandManagement/DemandList.tsx
import React, { useState } from 'react';
import {
    Table,
    Card,
    Button,
    Input,
    Select,
    DatePicker,
    Space,
    Tag,
    Tooltip,
    Modal,
    Form,
    message,
} from 'antd';
import {
    SearchOutlined,
    PlusOutlined,
    ImportOutlined,
    ExportOutlined,
    EditOutlined,
    DeleteOutlined,
} from '@ant-design/icons';
import { DemandStatus, Priority, IDemand } from './types/demand';
import StatusTag from './StatusTag';
import ImportExport from './ImportExport';
import dayjs from 'dayjs';

import AppLayout from '../../components/Layout'

const { RangePicker } = DatePicker;
const { Option } = Select;

const DemandList: React.FC = () => {
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [searchParams, setSearchParams] = useState({
        name: '',
        status: undefined as DemandStatus | undefined,
        priority: undefined as Priority | undefined,
        dateRange: [] as string[],
    });
    const [isImportModalVisible, setIsImportModalVisible] = useState(false);
    const [isExportModalVisible, setIsExportModalVisible] = useState(false);
    const [form] = Form.useForm();

    // 模拟数据
    const [demands, setDemands] = useState<IDemand[]>([
        {
            id: 'REQ-2023-001',
            name: '用户登录优化',
            description: '优化用户登录流程，增加短信验证码登录',
            businessValue: '提升用户登录转化率20%',
            acceptanceCriteria: '1. 支持手机号+验证码登录\n2. 登录成功率达到99.9%\n3. 页面加载时间小于2秒',
            status: DemandStatus.ONLINE,
            priority: Priority.HIGH,
            creator: '张三',
            createdAt: '2023-10-01',
            scheduledAt: '2023-10-05',
            developStartAt: '2023-10-10',
            developEndAt: '2023-10-20',
            testStartAt: '2023-10-21',
            testEndAt: '2023-10-25',
            onlineAt: '2023-10-26',
            closedAt: '2023-10-30',
            version: 1,
            modules: ['用户中心', '认证服务'],
            relatedRequirements: ['REQ-2023-002'],
            rtmId: 'RTM-001',
        },
        // 更多模拟数据...
    ]);

    const columns = [
        {
            title: '需求编号',
            dataIndex: 'id',
            key: 'id',
            width: 120,
            sorter: (a: IDemand, b: IDemand) => a.id.localeCompare(b.id),
        },
        {
            title: '需求名称',
            dataIndex: 'name',
            key: 'name',
            width: 200,
            render: (text: string, record: IDemand) => (
                <Tooltip title={record.description}>
                    <a onClick={() => viewDetail(record.id)}>{text}</a>
                </Tooltip>
            ),
        },
        {
            title: '需求描述',
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
            render: (status: DemandStatus) => <StatusTag status={status} />,
            filters: [
                { text: '已排期', value: DemandStatus.SCHEDULED },
                { text: '开发中', value: DemandStatus.DEVELOPING },
                { text: '测试中', value: DemandStatus.TESTING },
                { text: '已上线', value: DemandStatus.ONLINE },
                { text: '已关闭', value: DemandStatus.CLOSED },
            ],
            onFilter: (value: any, record: IDemand) => record.status === value,
        },
        {
            title: '优先级',
            dataIndex: 'priority',
            key: 'priority',
            width: 100,
            render: (priority: Priority) => {
                const config = {
                    [Priority.HIGH]: { color: 'red', text: '高' },
                    [Priority.MEDIUM]: { color: 'orange', text: '中' },
                    [Priority.LOW]: { color: 'green', text: '低' },
                };
                return <Tag color={config[priority].color}>{config[priority].text}</Tag>;
            },
        },
        {
            title: '创建时间',
            dataIndex: 'createdAt',
            key: 'createdAt',
            width: 120,
            sorter: (a: IDemand, b: IDemand) =>
                new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime(),
        },
        {
            title: '开发时间',
            key: 'developTime',
            width: 150,
            render: (_: any, record: IDemand) => (
                <div>
                    {record.developStartAt && record.developEndAt ? (
                        <div>
                            {record.developStartAt} ~ {record.developEndAt}
                        </div>
                    ) : (
                        '-'
                    )}
                </div>
            ),
        },
        {
            title: '测试时间',
            key: 'testTime',
            width: 150,
            render: (_: any, record: IDemand) => (
                <div>
                    {record.testStartAt && record.testEndAt ? (
                        <div>
                            {record.testStartAt} ~ {record.testEndAt}
                        </div>
                    ) : (
                        '-'
                    )}
                </div>
            ),
        },
        {
            title: '上线时间',
            dataIndex: 'onlineAt',
            key: 'onlineAt',
            width: 120,
        },
        {
            title: '操作',
            key: 'action',
            width: 150,
            fixed: 'right' as const,
            render: (_: any, record: IDemand) => (
                <Space>
                    <Button
                        type="link"
                        icon={<EditOutlined />}
                        onClick={() => editDemand(record)}
                    >
                        编辑
                    </Button>
                    <Button
                        type="link"
                        danger
                        icon={<DeleteOutlined />}
                        onClick={() => deleteDemand(record.id)}
                    >
                        删除
                    </Button>
                </Space>
            ),
        },
    ];

    const viewDetail = (id: string) => {
        // 跳转到详情页
        window.location.hash = `/demand/detail/${id}`;
    };

    const editDemand = (record: IDemand) => {
        Modal.confirm({
            title: '编辑需求',
            content: '编辑功能开发中...',
            onOk: () => {
                message.success('编辑功能开发中');
            },
        });
    };

    const deleteDemand = (id: string) => {
        Modal.confirm({
            title: '确认删除',
            content: '确定要删除这个需求吗？',
            onOk: () => {
                setDemands(demands.filter(demand => demand.id !== id));
                message.success('删除成功');
            },
        });
    };

    const handleSearch = () => {
        // 实现搜索逻辑
        message.info('搜索功能开发中');
    };

    const handleReset = () => {
        form.resetFields();
        setSearchParams({
            name: '',
            status: undefined,
            priority: undefined,
            dateRange: [],
        });
    };

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        setSelectedRowKeys(newSelectedRowKeys);
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    return (<AppLayout>
            <div style={{ padding: 24 }}>
                <Card>
                    {/* 搜索区域 */}
                    <Form form={form} layout="inline" style={{ marginBottom: 16 }}>
                        <Form.Item name="name" label="需求名称">
                            <Input
                                placeholder="请输入需求名称"
                                style={{ width: 200 }}
                                value={searchParams.name}
                                onChange={e => setSearchParams({ ...searchParams, name: e.target.value })}
                            />
                        </Form.Item>

                        <Form.Item name="status" label="状态">
                            <Select
                                placeholder="请选择状态"
                                style={{ width: 120 }}
                                allowClear
                                value={searchParams.status}
                                onChange={value => setSearchParams({ ...searchParams, status: value })}
                            >
                                <Option value={DemandStatus.SCHEDULED}>已排期</Option>
                                <Option value={DemandStatus.DEVELOPING}>开发中</Option>
                                <Option value={DemandStatus.TESTING}>测试中</Option>
                                <Option value={DemandStatus.ONLINE}>已上线</Option>
                                <Option value={DemandStatus.CLOSED}>已关闭</Option>
                            </Select>
                        </Form.Item>

                        <Form.Item name="priority" label="优先级">
                            <Select
                                placeholder="请选择优先级"
                                style={{ width: 120 }}
                                allowClear
                                value={searchParams.priority}
                                onChange={value => setSearchParams({ ...searchParams, priority: value })}
                            >
                                <Option value={Priority.HIGH}>高</Option>
                                <Option value={Priority.MEDIUM}>中</Option>
                                <Option value={Priority.LOW}>低</Option>
                            </Select>
                        </Form.Item>

                        <Form.Item name="dateRange" label="创建时间">
                            <RangePicker
                                style={{ width: 240 }}
                                onChange={(dates, dateStrings) =>
                                    setSearchParams({ ...searchParams, dateRange: dateStrings })
                                }
                            />
                        </Form.Item>

                        <Form.Item>
                            <Space>
                                <Button
                                    type="primary"
                                    icon={<SearchOutlined />}
                                    onClick={handleSearch}
                                >
                                    搜索
                                </Button>
                                <Button onClick={handleReset}>重置</Button>
                            </Space>
                        </Form.Item>
                    </Form>

                    {/* 操作按钮区域 */}
                    <div style={{ marginBottom: 16 }}>
                        <Space>
                            <Button
                                type="primary"
                                icon={<PlusOutlined />}
                                onClick={() => message.info('新增功能开发中')}
                            >
                                新增需求
                            </Button>

                            <Button
                                icon={<ImportOutlined />}
                                onClick={() => setIsImportModalVisible(true)}
                            >
                                导入
                            </Button>

                            <Button
                                icon={<ExportOutlined />}
                                onClick={() => setIsExportModalVisible(true)}
                                disabled={selectedRowKeys.length === 0}
                            >
                                导出
                            </Button>

                            {selectedRowKeys.length > 0 && (
                                <span>已选择 {selectedRowKeys.length} 项</span>
                            )}
                        </Space>
                    </div>

                    {/* 数据表格 */}
                    <Table
                        rowKey="id"
                        columns={columns}
                        dataSource={demands}
                        rowSelection={rowSelection}
                        scroll={{ x: 1500 }}
                        pagination={{
                            showSizeChanger: true,
                            showQuickJumper: true,
                            showTotal: total => `共 ${total} 条`,
                        }}
                    />
                </Card>

                {/* 导入模态框 */}
                <ImportExport
                    type="import"
                    visible={isImportModalVisible}
                    onClose={() => setIsImportModalVisible(false)}
                    onImport={(data) => {
                        // 处理导入数据
                        console.log('导入数据:', data);
                        setIsImportModalVisible(false);
                        message.success('导入成功');
                    }}
                />

                {/* 导出模态框 */}
                <ImportExport
                    type="export"
                    visible={isExportModalVisible}
                    onClose={() => setIsExportModalVisible(false)}
                    selectedIds={selectedRowKeys as string[]}
                    onExport={(data) => {
                        // 处理导出数据
                        console.log('导出数据:', data);
                        setIsExportModalVisible(false);
                        message.success('导出成功');
                    }}
                />
            </div></AppLayout>
    );
};

export default DemandList;