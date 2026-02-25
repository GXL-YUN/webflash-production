import React, { useState, useEffect, useMemo } from 'react';
import {
    Card,
    Button,
    Space,
    Input,
    Select,
    Tag,
    Tooltip,
    Checkbox,
    message,
    Row,
    Col
} from 'antd';
import {
    CheckCircleOutlined,
    CloseCircleOutlined,
    QuestionCircleOutlined,
    DatabaseOutlined,
    CaretDownOutlined,
    CaretRightOutlined,
    FolderOutlined,
    FileOutlined,
    UnorderedListOutlined
} from '@ant-design/icons';
import { FieldConfig, SelectedDataItem, HierarchicalFieldConfig } from '../types';
import { FieldSelectorPanelProps } from './type';

const { Option } = Select;

const FieldSelectorPanel: React.FC<FieldSelectorPanelProps> = ({
                                                                   fields,
                                                                   onSelectionChange,
                                                                   onDataGenerate,
                                                                   loading
                                                               }) => {
    const [selectedItems, setSelectedItems] = useState<SelectedDataItem[]>([]);
    const [selectAll, setSelectAll] = useState(false);
    const [searchText, setSearchText] = useState('');
    const [expandedKeys, setExpandedKeys] = useState<string[]>([]);
    const [autoExpandParent, setAutoExpandParent] = useState(true);

    // 递归转换平面字段为层次结构
    const hierarchicalFields = useMemo(() => {
        const convertFieldToHierarchical = (
            field: FieldConfig,
            parentPath = '',
            level = 0
        ): HierarchicalFieldConfig => {
            const fieldPath = parentPath ? `${parentPath}.${field.fieldName}` : field.fieldName;
            const isComplexType = field.fieldType === 'object' || field.fieldType === 'array';
            const hasChildren = field.children && field.children.length > 0;

            const node: HierarchicalFieldConfig = {
                fieldName: field.fieldName,
                fieldType: field.fieldType,
                description: field.description,
                required: field.required || false,
                defaultValue: field.defaultValue,
                parentKey: field.parentKey,
                isLeaf: !isComplexType || !hasChildren,
                expanded: expandedKeys.includes(fieldPath),
                level: level,
                key: fieldPath,
                path: fieldPath
            };

            // 如果有子字段，递归转换
            if (hasChildren && field.children) {
                node.children = field.children.map(child =>
                    convertFieldToHierarchical(child, fieldPath, level + 1)
                );
            }

            return node;
        };

        return fields.map(field => convertFieldToHierarchical(field, '', 0));
    }, [fields, expandedKeys]);

    // 递归获取所有字段路径
    const getAllFieldPaths = (fieldList: FieldConfig[], parentPath = ''): SelectedDataItem[] => {
        const items: SelectedDataItem[] = [];

        fieldList.forEach(field => {
            const fieldPath = parentPath ? `${parentPath}.${field.fieldName}` : field.fieldName;

            items.push({
                fieldName: fieldPath,
                selected: false,
                alias: field.fieldName,
                format: getDefaultFormat(field.fieldType),
                fieldType: field.fieldType
            });

            // 如果有子字段，递归处理
            if (field.children && field.children.length > 0) {
                items.push(...getAllFieldPaths(field.children, fieldPath));
            }
        });

        return items;
    };

    // 初始化选择项
    useEffect(() => {
        if (fields && fields.length > 0) {
            const allFieldItems = getAllFieldPaths(fields);
            setSelectedItems(allFieldItems);

            // 默认展开第一层
            const firstLevelKeys = fields.map(field => field.fieldName);
            setExpandedKeys(firstLevelKeys);
        }
    }, [fields]);

    // 获取所有子字段的key
    const getAllChildKeys = (field: HierarchicalFieldConfig): string[] => {
        const keys: string[] = [field.key];
        if (field.children) {
            field.children.forEach(child => {
                keys.push(...getAllChildKeys(child));
            });
        }
        return keys;
    };

    // 获取所有父字段的key
    const getAllParentKeys = (fieldKey: string): string[] => {
        const parts = fieldKey.split('.');
        const parentKeys: string[] = [];
        for (let i = 1; i < parts.length; i++) {
            parentKeys.push(parts.slice(0, i).join('.'));
        }
        return parentKeys;
    };

    // 获取字段类型标签
    const getTypeTag = (type: string) => {
        const typeColors: Record<string, string> = {
            string: 'blue',
            number: 'green',
            boolean: 'orange',
            date: 'purple',
            object: 'cyan',
            array: 'magenta'
        };

        return (
            <Tag color={typeColors[type] || 'default'}>
                {type.toUpperCase()}
            </Tag>
        );
    };

    // 获取默认格式化
    const getDefaultFormat = (type: string) => {
        switch(type) {
            case 'date': return 'YYYY-MM-DD';
            case 'number': return 'number';
            default: return '';
        }
    };

    // 切换字段及其所有子字段的选择状态
    const toggleSelectionWithChildren = (field: HierarchicalFieldConfig, selected: boolean) => {
        const allChildKeys = getAllChildKeys(field);
        setSelectedItems(prev => prev.map(item =>
            allChildKeys.includes(item.fieldName)
                ? { ...item, selected }
                : item
        ));
    };

    // 获取字段及其子字段的选择状态
    const getFieldSelectionState = (field: HierarchicalFieldConfig) => {
        const allChildKeys = getAllChildKeys(field);
        const selectedItemsInField = selectedItems.filter(item =>
            allChildKeys.includes(item.fieldName) && item.selected
        );

        if (selectedItemsInField.length === 0) {
            return 'none';
        } else if (selectedItemsInField.length === allChildKeys.length) {
            return 'all';
        } else {
            return 'partial';
        }
    };

    // 全选/全不选
    const toggleSelectAll = () => {
        const newSelectAll = !selectAll;
        setSelectAll(newSelectAll);
        setSelectedItems(prev =>
            prev.map(item => ({ ...item, selected: newSelectAll }))
        );
    };

    // 更新别名
    const updateAlias = (fieldKey: string, alias: string) => {
        setSelectedItems(prev =>
            prev.map(item =>
                item.fieldName === fieldKey
                    ? { ...item, alias }
                    : item
            )
        );
    };

    // 更新格式化
    const updateFormat = (fieldKey: string, format: string) => {
        setSelectedItems(prev =>
            prev.map(item =>
                item.fieldName === fieldKey
                    ? { ...item, format }
                    : item
            )
        );
    };

    // 展开/折叠节点
    const toggleExpand = (fieldKey: string) => {
        if (expandedKeys.includes(fieldKey)) {
            setExpandedKeys(expandedKeys.filter(key => key !== fieldKey));
        } else {
            setExpandedKeys([...expandedKeys, fieldKey]);
        }
    };

    // 递归搜索字段
    const searchFields = (fieldList: HierarchicalFieldConfig[], searchTerm: string): HierarchicalFieldConfig[] => {
        if (!searchTerm) return fieldList;

        return fieldList
            .filter(field => {
                const matches =
                    field.fieldName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    field.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    (field.children && searchFields(field.children, searchTerm).length > 0);

                return matches;
            })
            .map(field => ({
                ...field,
                children: field.children ? searchFields(field.children, searchTerm) : undefined
            }));
    };

    // 过滤字段
    const filteredFields = useMemo(() => {
        return searchFields(hierarchicalFields, searchText);
    }, [hierarchicalFields, searchText]);

    // 生成数据
    const handleGenerateData = () => {
        const selectedFields = selectedItems
            .filter(item => item.selected)
            .map(item => item.fieldName);

        if (selectedFields.length === 0) {
            message.warning('请至少选择一个字段');
            return;
        }

        onSelectionChange(selectedItems.filter(item => item.selected));
        onDataGenerate(selectedFields);
    };

    // 渲染字段行
    const renderFieldRow = (field: HierarchicalFieldConfig, level = 0) => {
        const selectionState = getFieldSelectionState(field);
        const item = selectedItems.find(i => i.fieldName === field.key);
        const hasChildren = field.children && field.children.length > 0;
        const isExpanded = expandedKeys.includes(field.key);

        return (
            <React.Fragment key={field.key}>
                <tr style={{ background: level % 2 === 0 ? '#fff' : '#fafafa' }}>
                    <td style={{ width: 80, padding: '8px' }}>
                        <Checkbox
                            checked={selectionState === 'all'}
                            indeterminate={selectionState === 'partial'}
                            onChange={(e) => toggleSelectionWithChildren(field, e.target.checked)}
                        />
                    </td>
                    <td style={{ padding: '8px' }}>
                        <div style={{
                            display: 'flex',
                            alignItems: 'center',
                            paddingLeft: level * 24
                        }}>
                            {hasChildren ? (
                                <span
                                    style={{ marginRight: 8, cursor: 'pointer' }}
                                    onClick={() => toggleExpand(field.key)}
                                >
                  {isExpanded ?
                      <CaretDownOutlined /> :
                      <CaretRightOutlined />
                  }
                </span>
                            ) : (
                                <span style={{ width: 20, display: 'inline-block' }} />
                            )}
                            {field.fieldType === 'object' ? (
                                <FolderOutlined style={{ color: '#ffa940', marginRight: 8 }} />
                            ) : field.fieldType === 'array' ? (
                                <UnorderedListOutlined style={{ color: '#9254de', marginRight: 8 }} />
                            ) : (
                                <FileOutlined style={{ color: '#1890ff', marginRight: 8 }} />
                            )}
                            <code style={{ fontWeight: 'bold' }}>
                                {field.fieldName.split('.').pop()}
                                {field.fieldType === 'array' && '[]'}
                            </code>
                        </div>
                    </td>
                    <td style={{ width: 100, padding: '8px' }}>
                        {getTypeTag(field.fieldType)}
                    </td>
                    <td style={{ padding: '8px', maxWidth: 200 }}>
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                            {field.description}
                            {field.required && (
                                <Tooltip title="必填字段">
                                    <QuestionCircleOutlined style={{ marginLeft: 4, color: '#ff4d4f' }} />
                                </Tooltip>
                            )}
                        </div>
                    </td>
                    <td style={{ width: 150, padding: '8px' }}>
                        <Input
                            size="small"
                            value={item?.alias || field.fieldName.split('.').pop()}
                            onChange={e => updateAlias(field.key, e.target.value)}
                            placeholder="显示名称"
                        />
                    </td>
                    <td style={{ width: 150, padding: '8px' }}>
                        {renderFormatSelector(field)}
                    </td>
                </tr>
                {hasChildren && isExpanded && field.children && (
                    <>
                        {field.children.map(child => renderFieldRow(child, level + 1))}
                    </>
                )}
            </React.Fragment>
        );
    };

    // 渲染格式化选择器
    const renderFormatSelector = (field: HierarchicalFieldConfig) => {
        const item = selectedItems.find(i => i.fieldName === field.key);

        if (field.fieldType === 'date') {
            return (
                <Select
                    size="small"
                    value={item?.format}
                    onChange={value => updateFormat(field.key, value)}
                    style={{ width: '100%' }}
                >
                    <Option value="YYYY-MM-DD">YYYY-MM-DD</Option>
                    <Option value="YYYY-MM-DD HH:mm:ss">YYYY-MM-DD HH:mm:ss</Option>
                    <Option value="timestamp">时间戳</Option>
                    <Option value="relative">相对时间</Option>
                </Select>
            );
        }

        if (field.fieldType === 'number') {
            return (
                <Select
                    size="small"
                    value={item?.format}
                    onChange={value => updateFormat(field.key, value)}
                    style={{ width: '100%' }}
                >
                    <Option value="number">数字</Option>
                    <Option value="percent">百分比</Option>
                    <Option value="currency">货币</Option>
                    <Option value="thousands">千分位</Option>
                </Select>
            );
        }

        return <span>-</span>;
    };

    // 展开/折叠所有
    const toggleExpandAll = () => {
        if (expandedKeys.length > 0) {
            setExpandedKeys([]);
        } else {
            const getAllKeys = (fields: HierarchicalFieldConfig[]): string[] => {
                return fields.flatMap(field => {
                    const keys = [field.key];
                    if (field.children) {
                        keys.push(...getAllKeys(field.children));
                    }
                    return keys;
                });
            };

            const allKeys = getAllKeys(hierarchicalFields);
            setExpandedKeys(allKeys);
        }
    };

    // 选择必填字段
    const selectRequiredFields = () => {
        const findRequiredFields = (fields: HierarchicalFieldConfig[]): string[] => {
            return fields.flatMap(field => {
                const keys: string[] = [];
                if (field.required) {
                    keys.push(field.key, ...getAllParentKeys(field.key));
                }
                if (field.children) {
                    keys.push(...findRequiredFields(field.children));
                }
                return keys;
            });
        };

        const requiredKeys = findRequiredFields(hierarchicalFields);
        setSelectedItems(prev =>
            prev.map(item => ({
                ...item,
                selected: requiredKeys.includes(item.fieldName)
            }))
        );
    };

    // 统计信息
    const stats = {
        total: selectedItems.length,
        selected: selectedItems.filter(item => item.selected).length,
        required: hierarchicalFields.filter(f => f.required).length
    };

    return (
        <Card
            title="字段选择器"
            extra={
                <Space>
                    <div style={{ fontSize: 12, color: '#666' }}>
                        已选: <strong style={{ color: '#1890ff' }}>{stats.selected}</strong> / {stats.total}
                    </div>
                    <Input.Search
                        placeholder="搜索字段..."
                        style={{ width: 200 }}
                        value={searchText}
                        onChange={e => setSearchText(e.target.value)}
                        allowClear
                    />
                </Space>
            }
        >
            <Row gutter={16} style={{ marginBottom: 16 }}>
                <Col span={24}>
                    <Space>
                        <Button
                            type="primary"
                            icon={<DatabaseOutlined />}
                            onClick={handleGenerateData}
                            loading={loading}
                            disabled={stats.selected === 0}
                        >
                            生成数据 ({stats.selected})
                        </Button>
                        <Button
                            onClick={() => {
                                setSelectedItems(prev =>
                                    prev.map(item => ({ ...item, selected: false }))
                                );
                                setSelectAll(false);
                            }}
                        >
                            清除选择
                        </Button>
                        <Button
                            onClick={selectRequiredFields}
                        >
                            选择必填项
                        </Button>
                        <Button
                            onClick={toggleExpandAll}
                        >
                            {expandedKeys.length > 0 ? '折叠全部' : '展开全部'}
                        </Button>
                    </Space>
                </Col>
            </Row>

            {/* 树形表格 */}
            <div style={{ border: '1px solid #f0f0f0', borderRadius: 4, overflow: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                    <tr style={{ background: '#fafafa' }}>
                        <th style={{ width: 80, padding: '12px 8px', textAlign: 'center' }}>
                            <Checkbox
                                checked={selectAll}
                                onChange={toggleSelectAll}
                                indeterminate={
                                    selectedItems.some(item => item.selected) &&
                                    !selectedItems.every(item => item.selected)
                                }
                            >
                                全选
                            </Checkbox>
                        </th>
                        <th style={{ padding: '12px 8px', textAlign: 'left' }}>字段名</th>
                        <th style={{ width: 100, padding: '12px 8px', textAlign: 'center' }}>类型</th>
                        <th style={{ padding: '12px 8px', textAlign: 'left' }}>描述</th>
                        <th style={{ width: 150, padding: '12px 8px', textAlign: 'center' }}>显示别名</th>
                        <th style={{ width: 150, padding: '12px 8px', textAlign: 'center' }}>格式化</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredFields.length > 0 ? (
                        filteredFields.map(field => renderFieldRow(field))
                    ) : (
                        <tr>
                            <td colSpan={6} style={{ textAlign: 'center', padding: '40px' }}>
                                {searchText ? '没有找到匹配的字段' : '暂无字段数据'}
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            {/* 字段路径信息 */}
            {selectedItems.some(item => item.selected) && (
                <div style={{ marginTop: 16, padding: 12, background: '#f6ffed', borderRadius: 4 }}>
                    <div style={{ fontWeight: 'bold', marginBottom: 8 }}>
                        已选择的字段路径:
                    </div>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
                        {selectedItems
                            .filter(item => item.selected)
                            .map(item => (
                                <Tag key={item.fieldName} color="blue">
                                    {item.fieldName}
                                    {item.alias && item.alias !== item.fieldName && ` → ${item.alias}`}
                                </Tag>
                            ))}
                    </div>
                </div>
            )}
        </Card>
    );
};

export default FieldSelectorPanel;