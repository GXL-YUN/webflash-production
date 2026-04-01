// src/types.ts
export interface AppCardItem {
    key: string; // 唯一标识
    title: string; // 标题
    description?: string; // 描述
    group:string;
    icon: string | React.ReactNode; // 图标（图片URL、图标名、或React组件）
    iconType?: 'antd' | 'image' | 'custom'; // 图标类型
    iconColor?: string; // 图标颜色
    disabled?: boolean; // 是否禁用
    onClick?: () => void; // 点击事件
    badge?: number; // 角标数字
    badgeColor?: string; // 角标颜色
    url:string
}