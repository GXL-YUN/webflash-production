// components/FieldDefinitionTable.tsx
import React, { useState, useEffect } from 'react';
import {
    Table,
    Tag,
    Checkbox,
    Tooltip,
    Button,
    Space
} from 'antd';
import {
    CheckOutlined,
    CloseOutlined,
    InfoCircleOutlined
} from '@ant-design/icons';
import { FieldDefinition } from '../types/data';

interface FieldDefinitionTableProps {
    dataSource: FieldDefinition[];
    onSelect: (selectedFields: string[]) => void;
    initialSelected?: string[];
}

const FieldDefinitionTable: React.FC<FieldDefinitionTableProps> = ({
                                                                       dataSource,
                                                                       onSelect,
                                                                       initialSelected = []
                                                                   }) => {
    const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>(initialSelected);
    const [selectAll, setSelectAll] = useState<boolean>(false);

    useEffect(() => {
        if (initialSelected.length > 0) {
            setSelectedRowKeys(initialSelected);
        }
    }, [initialSelected]);

    const columns = [
        {
            title: (
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <Checkbox
                        checked={selectAll}
                        onChange={(e) => {
                            const checked = e.target.checked;
                            setSelectAll(checked);
                            const allKeys = dataSource
                                .filter(item => item.isSelectable)
                                .map(item => item.fieldName);
                            setSelectedRowKeys(checked ? allKeys : []);
                        }}
                    >
                        选择
                    </Checkbox>
                </div>
            ),
            dataIndex: 'fieldName',
            key: 'selection',
            width: 100,
            render: (fieldName: string, record: FieldDefinition) => (
                <Checkbox
                    disabled={!record.isSelectable}
                    checked={selectedRowKeys.includes(fieldName)}
                    onChange={(e) => {
                        const checked = e.target.checked;
                        const newSelected = checked
                            ? [...selectedRowKeys, fieldName]
                            : selectedRowKeys.filter(key => key !== fieldName);
                        setSelectedRowKeys(newSelected);
                        setSelectAll(newSelected.length === dataSource.filter(f => f.isSelectable).length);
                    }}
                />
            )
        },
        {
            title: '字段名称',
            dataIndex: 'fieldName',
            key: 'fieldName',
            width: 150,
            render: (text: string) => <code>{text}</code>
        },
        {
            title: '显示名称',
            dataIndex: 'displayName',
            key: 'displayName',
            width: 150
        },
        {
            title: '类型',
            dataIndex: 'fieldType',
            key: 'fieldType',
            width: 100,
            render: (type: string) => (
                <Tag color={
                    type === 'string' ? 'blue' :
                        type === 'number' ? 'green' :
                            type === 'boolean' ? 'orange' :
                                type === 'date' ? 'purple' :
                                    type === 'array' ? 'cyan' : 'default'
                }>
                    {type}
                </Tag>
            )
        },
        {
            title: '可选项',
            dataIndex: 'isSelectable',
            key: 'isSelectable',
            width: 100,
            render: (selectable: boolean) => (
                selectable ?
                    <CheckOutlined style={{ color: '#52c41a' }} /> :
                    <CloseOutlined style={{ color: '#ff4d4f' }} />
            )
        },
        {
            title: '描述',
            dataIndex: 'description',
            key: 'description',
            ellipsis: true,
            render: (text: string, record: FieldDefinition) => (
                <div style={{ display: 'flex', alignItems: 'flex-start' }}>
                    {text || '暂无描述'}
                    {record.relatedEndpoint && (
                        <Tooltip title={`关联接口: ${record.relatedEndpoint}`}>
                            <InfoCircleOutlined style={{ marginLeft: 8, color: '#1890ff' }} />
                        </Tooltip>
                    )}
                </div>
            )
        },
        {
            title: '默认值',
            dataIndex: 'defaultValue',
            key: 'defaultValue',
            width: 120,
            render: (value: any) => (
                <span>{value !== undefined ? JSON.stringify(value) : '-'}</span>
            )
        }
    ];

    const handleConfirmSelection = () => {
        onSelect(selectedRowKeys);
    };

    return (
        <div>
            <div style={{ marginBottom: 16 }}>
                <Space>
                    <Button
                        type="primary"
                        onClick={handleConfirmSelection}
                        disabled={selectedRowKeys.length === 0}
                    >
                        确认选择 ({selectedRowKeys.length})
                    </Button>
                    <span style={{ color: '#666', fontSize: 12 }}>
            已选择 {selectedRowKeys.length} 个字段
          </span>
                </Space>
            </div>

            <Table
                dataSource={dataSource}
                columns={columns}
                rowKey="fieldName"
                pagination={{
                    pageSize: 10,
                    showSizeChanger: true,
                    showQuickJumper: true,
                    showTotal: (total) => `共 ${total} 个字段`
                }}
                scroll={{ y: 400 }}
                size="middle"
            />
        </div>
    );
};

export default FieldDefinitionTable;