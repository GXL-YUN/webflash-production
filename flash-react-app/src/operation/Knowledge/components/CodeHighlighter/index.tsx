// src/components/CodeHighlighter/index.tsx
import React from 'react';
import { Input } from 'antd';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

const { TextArea } = Input;

interface CodeHighlighterProps {
    value?: string;
    language?: string;
    onChange?: (value: string) => void;
    readOnly?: boolean;
}

const CodeHighlighter: React.FC<CodeHighlighterProps> = ({
                                                             value = '',
                                                             language = 'javascript',
                                                             onChange,
                                                             readOnly = false
                                                         }) => {
    if (readOnly) {
        return (
            <SyntaxHighlighter
                language={language}
                style={vscDarkPlus}
                showLineNumbers
                customStyle={{ borderRadius: 6 }}
            >
                {value}
            </SyntaxHighlighter>
        );
    }

    return (
        <TextArea
            rows={15}
            value={value}
            onChange={(e) => onChange?.(e.target.value)}
            placeholder={`输入${language}代码...`}
            style={{ fontFamily: 'Monaco, Consolas, monospace' }}
        />
    );
};

export default CodeHighlighter;