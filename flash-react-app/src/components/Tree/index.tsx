// App.tsx
import React from 'react';
import MultiLevelTable from './components/MultiLevelTable';
import { TreeNode } from './types/data';


import './css/index.css';

const App: React.FC = () => {
    // 示例数据
    const mockData: TreeNode[] = [
        {
            key: '0',
            title: '部门A',
            level: 0,
            children: [
                {
                    key: '0-0',
                    title: '开发组',
                    level: 1,
                    children: [
                        {
                            key: '0-0-0',
                            title: '前端团队',
                            level: 2,
                            children: [
                                { key: '0-0-0-0', title: '张三', level: 3 },
                                { key: '0-0-0-1', title: '李四', level: 3 },
                                { key: '0-0-0-2', title: '王五', level: 3 }
                            ]
                        },
                        {
                            key: '0-0-1',
                            title: '后端团队',
                            level: 2,
                            children: [
                                { key: '0-0-1-0', title: '赵六', level: 3 },
                                { key: '0-0-1-1', title: '孙七', level: 3 }
                            ]
                        }
                    ]
                },
                {
                    key: '0-1',
                    title: '测试组',
                    level: 1,
                    children: [
                        { key: '0-1-0', title: '周八', level: 2 },
                        { key: '0-1-1', title: '吴九', level: 2 }
                    ]
                }
            ]
        },
        {
            key: '1',
            title: '部门B',
            level: 0,
            children: [
                {
                    key: '1-0',
                    title: '产品组',
                    level: 1,
                    children: [
                        { key: '1-0-0', title: '郑十', level: 2 },
                        { key: '1-0-1', title: '陈十一', level: 2 }
                    ]
                }
            ]
        }
    ];

    return (
        <div className="App">
            <h1>多层数据展示与合并展开示例</h1>
            <MultiLevelTable
                dataSource={mockData}
                defaultExpandAll={false}
                showMergedView={false}
            />
        </div>
    );
};

export default App;