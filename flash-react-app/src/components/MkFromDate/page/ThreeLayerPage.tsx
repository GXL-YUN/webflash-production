// pages/ThreeLayerPage.tsx
import React, { useState, useEffect, useCallback } from 'react';
import {
    Card,
    Row,
    Col,
    Space,
    message,
    Spin,
    notification
} from 'antd';
import { SearchParams, FieldDefinition, ListData, SelectedData } from '../types/data';
import SearchPanel from '../components/SearchPanel';
import FieldDefinitionTable from '../components/FieldDefinitionTable';
import DataListTable from '../components/DataListTable';
import { mockApiService } from '../services/apiService';

const ThreeLayerPage: React.FC = () => {
    const [searchParams, setSearchParams] = useState<SearchParams>({});
    const [fieldDefinitions, setFieldDefinitions] = useState<FieldDefinition[]>([]);
    const [listData, setListData] = useState<ListData[]>([]);
    // const [selectedData, setSelectedData] = useState<SelectedData>({
    //     timestamp: Date.now(),
    //     batchId: ''
    // });
    const [selectedData, setSelectedData] = useState<SelectedData>({
        timestamp: Date.now(),  // 直接使用 number 类型
        batchId: ''
    });
    const [selectedFields, setSelectedFields] = useState<string[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [currentStep, setCurrentStep] = useState<number>(1);

    // 处理搜索
    const handleSearch = useCallback(async (params: SearchParams) => {
        try {
            setLoading(true);
            setSearchParams(params);

            // 调用接口获取字段定义
            const fieldDefs = await mockApiService.fetchFieldDefinitions(params);
            setFieldDefinitions(fieldDefs);
            setCurrentStep(2);

            notification.success({
                message: '字段定义获取成功',
                description: `共获取到 ${fieldDefs.length} 个字段定义`
            });
        } catch (error) {
            message.error('获取字段定义失败');
            console.error('Search error:', error);
        } finally {
            setLoading(false);
        }
    }, []);

    // 处理字段选择
    const handleFieldSelect = useCallback(async (selectedFieldNames: string[]) => {
        try {
            setLoading(true);
            setSelectedFields(selectedFieldNames);

            // 根据选择的字段获取数据
            const params = {
                ...searchParams,
                fields: selectedFieldNames
            };

            const data = await mockApiService.fetchListData(params);
            setListData(data);
            setCurrentStep(3);

            message.success(`已选择 ${selectedFieldNames.length} 个字段，获取 ${data.length} 条数据`);
        } catch (error) {
            message.error('获取数据失败');
            console.error('Fetch data error:', error);
        } finally {
            setLoading(false);
        }
    }, [searchParams]);

    // 处理数据选择
    const handleDataSelect = useCallback((selectedRows: ListData[]) => {
        const batchId = `BATCH_${Date.now()}`;

        const formattedData: SelectedData = {
            batchId,
            timestamp: Date.now(),
            ...selectedFields.reduce((acc, field) => {
                acc[field] = selectedRows.map(row => row[field] || null);
                return acc;
            }, {} as Record<string, any[]>)
        };

        setSelectedData(formattedData);

        // 调用接口重新封装数据
        mockApiService.submitSelectedData(formattedData)
            .then(response => {
                notification.success({
                    message: '数据提交成功',
                    description: `批次号: ${batchId}，共提交 ${selectedRows.length} 条数据`
                });
            })
            .catch(error => {
                message.error('数据提交失败');
                console.error('Submit error:', error);
            });
    }, [selectedFields]);

    // 步骤指示器
    const StepIndicator = () => (
        <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
            {[
                { step: 1, title: '参数配置', description: '设置搜索条件' },
                { step: 2, title: '字段定义', description: '选择需要展示的字段' },
                { step: 3, title: '数据展示', description: '查看和选择数据' }
            ].map(item => (
                <Col span={8} key={item.step}>
                    <Card
                        size="small"
                        style={{
                            borderColor: currentStep >= item.step ? '#1890ff' : '#f0f0f0',
                            opacity: currentStep >= item.step ? 1 : 0.6
                        }}
                    >
                        <div style={{ textAlign: 'center' }}>
                            <div style={{
                                display: 'inline-block',
                                width: 24,
                                height: 24,
                                borderRadius: '50%',
                                backgroundColor: currentStep >= item.step ? '#1890ff' : '#d9d9d9',
                                color: 'white',
                                lineHeight: '24px',
                                marginBottom: 8
                            }}>
                                {item.step}
                            </div>
                            <div><strong>{item.title}</strong></div>
                            <div style={{ fontSize: 12, color: '#666' }}>{item.description}</div>
                        </div>
                    </Card>
                </Col>
            ))}
        </Row>
    );

    return (
        <div style={{ padding: 24 }}>
            <Spin spinning={loading} size="large">
                <StepIndicator />

                <Space direction="vertical" size="large" style={{ width: '100%' }}>
                    {/* 第一层：搜索面板 */}
                    <Card title="参数配置" bordered={false}>
                        <SearchPanel onSearch={handleSearch} />
                    </Card>

                    {/* 第二层：字段定义表格 */}
                    {fieldDefinitions.length > 0 && (
                        <Card
                            title="字段定义"
                            bordered={false}
                            style={{
                                display: currentStep >= 2 ? 'block' : 'none'
                            }}
                        >
                            <FieldDefinitionTable
                                dataSource={fieldDefinitions}
                                onSelect={handleFieldSelect}
                                initialSelected={selectedFields}
                            />
                        </Card>
                    )}

                    {/* 第三层：数据列表 */}
                    {listData.length > 0 && selectedFields.length > 0 && (
                        <Card
                            title="数据列表"
                            bordered={false}
                            style={{
                                display: currentStep >= 3 ? 'block' : 'none'
                            }}
                            extra={
                                <div>
                                    已选择字段: {selectedFields.join(', ')}
                                </div>
                            }
                        >
                            <DataListTable
                                dataSource={listData}
                                fieldsToShow={selectedFields}
                                onSelect={handleDataSelect}
                            />
                        </Card>
                    )}

                    {/* 底部信息展示 */}
                    {Object.keys(selectedData).length > 2 && (
                        <Card title="提交数据预览" size="small" bordered>
              <pre style={{
                  backgroundColor: '#f6f8fa',
                  padding: 12,
                  borderRadius: 4,
                  maxHeight: 200,
                  overflow: 'auto'
              }}>
                {JSON.stringify(selectedData, null, 2)}
              </pre>
                        </Card>
                    )}
                </Space>
            </Spin>
        </div>
    );
};

export default ThreeLayerPage;