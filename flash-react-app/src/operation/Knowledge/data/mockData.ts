// src/data/mockData.ts
import { KnowledgeCategory, KnowledgeNote } from '../types/note';

export const mockCategories: KnowledgeCategory[] = [
    { id: 'frontend', name: '前端开发', count: 15 },
    { id: 'backend', name: '后端开发', count: 8 },
    { id: 'database', name: '数据库', count: 5 },
    { id: 'algorithm', name: '算法', count: 12 },
];

export const mockNotes: KnowledgeNote[] = [
    {
        id: '1',
        title: 'React Hooks 使用指南',
        content: 'React Hooks 是 React 16.8 的新特性，让你在函数组件中使用 state 和其他 React 特性...',
        category: 'frontend',
        tags: ['React', 'Hooks'],
        createTime: '2024-01-15',
        updateTime: '2024-01-20',
        type: 'markdown'
    },
    {
        id: '2',
        title: 'TypeScript 泛型示例',
        content: 'function identity<T>(arg: T): T { return arg; }',
        category: 'frontend',
        tags: ['TypeScript'],
        createTime: '2024-01-18',
        updateTime: '2024-01-18',
        type: 'code',
        language: 'typescript'
    }
];