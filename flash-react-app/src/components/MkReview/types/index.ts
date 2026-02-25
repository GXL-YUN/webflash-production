// 基础字段配置
export interface BaseFieldConfig {
    fieldName: string;
    fieldType: 'string' | 'number' | 'boolean' | 'date' | 'object' | 'array';
    description: string;
    required: boolean;
    defaultValue?: any;
    parentKey?: string;
    isLeaf?: boolean;
    expanded?: boolean;
    level?: number;
}

// 普通字段配置（用于数据存储）
export interface FieldConfig extends BaseFieldConfig {
    children?: FieldConfig[];
}

// 层级字段配置（用于UI展示）
export interface HierarchicalFieldConfig extends BaseFieldConfig {
    key: string;
    level: number;
    path: string;
    children?: HierarchicalFieldConfig[];
    expanded: boolean;
    isLeaf: boolean;
}

// 选择的数据项
export interface SelectedDataItem {
    fieldName: string;
    selected: boolean;
    alias?: string;
    format?: string;
    fieldType?: 'string' | 'number' | 'boolean' | 'date' | 'object' | 'array';
}