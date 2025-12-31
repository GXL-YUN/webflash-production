import React, { useState, useEffect } from 'react';
import {
    Card,
    Table,
    Space,
    Button,
    Input,
    Select,
    Tag,
    Tooltip,
    message,
    Empty,
    Row,
    Col,
    Statistic,
    Divider
} from 'antd';
import {
    DownloadOutlined,
    CopyOutlined,
    ReloadOutlined,
    FilterOutlined,
    SortAscendingOutlined,
    EyeOutlined,
    EyeInvisibleOutlined
} from '@ant-design/icons';
import { SelectedDataItem } from '../types';
import { DataDisplayPanelProps } from './type';

const { Search } = Input;
const { Option } = Select;

const DataDisplayPanel: React.FC<DataDisplayPanelProps> = ({
                                                               data,
                                                               fields,
                                                               selectedItems = [],
                                                               loading,
                                                               onDataTransform
                                                           }) => {
    const [displayData, setDisplayData] = useState<any[]>([]);
    const [searchText, setSearchText] = useState('');
    const [visibleColumns, setVisibleColumns] = useState<string[]>([]);
    const [sortConfig, setSortConfig] = useState<{field: string; order: 'ascend' | 'descend'} | null>(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);

    // 初始化显示数据和列
    useEffect(() => {
        if (data && data.length > 0 && selectedItems.length > 0) {
            const transformedData = transformData(data, selectedItems);
            setDisplayData(transformedData);
            setVisibleColumns(selectedItems.map(item => item.fieldName));
        } else if (data && data.length > 0) {
            setDisplayData(data);
            if (fields.length > 0) {
                setVisibleColumns(fields.map(field => field.fieldName));
            }
        }
    }, [data, fields, selectedItems]);

    // 转换数据
    const transformData = (originalData: any[], selections: SelectedDataItem[]) => {
        return originalData.map(item => {
            const transformedItem: any = {};
            selections.forEach(selection => {
                if (selection.selected && item[selection.fieldName] !== undefined) {
                    const key = selection.alias || selection.fieldName;
                    let value = item[selection.fieldName];

                    // 应用格式化
                    if (selection.format) {
                        value = formatValue(value, selection.format);
                    }

                    transformedItem[key] = value;
                    // 保留原始字段名用于排序和筛选
                    transformedItem[`_original_${selection.fieldName}`] = item[selection.fieldName];
                }
            });
            return transformedItem;
        });
    };

    // 格式化值
    const formatValue = (value: any, format: string) => {
        if (value === null || value === undefined) return '-';

        switch(format) {
            case 'YYYY-MM-DD':
                if (value instanceof Date) {
                    return value.toISOString().split('T')[0];
                }
                return value;
            case 'percent':
                return typeof value === 'number' ? `${(value * 100).toFixed(2)}%` : value;
            case 'currency':
                return typeof value === 'number' ? `¥${value.toFixed(2)}` : value;
            case 'thousands':
                return typeof value === 'number' ? value.toLocaleString() : value;
            default:
                return value;
        }
    };

    // 切换列显示
    const toggleColumn = (columnKey: string) => {
        setVisibleColumns(prev =>
            prev.includes(columnKey)
                ? prev.filter(key => key !== columnKey)
                : [...prev, columnKey]
        );
    };

    // 排序数据
    const sortData = (field: string, order: 'ascend' | 'descend') => {
        setSortConfig({ field, order });

        const sortedData = [...displayData].sort((a, b) => {
            const aValue = a[`_original_${field}`] || a[field];
            const bValue = b[`_original_${field}`] || b[field];

            if (order === 'ascend') {
                return aValue > bValue ? 1 : -1;
            } else {
                return aValue < bValue ? 1 : -1;
            }
        });

        setDisplayData(sortedData);
    };

    // 过滤数据
    const filteredData = searchText
        ? displayData.filter(item =>
            Object.values(item).some(value =>
                String(value).toLowerCase().includes(searchText.toLowerCase())
            )
        )
        : displayData;

    // 导出数据
    const exportData = (format: 'json' | 'csv') => {
        try {
            let content = '';
            const exportData = filteredData.map(item => {
                const { _original_$key, ...cleanItem } = item;
                return cleanItem;
            });

            if (format === 'json') {
                content = JSON.stringify(exportData, null, 2);
                downloadFile(content, 'data.json', 'application/json');
            } else if (format === 'csv') {
                if (exportData.length === 0) {
                    content = '';
                } else {
                    const headers = Object.keys(exportData[0]).join(',');
                    const rows = exportData.map(item =>
                        Object.values(item).map(val =>
                            `"${String(val).replace(/"/g, '""')}"`
                        ).join(',')
                    );
                    content = [headers, ...rows].join('\n');
                }
                downloadFile(content, 'data.csv', 'text/csv');
            }

            message.success('导出成功！');
        } catch (error) {
            message.error('导出失败');
        }
    };

    const downloadFile = (content: string, filename: string, type: string) => {
        const blob = new Blob([content], { type });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        link.click();
        URL.revokeObjectURL(url);
    };

    // 复制到剪贴板
    const copyToClipboard = () => {
        const text = JSON.stringify(filteredData, null, 2);
        navigator.clipboard.writeText(text)
            .then(() => message.success('已复制到剪贴板'))
            .catch(() => message.error('复制失败'));
    };

    // 表格列配置
    const columns = (selectedItems.length > 0 ? selectedItems : fields.map(f => ({
        fieldName: f.fieldName,
        alias: f.fieldName
    })) as SelectedDataItem[]).filter(item => visibleColumns.includes(item.fieldName)).map(item => ({
        title: (
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span>{item.alias || item.fieldName}</span>
                <Space size={0}>
                    <Tooltip title="排序">
                        <Button
                            type="text"
                            size="small"
                            icon={<SortAscendingOutlined />}
                            onClick={() => {

                                if (!sortConfig) {
                                    // 如果 sortConfig 为 null，使用默认值
                                    sortData(item.fieldName, 'ascend');
                                    return;
                                }

                                const currentOrder = sortConfig.field === item.fieldName
                                    ? (sortConfig.order === 'ascend' ? 'descend' : 'ascend')
                                    : 'ascend';
                                sortData(item.fieldName, currentOrder);
                            }}
                        />
                    </Tooltip>
                    <Tooltip title="隐藏列">
                        <Button
                            type="text"
                            size="small"
                            icon={<EyeInvisibleOutlined />}
                            onClick={() => toggleColumn(item.fieldName)}
                        />
                    </Tooltip>
                </Space>
            </div>
        ),
        dataIndex: item.alias || item.fieldName,
        key: item.fieldName,
        sorter: true,
        width: 150,
        render: (value: any) => {
            if (typeof value === 'boolean') {
                return (
                    <Tag color={value ? 'success' : 'error'}>
                        {value ? '是' : '否'}
                    </Tag>
                );
            }

            if (value === null || value === undefined) {
                return <span style={{ color: '#999' }}>-</span>;
            }

            return <span>{value}</span>;
        }
    }));

    // 统计数据
    const stats = {
        total: data?.length || 0,
        filtered: filteredData.length,
        columns: visibleColumns.length
    };

    return (
        <Card
            title="数据展示"
            extra={
                <Space>
                    <Statistic
                        title="数据量"
                        value={stats.filtered}
                        suffix={`/ ${stats.total}`}
                        valueStyle={{ fontSize: 14 }}
                    />
                    <Divider type="vertical" />
                    <Button
                        icon={<ReloadOutlined />}
                        onClick={() => onDataTransform?.(displayData)}
                        disabled={!data}
                    >
                        重新生成
                    </Button>
                </Space>
            }
        >
            {/* 工具栏 */}
            <Row gutter={16} style={{ marginBottom: 16 }}>
                <Col span={8}>
                    <Search
                        placeholder="搜索数据..."
                        value={searchText}
                        onChange={e => setSearchText(e.target.value)}
                        allowClear
                        enterButton={<FilterOutlined />}
                    />
                </Col>

                <Col span={16}>
                    <Space>
                        {/* 列显示控制 */}
                        <Select
                            mode="multiple"
                            placeholder="选择显示的列"
                            style={{ minWidth: 200 }}
                            value={visibleColumns}
                            onChange={setVisibleColumns}
                            maxTagCount="responsive"
                        >
                            {(selectedItems.length > 0 ? selectedItems : fields).map(item => (
                                <Option key={item.fieldName} value={item.fieldName}>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                                        {visibleColumns.includes(item.fieldName) ? (
                                            <EyeOutlined style={{ color: '#1890ff' }} />
                                        ) : (
                                            <EyeInvisibleOutlined />
                                        )}
                                        {item.alias || item.fieldName}
                                    </div>
                                </Option>
                            ))}
                        </Select>

                        {/* 导出选项 */}
                        <Select
                            placeholder="导出格式"
                            style={{ width: 120 }}
                            onSelect={exportData}
                        >
                            <Option value="json">JSON 格式</Option>
                            <Option value="csv">CSV 格式</Option>
                        </Select>

                        <Button
                            icon={<DownloadOutlined />}
                            onClick={() => exportData('json')}
                        >
                            导出
                        </Button>

                        <Button
                            icon={<CopyOutlined />}
                            onClick={copyToClipboard}
                        >
                            复制
                        </Button>
                    </Space>
                </Col>
            </Row>

            {/* 数据表格 */}
            {filteredData.length > 0 ? (
                <Table
                    columns={columns}
                    dataSource={filteredData.map((item, index) => ({
                        ...item,
                        key: index
                    }))}
                    loading={loading}
                    scroll={{ x: 'max-content', y: 400 }}
                    pagination={{
                        current: currentPage,
                        pageSize: pageSize,
                        total: filteredData.length,
                        showSizeChanger: true,
                        showQuickJumper: true,
                        showTotal: (total, range) =>
                            `第 ${range[0]}-${range[1]} 条，共 ${total} 条`,
                        onChange: (page, size) => {
                            setCurrentPage(page);
                            setPageSize(size || 10);
                        }
                    }}
                    size="middle"
                    bordered
                />
            ) : (
                <Empty
                    description={
                        searchText ? '没有找到匹配的数据' : '暂无数据'
                    }
                    image={Empty.PRESENTED_IMAGE_SIMPLE}
                >
                    {searchText && (
                        <Button onClick={() => setSearchText('')}>
                            清除搜索条件
                        </Button>
                    )}
                </Empty>
            )}

            {/* 数据预览 */}
            {displayData.length > 0 && (
                <div style={{ marginTop: 16, padding: 16, background: '#fafafa', borderRadius: 8 }}>
                    <div style={{ marginBottom: 8, fontWeight: 'bold' }}>
                        数据预览 (前5条):
                    </div>
                    <pre style={{
                        margin: 0,
                        maxHeight: 200,
                        overflow: 'auto',
                        fontSize: 12,
                        background: '#fff',
                        padding: 12,
                        borderRadius: 4
                    }}>
            {JSON.stringify(displayData.slice(0, 5), null, 2)}
          </pre>
                </div>
            )}
        </Card>
    );
};

export default DataDisplayPanel;