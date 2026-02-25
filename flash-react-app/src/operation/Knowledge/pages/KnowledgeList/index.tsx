// src/pages/KnowledgeList/index.tsx
import React, { useState, useEffect } from 'react';
import { Layout, Menu, Card, List, Tag, Input, Button } from 'antd';
import {
    CodeOutlined,
    FileTextOutlined,
    SearchOutlined,
    PlusOutlined
} from '@ant-design/icons';
import { KnowledgeCategory, KnowledgeNote } from '../../types/note';
import { mockCategories, mockNotes } from '../../data/mockData';

const { Sider, Content } = Layout;
const { Search } = Input;

const KnowledgeList: React.FC = () => {
    const [categories, setCategories] = useState<KnowledgeCategory[]>([]);
    const [notes, setNotes] = useState<KnowledgeNote[]>([]);
    const [selectedCategory, setSelectedCategory] = useState<string>('all');
    const [searchText, setSearchText] = useState<string>('');

    useEffect(() => {
        // 模拟数据加载
        setCategories(mockCategories);
        setNotes(mockNotes);
    }, []);

    const filteredNotes = notes.filter(note => {
        const categoryMatch = selectedCategory === 'all' || note.category === selectedCategory;
        const searchMatch = note.title.includes(searchText) || note.content.includes(searchText);
        return categoryMatch && searchMatch;
    });

    const menuItems = [
        {
            key: 'all',
            icon: <FileTextOutlined />,
            label: `全部笔记 (${notes.length})`
        },
        ...categories.map(cat => ({
            key: cat.id,
            icon: cat.icon ? <CodeOutlined /> : <FileTextOutlined />,
            label: `${cat.name} (${cat.count})`
        }))
    ];

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider width={250} theme="light">
                <div style={{ padding: '16px' }}>
                    <Button
                        type="primary"
                        icon={<PlusOutlined />}
                        block
                        onClick={() => window.location.href = '/KnowledgeList/add'}
                    >
                        新建笔记
                    </Button>
                </div>

                <Menu
                    mode="inline"
                    selectedKeys={[selectedCategory]}
                    items={menuItems}
                    onClick={({ key }) => setSelectedCategory(key)}
                />
            </Sider>

            <Content style={{ padding: '24px' }}>
                <div style={{ marginBottom: 16 }}>
                    <Search
                        placeholder="搜索笔记..."
                        allowClear
                        enterButton={<SearchOutlined />}
                        size="large"
                        onSearch={setSearchText}
                        onChange={(e) => setSearchText(e.target.value)}
                    />
                </div>

                <List
                    grid={{ gutter: 16, column: 2 }}
                    dataSource={filteredNotes}
                    renderItem={note => (
                        <List.Item>
                            <Card
                                title={note.title}
                                extra={<Tag color="blue">{note.type}</Tag>}
                                hoverable
                                onClick={() => window.location.href = `/Knowledge/view/${note.id}`}
                            >
                                <div style={{ height: 80, overflow: 'hidden' }}>
                                    {note.content.substring(0, 100)}...
                                </div>
                                <div style={{ marginTop: 8 }}>
                                    {note.tags.map(tag => (
                                        <Tag key={tag} color="green">{tag}</Tag>
                                    ))}
                                </div>
                                <div style={{ marginTop: 8, color: '#999', fontSize: 12 }}>
                                    更新于: {new Date(note.updateTime).toLocaleDateString()}
                                </div>
                            </Card>
                        </List.Item>
                    )}
                />
            </Content>
        </Layout>
    );
};

export default KnowledgeList;