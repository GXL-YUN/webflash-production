// components/SearchPanel.tsx
import React, { useState } from 'react';
import {
    Form,
    Input,
    Button,
    DatePicker,
    Select,
    InputNumber,
    Row,
    Col,
    Space
} from 'antd';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons';
import { SearchParams } from '../types/data';

const { RangePicker } = DatePicker;
const { Option } = Select;

interface SearchPanelProps {
    onSearch: (params: SearchParams) => void;
}

const SearchPanel: React.FC<SearchPanelProps> = ({ onSearch }) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const handleSearch = async () => {
        try {
            setLoading(true);
            const values = await form.validateFields();

            const searchParams: SearchParams = {
                keyword: values.keyword,
                dateRange: values.dateRange,
                type: values.type,
                pageSize: values.pageSize || 10,
                pageNum: 1
            };

            onSearch(searchParams);
        } catch (error) {
            console.error('表单验证失败:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleReset = () => {
        form.resetFields();
        onSearch({});
    };

    return (
        <Form
            form={form}
            layout="vertical"
            initialValues={{
                pageSize: 10
            }}
        >
            <Row gutter={[16, 16]}>
                <Col xs={24} sm={12} md={8} lg={6}>
                    <Form.Item
                        label="关键词"
                        name="keyword"
                    >
                        <Input placeholder="请输入搜索关键词" allowClear />
                    </Form.Item>
                </Col>

                <Col xs={24} sm={12} md={8} lg={6}>
                    <Form.Item
                        label="日期范围"
                        name="dateRange"
                    >
                        <RangePicker
                            style={{ width: '100%' }}
                            placeholder={['开始日期', '结束日期']}
                        />
                    </Form.Item>
                </Col>

                <Col xs={24} sm={12} md={8} lg={6}>
                    <Form.Item
                        label="类型"
                        name="type"
                    >
                        <Select placeholder="请选择类型" allowClear>
                            <Option value="type1">类型1</Option>
                            <Option value="type2">类型2</Option>
                            <Option value="type3">类型3</Option>
                        </Select>
                    </Form.Item>
                </Col>

                <Col xs={24} sm={12} md={8} lg={6}>
                    <Form.Item
                        label="每页数量"
                        name="pageSize"
                    >
                        <InputNumber
                            min={1}
                            max={100}
                            style={{ width: '100%' }}
                            placeholder="每页显示数量"
                        />
                    </Form.Item>
                </Col>
            </Row>

            <Row justify="end">
                <Col>
                    <Space>
                        <Button
                            type="primary"
                            icon={<SearchOutlined />}
                            onClick={handleSearch}
                            loading={loading}
                        >
                            搜索
                        </Button>
                        <Button
                            icon={<ReloadOutlined />}
                            onClick={handleReset}
                        >
                            重置
                        </Button>
                    </Space>
                </Col>
            </Row>
        </Form>
    );
};

export default SearchPanel;