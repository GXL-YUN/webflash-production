// src/types/note.ts
export interface KnowledgeCategory {
    id: string;
    name: string;
    icon?: string;
    count: number;
}

export interface KnowledgeNote {
    id: string;
    title: string;
    content: string;
    category: string;
    tags: string[];
    createTime: string;
    updateTime: string;
    type: 'markdown' | 'code';
    language?: string; // 编程语言
}

export interface CreateNoteForm {
    title: string;
    content: string;
    category: string;
    tags: string[];
    type: 'markdown' | 'code';
    language?: string;
}