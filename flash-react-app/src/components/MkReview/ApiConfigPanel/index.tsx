import React, { useState } from 'react';
import {
    Card,
    Form,
    Input,
    Select,
    Button,
    Space,
    message,
    Row,
    Col,
    Divider
} from 'antd';
import { SendOutlined, SettingOutlined } from '@ant-design/icons';
import { ApiConfig, ApiConfigPanelProps } from './type';

const { Option } = Select;
const { TextArea } = Input;

const ApiConfigPanel: React.FC<ApiConfigPanelProps> = ({
                                                           onApiResponse,
                                                           loading,
                                                           setLoading
                                                       }) => {
    const [form] = Form.useForm();
    const [advancedVisible, setAdvancedVisible] = useState(false);
    const [apiHistory, setApiHistory] = useState<ApiConfig[]>([]);

    // 预设接口示例
    const presetApis = [
        { label: '用户接口', value: 'https://api.example.com/users' },
        { label: '订单接口', value: 'https://api.example.com/orders' },
        { label: '商品接口', value: 'https://api.example.com/products' },
    ];

    // 发送请求
    const sendRequest = async (values: any) => {
        setLoading(true);
        try {
            const { url, method, headers, params, body } = values;

            // 保存到历史记录
            const apiConfig: ApiConfig = { url, method, headers, params, body };
            setApiHistory(prev => [apiConfig, ...prev.slice(0, 4)]);

            // 构建请求配置
            const config: RequestInit = {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    ...(headers ? JSON.parse(headers) : {}),
                },
            };

            if (method !== 'GET' && body) {
                config.body = JSON.stringify(body);
            }

            // 构建查询参数
            let requestUrl = url;
            if (params && method === 'GET') {
                const queryParams = new URLSearchParams(params).toString();
                requestUrl = `${url}?${queryParams}`;
            }

            // 发送请求
            const response = await fetch(requestUrl, config);

            if (!response.ok) {
                throw new Error(`请求失败: ${response.status}`);
            }

            const data = await response.json();

            // 模拟生成字段配置（实际项目中可能从接口文档或响应中提取）
            const fields = generateFieldConfigFromResponse(data);

            onApiResponse(data, fields);
            message.success('接口请求成功！');

        } catch (error) {
            message.error(`请求失败: ${error instanceof Error ? error.message : '未知错误'}`);

            // 模拟数据用于演示
            const mockData = generateMockData();
            const mockFields = generateFieldConfigFromResponse(mockData);
            onApiResponse(mockData, mockFields);

        } finally {
            setLoading(false);
        }
    };

    // 生成字段配置
    const generateFieldConfigFromResponse = (data: any) => {
        if (!data || typeof data !== 'object') return [];

        const sampleItem = Array.isArray(data.data) && data.data[0] ? data.data[0] : data;

        return Object.keys(sampleItem).map(key => ({
            fieldName: key,
            fieldType: typeof sampleItem[key] as any,
            description: `字段: ${key}`,
            required: false,
            defaultValue: sampleItem[key]
        }));
    };

    // 生成模拟数据
    const generateMockData = () => {
        return {
            data: [
                { id: 1, name: '张三', age: 25, email: 'zhangsan@example.com', status: 'active' },
                { id: 2, name: '李四', age: 30, email: 'lisi@example.com', status: 'inactive' },
                { id: 3, name: '王五', age: 28, email: 'wangwu@example.com', status: 'active' },
            ],
            total: 3,
            page: 1,
            pageSize: 10
        };
    };

    // 选择历史记录
    const selectHistory = (config: ApiConfig) => {
        form.setFieldsValue(config);
    };

    return (
        <Card
            title="接口配置"
            extra={
                <Space>
                    <Button
                        icon={<SettingOutlined />}
                        onClick={() => setAdvancedVisible(!advancedVisible)}
                    >
                        高级配置
                    </Button>
                </Space>
            }
        >
            <Form
                form={form}
                layout="vertical"
                onFinish={sendRequest}
                initialValues={{
                    method: 'GET',
                    url: 'https://api.example.com/data',
                }}
            >
                <Row gutter={16}>
                    <Col span={6}>
                        <Form.Item
                            name="method"
                            label="请求方法"
                            rules={[{ required: true }]}
                        >
                            <Select>
                                <Option value="GET">GET</Option>
                                <Option value="POST">POST</Option>
                                <Option value="PUT">PUT</Option>
                                <Option value="DELETE">DELETE</Option>
                            </Select>
                        </Form.Item>
                    </Col>

                    <Col span={18}>
                        <Form.Item
                            name="url"
                            label="接口地址"
                            rules={[
                                { required: true, message: '请输入接口地址' },
                                { type: 'url', message: '请输入有效的URL' }
                            ]}
                        >
                            <Select
                                showSearch
                                placeholder="输入或选择接口地址"
                                options={presetApis}
                                dropdownRender={menu => (
                                    <>
                                        {menu}
                                        <Divider style={{ margin: '8px 0' }} />
                                        <div style={{ padding: '0 8px 8px' }}>
                                            <div style={{ marginBottom: 8, color: '#999' }}>历史记录:</div>
                                            {apiHistory.map((config, index) => (
                                                <div
                                                    key={index}
                                                    style={{
                                                        padding: '4px 8px',
                                                        cursor: 'pointer',
                                                        borderRadius: 4,
                                                        '&:hover': { backgroundColor: '#f5f5f5' }
                                                    }as any}
                                                    onClick={() => selectHistory(config)}
                                                >
                                                    {config.method} {config.url}
                                                </div>
                                            ))}
                                        </div>
                                    </>
                                )}
                            />
                        </Form.Item>
                    </Col>
                </Row>

                {advancedVisible && (
                    <>
                        <Row gutter={16}>
                            <Col span={12}>
                                <Form.Item
                                    name="headers"
                                    label="请求头 (JSON)"
                                    tooltip="输入JSON格式的请求头，如: {'Authorization': 'Bearer token'}"
                                >
                                    <TextArea
                                        rows={3}
                                        placeholder={'{\n  "Authorization": "Bearer your-token"\n}'}
                                    />
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item
                                    name="params"
                                    label="查询参数 (JSON)"
                                    tooltip="GET请求的查询参数"
                                >
                                    <TextArea
                                        rows={3}
                                        placeholder={'{\n  "page": 1,\n  "pageSize": 10\n}'}
                                    />
                                </Form.Item>
                            </Col>
                        </Row>

                        <Form.Item
                            name="body"
                            label="请求体 (JSON)"
                            tooltip="POST/PUT请求的请求体"
                        >
                            <TextArea
                                rows={4}
                                placeholder={'{\n  "name": "value",\n  "data": {}\n}'}
                            />
                        </Form.Item>
                    </>
                )}

                <Form.Item>
                    <Space>
                        <Button
                            type="primary"
                            htmlType="submit"
                            icon={<SendOutlined />}
                            loading={loading}
                        >
                            发送请求
                        </Button>
                        <Button
                            onClick={() => {
                                form.resetFields();
                                setAdvancedVisible(false);
                            }}
                        >
                            重置
                        </Button>
                    </Space>
                </Form.Item>
            </Form>
        </Card>
    );
};

export default ApiConfigPanel;