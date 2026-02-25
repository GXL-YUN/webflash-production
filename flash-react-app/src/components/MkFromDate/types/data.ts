// types/data.ts
export interface ApiResponse<T = any> {
    code: number;
    message: string;
    data: T;
    timestamp: number;
}

export interface SearchParams {
    keyword?: string;
    dateRange?: [string, string];
    type?: string;
    pageSize?: number;
    pageNum?: number;
}

export interface FieldDefinition {
    fieldName: string;
    fieldType: 'string' | 'number' | 'boolean' | 'object' | 'array' | 'date';
    displayName: string;
    description?: string;
    isSelectable: boolean;
    relatedEndpoint?: string;
    validationRules?: ValidationRule[];
    defaultValue?: any;
}

export interface ValidationRule {
    rule: 'required' | 'min' | 'max' | 'pattern';
    value: any;
    message: string;
}

export interface ListData {
    id: string | number;
    [key: string]: any;
    createdTime?: string;
    updatedTime?: string;
}

export interface SelectedData {
    [key: string]: any[] | number | string;  // 允许多种类型
    timestamp: number;
    batchId: string;  // 改为数组
}