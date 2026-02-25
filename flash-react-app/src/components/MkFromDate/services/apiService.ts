// services/apiService.ts
import { SearchParams, FieldDefinition, ListData, SelectedData } from '../types/data';

export const mockApiService = {
    // 模拟获取字段定义
    async fetchFieldDefinitions(params: SearchParams): Promise<FieldDefinition[]> {
        console.log('获取字段定义，参数:', params);

        // 模拟API延迟
        await new Promise(resolve => setTimeout(resolve, 500));

        // 返回模拟数据
        return [
            {
                fieldName: 'id',
                fieldType: 'number',
                displayName: 'ID',
                description: '唯一标识符',
                isSelectable: true,
                validationRules: [{ rule: 'required', value: true, message: 'ID必填' }]
            },
            {
                fieldName: 'name',
                fieldType: 'string',
                displayName: '名称',
                description: '记录名称',
                isSelectable: true,
                validationRules: [
                    { rule: 'required', value: true, message: '名称必填' },
                    { rule: 'min', value: 2, message: '名称最少2个字符' }
                ]
            },
            {
                fieldName: 'status',
                fieldType: 'boolean',
                displayName: '状态',
                description: '启用状态',
                isSelectable: true,
                defaultValue: true
            },
            {
                fieldName: 'price',
                fieldType: 'number',
                displayName: '价格',
                description: '商品价格',
                isSelectable: true,
                validationRules: [
                    { rule: 'required', value: true, message: '价格必填' },
                    { rule: 'min', value: 0, message: '价格不能为负数' }
                ]
            },
            {
                fieldName: 'tags',
                fieldType: 'array',
                displayName: '标签',
                description: '标签数组',
                isSelectable: true
            },
            {
                fieldName: 'createdAt',
                fieldType: 'date',
                displayName: '创建时间',
                description: '记录创建时间',
                isSelectable: false
            },
            {
                fieldName: 'metadata',
                fieldType: 'object',
                displayName: '元数据',
                description: '扩展元数据',
                isSelectable: true
            }
        ];
    },

    // 模拟获取列表数据
    async fetchListData(params: SearchParams & { fields?: string[] }): Promise<ListData[]> {
        console.log('获取列表数据，参数:', params);

        await new Promise(resolve => setTimeout(resolve, 800));

        const mockData: ListData[] = [];
        for (let i = 1; i <= 50; i++) {
            const record: ListData = {
                id: i,
                name: `记录 ${i}`,
                status: i % 3 === 0,
                price: Math.random() * 1000,
                tags: [`标签${i % 5 + 1}`, `标签${i % 3 + 1}`],
                createdAt: new Date(Date.now() - i * 3600000).toISOString(),
                metadata: {
                    category: `分类${i % 4}`,
                    priority: i % 5
                }
            };

            // 如果指定了字段，只保留需要的字段
            if (params.fields && params.fields.length > 0) {
                const filteredRecord: any = { id: record.id };
                params.fields.forEach(field => {
                    if (field in record) {
                        filteredRecord[field] = (record as any)[field];
                    }
                });
                mockData.push(filteredRecord);
            } else {
                mockData.push(record);
            }
        }

        return mockData;
    },

    // 模拟提交选择的数据
    async submitSelectedData(data: SelectedData): Promise<{ success: boolean; batchId: string }> {
        console.log('提交选择的数据:', data);

        await new Promise(resolve => setTimeout(resolve, 1000));

        // 这里可以添加实际的数据处理逻辑
        return {
            success: true,
            batchId: data.batchId
        };
    }
};