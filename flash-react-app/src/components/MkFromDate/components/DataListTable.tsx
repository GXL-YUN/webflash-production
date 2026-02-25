// components/DataListTable.tsx
import React, { useState } from 'react';
import {
    Table,
    Button,
    Space,
    Tooltip,
    Tag,
    notification
} from 'antd';
import {
    CheckSquareOutlined,
    BorderOutlined,
    ExportOutlined
} from '@ant-design/icons';
import { ListData } from '../types/data';

interface DataListTableProps {
    dataSource: ListData[];
    fieldsToShow: string[];
    onSelect: (selectedRows: ListData[]) => void;
}

const DataListTable: React.FC<DataListTableProps> = ({
                                                         dataSource,
                                                         fieldsToShow,
                                                         onSelect
                                                     }) => {
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [selectedRows, setSelectedRows] = useState<ListData[]>([]);

    // 动态生成列
    const generateColumns = () => {
        const baseColumns = [
            {
                title: '序号',
                key: 'index',
                width: 60,
                render: (_: any, __: any, index: number) => index + 1
            },
            {
                title: 'ID',
                dataIndex: 'id',
                key: 'id',
                width: 80
            }
        ];

        const dynamicColumns = fieldsToShow.map(field => ({
            title: field,
            dataIndex: field,
            key: field,
            ellipsis: true,
            render: (value: any) => {
                if (value === null || value === undefined) {
                    return <span style={{ color: '#999' }}>空</span>;
                }
                if (Array.isArray(value)) {
                    return (
                        <Tooltip title={JSON.stringify(value)}>
                            <Tag color="blue">数组[{value.length}]</Tag>
                        </Tooltip>
                    );
                }
                if (typeof value === 'object') {
                    return (
                        <Tooltip title={JSON.stringify(value)}>
                            <Tag color="cyan">对象</Tag>
                        </Tooltip>
                    );
                }
                if (typeof value === 'boolean') {
                    return (
                        <Tag color={value ? 'green' : 'red'}>
                            {value.toString()}
                        </Tag>
                    );
                }
                return String(value);
            }
        }));

        return [
            ...baseColumns,
            ...dynamicColumns,
            {
                title: '创建时间',
                dataIndex: 'createdTime',
                key: 'createdTime',
                width: 180
            }
        ];
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: (selectedKeys: React.Key[], selectedRows: ListData[]) => {
            setSelectedRowKeys(selectedKeys);
            setSelectedRows(selectedRows);
        },
        getCheckboxProps: (record: ListData) => ({
            disabled: record.disabled || false
        })
    };

    const handleSubmitSelection = () => {
        if (selectedRows.length === 0) {
            notification.warning({
                message: '请选择数据',
                description: '请至少选择一条数据进行提交'
            });
            return;
        }

        onSelect(selectedRows);
    };

    const handleExport = () => {
        if (selectedRows.length === 0) {
            notification.warning({
                message: '请选择数据',
                description: '请先选择要导出的数据'
            });
            return;
        }

        const dataStr = JSON.stringify(selectedRows, null, 2);
        const dataBlob = new Blob([dataStr], { type: 'application/json' });
        const url = URL.createObjectURL(dataBlob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `data_${Date.now()}.json`;
        link.click();
        URL.revokeObjectURL(url);
    };

    return (
        <div>
            <div style={{ marginBottom: 16 }}>
                <Space>
                    <Button
                        type="primary"
                        icon={<CheckSquareOutlined />}
                        onClick={handleSubmitSelection}
                        disabled={selectedRows.length === 0}
                    >
                        提交选择 ({selectedRows.length})
                    </Button>

                    <Button
                        icon={<ExportOutlined />}
                        onClick={handleExport}
                        disabled={selectedRows.length === 0}
                    >
                        导出数据
                    </Button>

                    <Button
                        icon={<BorderOutlined />}
                        onClick={() => {
                            setSelectedRowKeys([]);
                            setSelectedRows([]);
                        }}
                    >
                        清空选择
                    </Button>

                    <span style={{ color: '#666' }}>
            已选择 {selectedRows.length} 条数据
          </span>
                </Space>
            </div>

            <Table
                rowSelection={{
                    type: 'checkbox',
                    ...rowSelection
                }}
                columns={generateColumns()}
                dataSource={dataSource}
                rowKey="id"
                pagination={{
                    pageSize: 10,
                    current: 1,
                    total: 300,
                    showSizeChanger: true,  // 这里应该是布尔值
                    showQuickJumper: true,
                    showTotal: (total) => `共 ${total} 条记录`,
                    onChange: (page, pageSize) => {
                        setSelectedRowKeys([]);
                        setSelectedRows([]);
                        // 处理页码或分页大小改变的逻辑
                    },
                    onShowSizeChange: (current, size) => {  // 这是正确的回调函数
                        setSelectedRowKeys([]);
                        setSelectedRows([]);
                        // 专门处理分页大小改变的逻辑
                    }
                }}
                scroll={{ x: 'max-content', y: 500 }}
                size="middle"
                bordered
            />
        </div>
    );
};

export default DataListTable;