// src/components/MarkdownEditor/index.tsx
import React, { useState } from 'react';
import { Input } from 'antd';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

const { TextArea } = Input;

interface MarkdownEditorProps {
    value?: string;
    active?:string;
    onChange?: (value: string) => void;
}

const MarkdownEditor: React.FC<MarkdownEditorProps> = ({ value, onChange,active }) => {
    const [activeTab, setActiveTab] = useState<'write' | 'preview'>('write');
    if(active!=""&&active!=null&&active!=undefined){
        setActiveTab("preview");

    }

    return (
        <div>
            <div style={{ marginBottom: 8 }}>
                <button
                    onClick={() => setActiveTab('write')}
                    style={{
                        padding: '8px 16px',
                        border: '1px solid #d9d9d9',
                        background: activeTab === 'write' ? '#1890ff' : 'white',
                        color: activeTab === 'write' ? 'white' : 'black'
                    }}
                >
                    编辑
                </button>
                <button
                    onClick={() => setActiveTab('preview')}
                    style={{
                        padding: '8px 16px',
                        border: '1px solid #d9d9d9',
                        background: activeTab === 'preview' ? '#1890ff' : 'white',
                        color: activeTab === 'preview' ? 'white' : 'black'
                    }}
                >
                    预览
                </button>
            </div>

            {activeTab === 'write' ? (
                <TextArea
                    rows={15}
                    value={value}
                    onChange={(e) => onChange?.(e.target.value)}
                    placeholder="输入Markdown内容..."
                />
            ) : (
                <div
                    style={{
                        border: '1px solid #d9d9d9',
                        padding: '12px',
                        minHeight: 200,
                        background: '#fafafa'
                    }}
                >
                    <ReactMarkdown remarkPlugins={[remarkGfm]}>
                        {value || '*暂无内容*'}
                    </ReactMarkdown>
                </div>
            )}
        </div>
    );
};

export default MarkdownEditor;