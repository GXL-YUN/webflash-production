// src/pages/NoteDetail/index.tsx
import React from 'react';
import { useParams } from 'react-router-dom';
import { Card, Tag, Button, Space, Divider } from 'antd';
import { EditOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import CodeHighlighter from '../../components/CodeHighlighter';
import { mockNotes } from '../../data/mockData';

// 定义正确的类型
interface CodeProps {
    node?: any;
    inline?: boolean;
    className?: string;
    children?: React.ReactNode;
    [key: string]: any;
}

const NoteDetail: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const note = mockNotes.find(n => n.id === id);

    if (!note) {
        return <div>笔记不存在</div>;
    }

    const renderContent = () => {
        if (note.type === 'markdown') {
            return (
                <ReactMarkdown
                    remarkPlugins={[remarkGfm]}
                    components={{
                        code: (props: CodeProps) => {
                            const { node, inline, className, children, ...rest } = props;
                            const match = /language-(\w+)/.exec(className || '');

                            // 如果是代码块且有语言标识
                            if (!inline && match) {
                                return (
                                    <CodeHighlighter
                                        language={match[1]}
                                        value={String(children).replace(/\n$/, '')}
                                        readOnly
                                    />
                                );
                            }

                            // 如果是行内代码
                            return (
                                <code className={className} {...rest}>
                                    {children}
                                </code>
                            );
                        }
                    }}
                >
                    {note.content}
                </ReactMarkdown>
            );
        } else {
            return (
                <CodeHighlighter
                    language={note.language || 'text'}
                    value={note.content}
                    readOnly
                />
            );
        }
    };

    return (
        <div style={{ padding: '24px' }}>
            <Space style={{ marginBottom: 16 }}>
                <Button
                    icon={<ArrowLeftOutlined />}
                    onClick={() => window.history.back()}
                >
                    返回
                </Button>
                <Button
                    type="primary"
                    icon={<EditOutlined />}
                    onClick={() => window.location.href = `/edit/${note.id}`}
                >
                    编辑
                </Button>
            </Space>

            <Card>
                <div style={{ textAlign: 'center', marginBottom: 24 }}>
                    <h1>{note.title}</h1>
                    <Space>
                        <Tag color="blue">{note.type}</Tag>
                        <Tag color="green">{note.category}</Tag>
                        {note.language && <Tag color="orange">{note.language}</Tag>}
                        <span>创建时间: {new Date(note.createTime).toLocaleString()}</span>
                    </Space>
                    <div style={{ marginTop: 8 }}>
                        {note.tags.map(tag => (
                            <Tag key={tag}>{tag}</Tag>
                        ))}
                    </div>
                </div>

                <Divider />

                <div style={{ minHeight: 400 }}>
                    {renderContent()}
                </div>
            </Card>
        </div>
    );
};

export default NoteDetail;