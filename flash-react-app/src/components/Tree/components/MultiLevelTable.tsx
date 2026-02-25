// components/Tree/components/MultiLevelTable.tsx
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
    SplitCellsOutlined
} from '@ant-design/icons';
import { TreeNode } from '../types/data';
import '../css/MultiLevelTable.css';

const { Text } = Typography;

interface MultiLevelTableProps {
    dataSource: TreeNode[];
    columns?: any[];
    defaultExpandAll?: boolean;
    showMergedView?: boolean;
    className?: string;
}

const MultiLevelTable: React.FC<MultiLevelTableProps> = ({
                                                             dataSource,
                                                             columns,
                                                             defaultExpandAll = false,
                                                             showMergedView = false,
                                                             className = ''
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

    // 展开/收起行
    const handleExpand = (record: TreeNode, expanded: boolean) => {
        console.log('handleExpand', record.key, expanded, expandedRowKeys);

        if (expanded) {
            // 展开：添加当前节点
            if (!expandedRowKeys.includes(record.key)) {
                setExpandedRowKeys(prev => [...prev, record.key]);
            }
        } else {
            // 收起：移除当前节点及其所有子节点
            const childKeys = getAllChildKeys(record);
            const keysToRemove = [record.key, ...childKeys];
            setExpandedRowKeys(prev =>
                prev.filter(key => !keysToRemove.includes(key))
            );
        }
    };

    // 合并/取消合并节点
    const handleMerge = (record: TreeNode) => {
        const updatedData = [...data];

        const toggleMerge = (nodes: TreeNode[], targetKey: string): boolean => {
            for (let i = 0; i < nodes.length; i++) {
                const node = nodes[i];
                if (node.key === targetKey && node.children && node.children.length > 0) {
                    const newMergedState = !node.merged;
                    node.merged = newMergedState;

                    // 更新数据中的展开状态
                    const updateNodeExpanded = (nodeList: TreeNode[]) => {
                        for (let j = 0; j < nodeList.length; j++) {
                            const n = nodeList[j];
                            if (n.key === targetKey) {
                                n.expanded = !newMergedState; // 合并时收起，取消合并时展开
                            }
                            if (n.children && n.children.length > 0) {
                                updateNodeExpanded(n.children);
                            }
                        }
                    };

                    updateNodeExpanded(updatedData);

                    // 更新展开状态
                    if (newMergedState) {
                        // 合并时，从展开列表中移除该节点及其子节点
                        const childKeys = getAllChildKeys(node);
                        const keysToRemove = [node.key, ...childKeys];
                        setExpandedRowKeys(prev =>
                            prev.filter(key => !keysToRemove.includes(key))
                        );
                    } else {
                        // 取消合并时，展开该节点
                        if (!expandedRowKeys.includes(node.key)) {
                            setExpandedRowKeys(prev => [...prev, node.key]);
                        }
                    }

                    return true;
                }
                if (node.children) {
                    if (toggleMerge(node.children, targetKey)) {
                        return true;
                    }
                }
            }
            return false;
        };

        toggleMerge(updatedData, record.key);
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
                node.merged = false; // 展开全部时取消合并状态
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

    // 获取要显示的扁平化数据
    const getFlattenedData = (nodes: TreeNode[], showMerged: boolean): TreeNode[] => {
        const result: TreeNode[] = [];

        const flattenNodes = (nodeList: TreeNode[], level: number = 0) => {
            for (let i = 0; i < nodeList.length; i++) {
                const node = nodeList[i];
                const displayNode = {
                    ...node,
                    level: level
                };

                // 如果启用了合并模式且节点被合并，只显示该节点
                if (showMerged && node.merged && node.children && node.children.length > 0) {
                    result.push({
                        ...displayNode,
                        title: node.title + ' (已合并 ' + node.children.length + ' 个子项)',
                        // 合并模式下，被合并的节点没有子节点（对于Table来说是叶子节点）
                    });
                } else {
                    result.push(displayNode);

                    // 如果节点展开且未合并，才递归显示子节点
                    if (node.expanded && node.children && node.children.length > 0) {
                        flattenNodes(node.children, level + 1);
                    }
                }
            }
        };

        flattenNodes(nodes, 0);
        return result;
    };

    // 处理合并模式切换
    const handleMergeModeChange = (checked: boolean) => {
        setMergedMode(checked);

        // 切换到合并模式时，收起所有已合并的节点
        if (checked) {
            const updatedData = [...data];
            const mergedNodeKeys: string[] = [];

            const findMergedNodes = (nodes: TreeNode[]) => {
                for (let i = 0; i < nodes.length; i++) {
                    const node = nodes[i];
                    if (node.merged && node.children && node.children.length > 0) {
                        node.expanded = false;
                        const childKeys = getAllChildKeys(node);
                        mergedNodeKeys.push(...[node.key, ...childKeys]);
                    }
                    if (node.children && node.children.length > 0) {
                        findMergedNodes(node.children);
                    }
                }
            };

            findMergedNodes(updatedData);
            setData(updatedData);

            // 从展开列表中移除已合并的节点及其子节点
            if (mergedNodeKeys.length > 0) {
                setExpandedRowKeys(prev =>
                    prev.filter(key => !mergedNodeKeys.includes(key))
                );
            }
        }
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
                const canExpand = hasChildren && !(mergedMode && record.merged);

                return (
                    <div style={indentStyle} className={'tree-node level-' + record.level}>
                        {canExpand ? (
                            <Button
                                type="text"
                                size="small"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleExpand(record, !expandedRowKeys.includes(record.key));
                                }}
                                icon={expandedRowKeys.includes(record.key) ? <DownOutlined /> : <RightOutlined />}
                                className="expand-btn"
                                style={{ marginRight: 8 }}
                            />
                        ) : (
                            <span style={{ width: 24, display: 'inline-block' }} />
                        )}

                        <Text className="node-title">{text}</Text>

                        {hasChildren && !mergedMode && (
                            <Tooltip title={record.merged ? "取消合并" : "合并显示"}>
                                <Button
                                    type="text"
                                    size="small"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        handleMerge(record);
                                    }}
                                    icon={record.merged ? <SplitCellsOutlined /> : <MergeCellsOutlined />}
                                    className="merge-btn"
                                    style={{ marginLeft: 8 }}
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
                <span className="level-badge">
                    第 {level + 1} 层
                </span>
            )
        },
        {
            title: '子节点数量',
            dataIndex: 'children',
            key: 'childrenCount',
            width: '15%',
            render: (children: TreeNode[] | undefined) =>
                children ? (
                    <span className="children-count">{children.length}</span>
                ) : 0
        },
        {
            title: '状态',
            key: 'status',
            width: '30%',
            render: (_: any, record: TreeNode) => {
                const hasChildren = record.children && record.children.length > 0;
                const isExpanded = expandedRowKeys.includes(record.key);

                return (
                    <Space className="status-container">
                        {hasChildren && (
                            <Text
                                type={isExpanded ? "success" : undefined}
                                className={isExpanded ? "status-expanded" : "status-collapsed"}
                            >
                                {isExpanded ? "已展开" : "已收起"}
                            </Text>
                        )}
                        {record.merged && (
                            <Text type="warning" className="status-merged">已合并</Text>
                        )}
                    </Space>
                );
            }
        }
    ];

    const displayColumns = columns || defaultColumns;
    const displayData = getFlattenedData(data, mergedMode);

    return (
        <div className={'multi-level-table-container ' + className}>
            <Row justify="space-between" align="middle" className="table-header">
                <Col>
                    <Space>
                        <Button onClick={expandAll} className="action-btn">展开全部</Button>
                        <Button onClick={collapseAll} className="action-btn">收起全部</Button>
                    </Space>
                </Col>
                <Col>
                    <Space>
                        <Text className="mode-label">合并模式</Text>
                        <Switch
                            checked={mergedMode}
                            onChange={handleMergeModeChange}
                            checkedChildren="开启"
                            unCheckedChildren="关闭"
                            className="mode-switch"
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
                className="multi-level-table"
                expandable={{
                    expandedRowKeys,
                    onExpand: (expanded, record) => {
                        handleExpand(record as TreeNode, expanded);
                    },
                    rowExpandable: (record) => {
                        const node = record as TreeNode;
                        // 在合并模式下，已合并的节点不可展开
                        if (mergedMode && node.merged) return false;
                        // 没有子节点时不可展开
                        return !!(node.children && node.children.length > 0);
                    }
                }}
                rowClassName={(record) => {
                    const row = record as TreeNode;
                    const classes = ['level-' + row.level];
                    if (expandedRowKeys.includes(row.key)) classes.push('expanded');
                    if (row.merged) classes.push('merged');
                    return classes.join(' ');
                }}
            />
        </div>
    );
};

export default MultiLevelTable;