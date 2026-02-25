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

    // 新增属性用于页面跳转
    url?: string; // 跳转链接
    route?: string; // 路由路径
    onClick?: () => void; // 点击回调
    icon?: string; // 图标
    badge?: number; // 徽标数字
    disabled?: boolean; // 是否禁用
    // 其他自定义属性
    [key: string]: any;
}