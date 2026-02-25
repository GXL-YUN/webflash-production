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
        </div>
    );
};

export default MarkdownEditor;