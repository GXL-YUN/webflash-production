// components/MultiLevelTable2.tsx
import React, { useState, useEffect, useCallback } from 'react';
import {
    Table,
    Button,
    Space,
    Tooltip,
    Row,
    Col,
    Switch,
    Typography
} from 'antd';
import {
    DownOutlined,
    RightOutlined,
    MergeCellsOutlined,
    SplitCellsOutlined,
    LinkOutlined
} from '@ant-design/icons';
import { TreeNode } from '../types/data2';

const { Text } = Typography;

interface MultiLevelTableProps {
    dataSource: TreeNode[];
    columns?: any[];
    defaultExpandAll?: boolean;
    showMergedView?: boolean;
    className?: string;
    onNodeClick?: (node: TreeNode) => void;
    showNodeLink?: boolean;
    renderNodeLink?: (node: TreeNode) => React.ReactNode;
}

const MultiLevelTable: React.FC<MultiLevelTableProps> = ({
                                                             dataSource,
                                                             columns,
                                                             defaultExpandAll = false,
                                                             showMergedView = false,
                                                             className = '',
                                                             onNodeClick,
                                                             showNodeLink = false,
                                                             renderNodeLink
                                                         }) => {
    const [data, setData] = useState<TreeNode[]>([]);
    const [mergedMode, setMergedMode] = useState(showMergedView);
    const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([]);

    // 初始化数据
    useEffect(() => {
        const initData = (nodes: TreeNode[], parentKey?: string): TreeNode[] => {
            return nodes.map((node, index) => {
                const key = parentKey ? parentKey + '-' + index : index.toString();
                const newNode: TreeNode = {
                    ...node,
                    key,
                    level: parentKey ? parentKey.split('-').length : 0,
                    parentKey,
                    expanded: defaultExpandAll,
                    merged: false,
                    children: node.children ? initData(node.children, key) : undefined
                };
                return newNode;
            });
        };

        const processedData = initData(dataSource);
        setData(processedData);

        if (defaultExpandAll) {
            const allKeys = getAllKeys(processedData);
            setExpandedRowKeys(allKeys);
        } else {
            // 默认不展开任何节点
            setExpandedRowKeys([]);
        }
    }, [dataSource, defaultExpandAll]);

    // 获取所有节点key
    const getAllKeys = useCallback((nodes: TreeNode[]): string[] => {
        let keys: string[] = [];
        for (let i = 0; i < nodes.length; i++) {
            const node = nodes[i];
            keys.push(node.key);
            if (node.children && node.children.length > 0) {
                keys = keys.concat(getAllKeys(node.children));
            }
        }
        return keys;
    }, []);

    // 获取节点及其所有子节点的key
    const getAllChildKeys = useCallback((node: TreeNode): string[] => {
        let keys: string[] = [];
        if (node.children && node.children.length > 0) {
            for (let i = 0; i < node.children.length; i++) {
                const child = node.children[i];
                keys.push(child.key);
                keys = keys.concat(getAllChildKeys(child));
            }
        }
        return keys;
    }, []);

    // 处理节点点击事件
    const handleNodeClick = (record: TreeNode) => {
        if (onNodeClick) {
            onNodeClick(record);
        }
    };

    // 展开/收起行 - 使用Ant Design的expandable控制
    const handleExpand = (record: TreeNode, expanded: boolean, event?: React.MouseEvent) => {
        if (event) {
            event.stopPropagation();
        }

        // 更新展开状态
        if (expanded) {
            // 展开：添加当前节点
            if (!expandedRowKeys.includes(record.key)) {
                setExpandedRowKeys(prev => [...prev, record.key]);
            }
        } else {
            // 收起：只移除当前节点（Ant Design会自动收起子节点）
            setExpandedRowKeys(prev =>
                prev.filter(key => key !== record.key)
            );
        }
    };

    // 合并/取消合并节点
    const handleMerge = (record: TreeNode, event: React.MouseEvent) => {
        event.stopPropagation();

        const updatedData = [...data];
        const wasMerged = record.merged;
        const newMergedState = !wasMerged;

        const updateNode = (nodes: TreeNode[]): boolean => {
            for (let i = 0; i < nodes.length; i++) {
                const node = nodes[i];
                if (node.key === record.key) {
                    node.merged = newMergedState;

                    // 更新展开状态
                    if (newMergedState) {
                        // 合并时，收起该节点
                        setExpandedRowKeys(prev =>
                            prev.filter(key => key !== node.key)
                        );
                    }
                    // 取消合并时不自动展开

                    return true;
                }
                if (node.children) {
                    if (updateNode(node.children)) {
                        return true;
                    }
                }
            }
            return false;
        };

        updateNode(updatedData);
        setData(updatedData);
    };

    // 批量展开/收起所有
    const expandAll = () => {
        const updatedData = [...data];
        const allKeys: string[] = [];

        const expandNodes = (nodes: TreeNode[]) => {
            for (let i = 0; i < nodes.length; i++) {
                const node = nodes[i];
                node.expanded = true;
                node.merged = false;
                allKeys.push(node.key);
                if (node.children && node.children.length > 0) {
                    expandNodes(node.children);
                }
            }
        };

        expandNodes(updatedData);
        setData(updatedData);
        setExpandedRowKeys(allKeys);
    };

    const collapseAll = () => {
        const updatedData = [...data];

        const collapseNodes = (nodes: TreeNode[]) => {
            for (let i = 0; i < nodes.length; i++) {
                const node = nodes[i];
                node.expanded = false;
                if (node.children && node.children.length > 0) {
                    collapseNodes(node.children);
                }
            }
        };

        collapseNodes(updatedData);
        setData(updatedData);
        setExpandedRowKeys([]);
    };

    // 获取显示数据 - 核心修复：让Ant Design Table完全控制展开
    const getDisplayData = (nodes: TreeNode[]): TreeNode[] => {
        const result: TreeNode[] = [];

        const processNodes = (nodeList: TreeNode[]) => {
            for (let i = 0; i < nodeList.length; i++) {
                const node = nodeList[i];

                // 对于合并模式下的已合并节点，需要特殊处理
                const displayNode = { ...node };

                result.push(displayNode);

                // 只有在合并模式下且节点未合并，且节点展开时，才处理子节点
                // 让Ant Design Table的expandable来控制子节点的显示
            }
        };

        processNodes(nodes);
        return result;
    };

    // 处理合并模式切换
    const handleMergeModeChange = (checked: boolean) => {
        setMergedMode(checked);

        if (checked) {
            // 切换到合并模式时，收起所有节点
            setExpandedRowKeys([]);

            // 更新所有节点的合并状态（保持原样）
            const updatedData = [...data];
            // 这里可以根据需要重置合并状态
            setData(updatedData);
        }
    };

    // 判断节点是否可展开
    const isRowExpandable = (record: TreeNode) => {
        // 在合并模式下，已合并的节点不可展开
        if (mergedMode && record.merged) return false;

        // 没有子节点时不可展开
        if (!record.children || record.children.length === 0) return false;

        return true;
    };

    // 获取应该显示的children（用于合并模式）
    const getDisplayChildren = (record: TreeNode) => {
        // 在合并模式下，已合并的节点显示空children
        if (mergedMode && record.merged) {
            return [];
        }
        return record.children;
    };

    // 默认列配置
    const defaultColumns = [
        {
            title: '名称',
            dataIndex: 'title',
            key: 'title',
            width: '40%',
            render: (text: string, record: TreeNode) => {
                const indentStyle = {
                    paddingLeft: (record.level * 24) + 'px',
                    display: 'flex',
                    alignItems: 'center'
                } as React.CSSProperties;

                const hasChildren = record.children && record.children.length > 0;
                const isMerged = mergedMode && record.merged;
                const showExpandIcon = hasChildren && !isMerged;
                const isExpanded = expandedRowKeys.includes(record.key);

                // 渲染节点内容
                const renderNodeContent = () => {
                    if (isMerged && hasChildren) {
                        // 合并状态的节点
                        return (
                            <Text style={{ color: '#fa8c16', fontWeight: 600 }}>
                                {text} (已合并 {record.children!.length} 个子项)
                            </Text>
                        );
                    }

                    if (renderNodeLink) {
                        return renderNodeLink(record);
                    }

                    if (showNodeLink) {
                        return (
                            <a
                                href="#"
                                onClick={(e) => {
                                    e.preventDefault();
                                    e.stopPropagation();
                                    handleNodeClick(record);
                                }}
                                style={{
                                    color: '#1890ff',
                                    textDecoration: 'none',
                                    display: 'flex',
                                    alignItems: 'center'
                                }}
                            >
                                <LinkOutlined style={{ marginRight: 4 }} />
                                {text}
                            </a>
                        );
                    }

                    return (
                        <span
                            onClick={() => handleNodeClick(record)}
                            style={{
                                fontWeight: 500,
                                color: '#333',
                                cursor: 'pointer'
                            }}
                        >
                            {text}
                        </span>
                    );
                };

                return (
                    <div
                        style={indentStyle}
                        onClick={() => handleNodeClick(record)}
                    >
                        {showExpandIcon ? (
                            <Button
                                type="text"
                                size="small"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleExpand(record, !isExpanded, e);
                                }}
                                icon={isExpanded ? <DownOutlined /> : <RightOutlined />}
                                style={{
                                    color: '#1890ff',
                                    background: 'transparent',
                                    border: 'none',
                                    padding: 4,
                                    marginRight: 8
                                }}
                            />
                        ) : (
                            <span style={{ width: 24, display: 'inline-block' }} />
                        )}

                        {renderNodeContent()}

                        {hasChildren && !mergedMode && (
                            <Tooltip title={record.merged ? "取消合并" : "合并显示"}>
                                <Button
                                    type="text"
                                    size="small"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        handleMerge(record, e);
                                    }}
                                    icon={record.merged ? <SplitCellsOutlined /> : <MergeCellsOutlined />}
                                    style={{
                                        color: '#fa8c16',
                                        background: 'transparent',
                                        border: 'none',
                                        padding: 4,
                                        marginLeft: 8
                                    }}
                                />
                            </Tooltip>
                        )}
                    </div>
                );
            }
        },
        {
            title: '层级',
            dataIndex: 'level',
            key: 'level',
            width: '15%',
            render: (level: number) => (
                <span style={{
                    display: 'inline-block',
                    padding: '2px 8px',
                    borderRadius: '12px',
                    fontSize: '12px',
                    fontWeight: 500,
                    background: '#e6f7ff',
                    color: '#1890ff',
                    border: '1px solid #91d5ff'
                }}>
                    第 {level + 1} 层
                </span>
            )
        },
        {
            title: '子节点数量',
            dataIndex: 'children',
            key: 'childrenCount',
            width: '15%',
            render: (children: TreeNode[] | undefined, record: TreeNode) => {
                const hasChildren = record.children && record.children.length > 0;
                const isMerged = mergedMode && record.merged;

                if (isMerged && record.children) {
                    return (
                        <span style={{
                            display: 'inline-block',
                            width: '24px',
                            height: '24px',
                            lineHeight: '24px',
                            textAlign: 'center',
                            background: '#fff7e6',
                            color: '#fa8c16',
                            borderRadius: '50%',
                            fontWeight: 600,
                            border: '1px solid #ffd591'
                        }}>
                            {record.children.length}
                        </span>
                    );
                }

                if (hasChildren && !isMerged) {
                    return (
                        <span style={{
                            display: 'inline-block',
                            width: '24px',
                            height: '24px',
                            lineHeight: '24px',
                            textAlign: 'center',
                            background: '#f6ffed',
                            color: '#52c41a',
                            borderRadius: '50%',
                            fontWeight: 600,
                            border: '1px solid #b7eb8f'
                        }}>
                            {record.children!.length}
                        </span>
                    );
                }

                return <span>0</span>;
            }
        },
        {
            title: '状态',
            key: 'status',
            width: '30%',
            render: (_: any, record: TreeNode) => {
                const hasChildren = record.children && record.children.length > 0;
                const isExpanded = expandedRowKeys.includes(record.key);
                const isMerged = mergedMode && record.merged;

                return (
                    <Space style={{ display: 'flex', alignItems: 'center' }}>
                        {hasChildren && !isMerged && (
                            <Text
                                type={isExpanded ? "success" : undefined}
                                style={isExpanded ? {
                                    color: '#52c41a',
                                    padding: '4px 8px',
                                    background: '#f6ffed',
                                    borderRadius: '4px',
                                    border: '1px solid #b7eb8f'
                                } : {
                                    color: '#8c8c8c',
                                    padding: '4px 8px',
                                    background: '#fafafa',
                                    borderRadius: '4px',
                                    border: '1px solid #d9d9d9'
                                }}
                            >
                                {isExpanded ? "已展开" : "已收起"}
                            </Text>
                        )}
                        {isMerged && (
                            <Text type="warning" style={{
                                padding: '4px 8px',
                                background: '#fff7e6',
                                borderRadius: '4px',
                                border: '1px solid #ffd591',
                                marginLeft: '8px'
                            }}>
                                已合并
                            </Text>
                        )}
                        {/* 添加跳转按钮 */}
                        {onNodeClick && (
                            <Button
                                type="link"
                                size="small"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleNodeClick(record);
                                }}
                                icon={<LinkOutlined />}
                                style={{
                                    color: '#1890ff',
                                    borderColor: '#1890ff',
                                    marginLeft: '8px'
                                }}
                            >
                                查看
                            </Button>
                        )}
                    </Space>
                );
            }
        }
    ];

    const displayColumns = columns || defaultColumns;
    const displayData = getDisplayData(data);

    return (
        <div style={{
            padding: '24px',
            background: '#fff',
            borderRadius: '8px',
            boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)'
        }}>
            <Row justify="space-between" align="middle" style={{
                marginBottom: '16px',
                padding: '12px 16px',
                background: '#fafafa',
                borderRadius: '6px',
                border: '1px solid #f0f0f0'
            }}>
                <Col>
                    <Space>
                        <Button onClick={expandAll} style={{
                            background: '#1890ff',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px'
                        }}>展开全部</Button>
                        <Button onClick={collapseAll} style={{
                            background: '#1890ff',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px'
                        }}>收起全部</Button>
                    </Space>
                </Col>
                <Col>
                    <Space>
                        <Text style={{ fontWeight: 500, color: '#333' }}>合并模式</Text>
                        <Switch
                            checked={mergedMode}
                            onChange={handleMergeModeChange}
                            checkedChildren="开启"
                            unCheckedChildren="关闭"
                            style={{ background: '#52c41a' }}
                        />
                    </Space>
                </Col>
            </Row>

            <Table
                columns={displayColumns}
                dataSource={displayData}
                rowKey="key"
                pagination={false}
                bordered
                size="middle"
                style={{
                    borderRadius: '8px',
                    overflow: 'hidden'
                }}
                expandable={{
                    expandedRowKeys,
                    onExpand: (expanded, record) => {
                        handleExpand(record as TreeNode, expanded);
                    },
                    rowExpandable: (record) => {
                        const node = record as TreeNode;
                        return isRowExpandable(node);
                    },
                    // 关键：处理children的显示
                    childrenColumnName: mergedMode ? undefined : 'children'
                }}
                rowClassName={(record) => {
                    const row = record as TreeNode;
                    const classes = [];
                    if (expandedRowKeys.includes(row.key)) classes.push('expanded');
                    if (row.merged) classes.push('merged');
                    if (onNodeClick) classes.push('clickable');

                    // 添加层级样式
                    if (row.level === 0) classes.push('level-0');
                    else if (row.level === 1) classes.push('level-1');
                    else if (row.level === 2) classes.push('level-2');
                    else if (row.level === 3) classes.push('level-3');
                    else if (row.level === 4) classes.push('level-4');

                    return classes.join(' ');
                }}
                onRow={(record) => {
                    const node = record as TreeNode;
                    return {
                        onClick: () => handleNodeClick(node),
                        style: {
                            cursor: onNodeClick ? 'pointer' : 'default',
                            backgroundColor: node.level === 0 ? '#f0f8ff' :
                                node.level === 1 ? '#f9f0ff' :
                                    node.level === 2 ? '#f6ffed' :
                                        node.level === 3 ? '#fff1f0' :
                                            node.level === 4 ? '#fff7e6' : '#fff'
                        }
                    };
                }}
            />
        </div>
    );
};

export default MultiLevelTable;