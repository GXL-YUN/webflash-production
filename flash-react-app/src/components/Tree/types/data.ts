// types/data.ts
export interface TreeNode {
    key: string;
    title: string;
    children?: TreeNode[];
    level: number; // 层级，0开始
    parentKey?: string;
    expanded?: boolean;
    mergeable?: boolean; // 是否可合并
    merged?: boolean; // 当前是否已合并
}