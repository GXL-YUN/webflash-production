// src/pages/NoteCreate/index.tsx
import React, { useState } from 'react';
import {
    Form,
    Input,
    Select,
    Button,
    Card,
    Space,
    message,
    Tabs
} from 'antd';
import { SaveOutlined, CodeOutlined } from '@ant-design/icons';
import MarkdownEditor from '../../components/MarkdownEditor';
import CodeHighlighter from '../../components/CodeHighlighter';
import { CreateNoteForm } from '../../types/note';

const { TextArea } = Input;
const { Option } = Select;

const NoteCreate: React.FC = () => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const [activeTab, setActiveTab] = useState<'markdown' | 'code'>('markdown');

    const onFinish = async (values: CreateNoteForm) => {
        setLoading(true);
        try {
            // 这里调用API保存笔记
            console.log('保存笔记:', values);
            message.success('笔记保存成功');
            // 跳转到列表页
            window.location.href = '/';
        } catch (error) {
            message.error('保存失败');
        } finally {
            setLoading(false);
        }
    };

    const languageOptions = [
        'javascript', 'typescript', 'python', 'java', 'cpp',
        'css', 'html', 'sql', 'json', 'markdown'
    ];

    return (
        <div style={{ padding: '24px' }}>
            <Card>
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={onFinish}
                    initialValues={{ type: 'markdown' }}
                >
                    <Form.Item
                        name="title"
                        label="笔记标题"
                        rules={[{ required: true, message: '请输入标题' }]}
                    >
                        <Input placeholder="输入笔记标题" size="large" />
                    </Form.Item>

                    <Form.Item name="category" label="分类" required>
                        <Select placeholder="选择分类">
                            <Option value="frontend">前端开发</Option>
                            <Option value="backend">后端开发</Option>
                            <Option value="database">数据库</Option>
                            <Option value="algorithm">算法</Option>
                        </Select>
                    </Form.Item>

                    <Form.Item name="tags" label="标签">
                        <Select mode="tags" placeholder="添加标签">
                            <Option value="React">React</Option>
                            <Option value="TypeScript">TypeScript</Option>
                            <Option value="Antd">Antd</Option>
                        </Select>
                    </Form.Item>

                    <Form.Item name="type" label="内容类型">
                        <Select onChange={setActiveTab}>
                            <Option value="markdown">Markdown文档</Option>
                            <Option value="code">代码片段</Option>
                        </Select>
                    </Form.Item>

                    {activeTab === 'markdown' ? (
                        <Form.Item
                            name="content"
                            label="内容"
                            rules={[{ required: true, message: '请输入内容' }]}
                        >
                            <MarkdownEditor />
                        </Form.Item>
                    ) : (
                        <>
                            <Form.Item name="language" label="编程语言">
                                <Select placeholder="选择语言">
                                    {languageOptions.map(lang => (
                                        <Option key={lang} value={lang}>{lang}</Option>
                                    ))}
                                </Select>
                            </Form.Item>
                            <Form.Item
                                name="content"
                                label="代码"
                                rules={[{ required: true, message: '请输入代码' }]}
                            >
                                <CodeHighlighter language={form.getFieldValue('language') || 'javascript'} />
                            </Form.Item>
                        </>
                    )}

                    <Form.Item>
                        <Space>
                            <Button
                                type="primary"
                                htmlType="submit"
                                icon={<SaveOutlined />}
                                loading={loading}
                                size="large"
                            >
                                保存笔记
                            </Button>
                            <Button size="large" onClick={() => window.history.back()}>
                                取消
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default NoteCreate;