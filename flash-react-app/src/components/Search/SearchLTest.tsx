import React, { useState } from 'react';
import { Input, Button, List, Card, Typography, Space, Divider } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import './index.css';

const { Title, Text } = Typography;
const { Search } = Input;

// 定义搜索结果的数据类型
interface ISearchResult {
    id: number;
    title: string;
    url: string;
    description: string;
}

// 模拟搜索结果数据
const mockSearchResults: ISearchResult[] = [
    {
        id: 1,
        title: 'React 官方文档',
        url: 'https://reactjs.org',
        description: '用于构建用户界面的 JavaScript 库 - React 官方文档提供全面的教程和API参考。'
    },
    {
        id: 2,
        title: 'Ant Design - 一套企业级 UI 设计语言和 React 组件库',
        url: 'https://ant.design',
        description: 'Ant Design 为 Web 应用程序提供了丰富的基础组件，我们致力于提升用户体验和开发效率。'
    },
    {
        id: 3,
        title: '百度一下，你就知道',
        url: 'https://www.baidu.com',
        description: '百度是全球最大的中文搜索引擎，致力于让网民更便捷地获取信息，找到所求。'
    },
    {
        id: 4,
        title: 'React 教程 - 菜鸟教程',
        url: 'https://www.runoob.com/react/react-tutorial.html',
        description: 'React 是一个用于构建用户界面的 JAVASCRIPT 库。React 主要用于构建UI。'
    },
    {
        id: 5,
        title: 'Ant Design 组件总览',
        url: 'https://ant.design/components/overview-cn/',
        description: 'Ant Design 提供了丰富的组件，包括按钮、图标、布局、导航、数据录入、数据展示、反馈等。'
    }
];

const SearchLTest: React.FC = () => {
    const [searchValue, setSearchValue] = useState<string>('');
    const [searchResults, setSearchResults] = useState<ISearchResult[]>([]);
    const [hasSearched, setHasSearched] = useState<boolean>(false);

    // 处理搜索 - 添加明确的参数类型
    const handleSearch = (value: string) => {
        if (!value.trim()) return;

        setSearchValue(value);
        setHasSearched(true);

        // 模拟搜索API调用
        setTimeout(() => {
            const filteredResults = mockSearchResults.filter(item =>
                item.title.toLowerCase().includes(value.toLowerCase()) ||
                item.description.toLowerCase().includes(value.toLowerCase())
            );
            setSearchResults(filteredResults);
        }, 300);
    };

    return (
        <div className="baidu-search-container">
            <div className="search-header">
                <Title level={2} className="logo">危险化学品数据源查询</Title>
                <div className="search-box">
                    <Space.Compact size="large" style={{ width: '100%' }}>
                        <Search
                            placeholder="请输入搜索关键词"
                            allowClear
                            enterButton={<Button type="primary" icon={<SearchOutlined />}>搜索</Button>}
                            size="large"
                            onSearch={handleSearch}
                            style={{ width: '600px' }}
                        />
                    </Space.Compact>
                </div>
            </div>

            <Divider />

            <div className="search-results">
                {hasSearched ? (
                    <>
                        <div className="result-stats">
                            <Text type="secondary">找到约 {searchResults.length} 条结果（用时 0.3 秒）</Text>
                        </div>

                        {searchResults.length > 0 ? (
                            <List
                                itemLayout="vertical"
                                dataSource={searchResults}
                                renderItem={item => (
                                    <List.Item>
                                        <Card size="small" bordered={false} className="result-card">
                                            <a href={item.url} target="_blank" rel="noopener noreferrer" className="result-title">
                                                {item.title}
                                            </a>
                                            <div className="result-url">{item.url}</div>
                                            <div className="result-description">{item.description}</div>
                                        </Card>
                                    </List.Item>
                                )}
                            />
                        ) : (
                            <div className="no-results">
                                <Title level={4}>没有找到与"{searchValue}"相关的结果</Title>
                                <Text type="secondary">建议：请检查输入的关键词是否正确，或尝试使用其他关键词搜索</Text>
                            </div>
                        )}
                    </>
                ) : (
                    <div className="welcome-message">
                        <Title level={3}>欢迎使用搜索</Title>
                        <Text type="secondary">请在搜索框中输入关键词，然后点击搜索按钮</Text>
                    </div>
                )}
            </div>
        </div>
    );
};

export default SearchLTest;