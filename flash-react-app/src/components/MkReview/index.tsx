import React, { useState } from 'react';
import { Layout, Spin, message, Alert, Row, Col, Space, Typography } from 'antd';
import ApiConfigPanel from './ApiConfigPanel';
import FieldSelectorPanel from './FieldSelectorPanel';
import DataDisplayPanel from './DataDisplayPanel';
import { FieldConfig, SelectedDataItem } from './types';
import '../..//App.css';

const { Header, Content, Footer } = Layout;
const { Title, Text } = Typography;

const App: React.FC = () => {
    const [apiData, setApiData] = useState<any>(null);
    const [fields, setFields] = useState<FieldConfig[]>([]);
    const [selectedItems, setSelectedItems] = useState<SelectedDataItem[]>([]);
    const [displayData, setDisplayData] = useState<any[]>([]);
    const [loading, setLoading] = useState({
        api: false,
        fields: false,
        data: false
    });

    // 处理API响应
    const handleApiResponse = (data: any, responseFields: FieldConfig[]) => {
        setApiData(data);
        setFields(responseFields);
        setSelectedItems([]);
        setDisplayData(data?.data || []);
        message.success(`获取到 ${responseFields.length} 个字段`);
    };

    // 处理字段选择变化
    const handleSelectionChange = (selected: SelectedDataItem[]) => {
        setSelectedItems(selected);
    };

    // 生成数据
    const handleGenerateData = (selectedFields: string[]) => {
        setLoading(prev => ({ ...prev, data: true }));

        // 模拟数据生成延迟
        setTimeout(() => {
            if (apiData?.data) {
                // 过滤数据，只显示选中的字段
                const filteredData = apiData.data.map((item: any) => {
                    const filteredItem: any = {};
                    selectedFields.forEach(field => {
                        if (item[field] !== undefined) {
                            filteredItem[field] = item[field];
                        }
                    });
                    return filteredItem;
                });

                setDisplayData(filteredData);

                // 构建新的接口请求体
                const requestBody = {
                    fields: selectedFields,
                    originalData: apiData.data,
                    timestamp: new Date().toISOString()
                };

                // 这里可以发送新的接口请求
                console.log('重新封装的接口请求:', requestBody);

                message.success(`生成 ${filteredData.length} 条数据，使用 ${selectedFields.length} 个字段`);
            }

            setLoading(prev => ({ ...prev, data: false }));
        }, 500);
    };

    return (
        <Layout className="app-layout">
            <Header className="app-header">
                <Title level={3} style={{ color: 'white', margin: 0 }}>
                    接口数据生成器
                </Title>
                <Text style={{ color: 'rgba(255,255,255,0.8)' }}>
                    配置接口 → 选择字段 → 生成数据
                </Text>
            </Header>

            <Content className="app-content">
                <Spin spinning={loading.api || loading.data} size="large">
                    <Space direction="vertical" size="large" style={{ width: '100%' }}>
                        {/* 步骤指示器 */}
                        <Row gutter={16} className="steps-indicator">
                            <Col span={8} className="step active">
                                <div className="step-number">1</div>
                                <div className="step-title">配置接口</div>
                                <div className="step-desc">设置API参数并发送请求</div>
                            </Col>
                            <Col span={8} className={`step ${fields.length > 0 ? 'active' : ''}`}>
                                <div className="step-number">2</div>
                                <div className="step-title">选择字段</div>
                                <div className="step-desc">从返回数据中选择需要展示的字段</div>
                            </Col>
                            <Col span={8} className={`step ${selectedItems.length > 0 ? 'active' : ''}`}>
                                <div className="step-number">3</div>
                                <div className="step-title">生成数据</div>
                                <div className="step-desc">根据选择生成新的数据展示</div>
                            </Col>
                        </Row>

                        {/* API配置面板 */}
                        <ApiConfigPanel
                            onApiResponse={handleApiResponse}
                            loading={loading.api}
                            setLoading={(isLoading) => setLoading(prev => ({ ...prev, api: isLoading }))}
                        />

                        {/* 提示信息 */}
                        {apiData && (
                            <Alert
                                message="接口请求成功"
                                description={`获取到 ${apiData.data?.length || 0} 条数据，${fields.length} 个字段`}
                                type="success"
                                showIcon
                                closable
                            />
                        )}

                        {/* 字段选择面板 */}
                        {fields.length > 0 && (
                            <FieldSelectorPanel
                                fields={fields}
                                onSelectionChange={handleSelectionChange}
                                onDataGenerate={handleGenerateData}
                                loading={loading.data}
                            />
                        )}

                        {/* 数据展示面板 */}
                        {(displayData.length > 0 || apiData) && (
                            <DataDisplayPanel
                                data={displayData}
                                fields={fields}
                                selectedItems={selectedItems.filter(item => item.selected)}
                                loading={loading.data}
                                onDataTransform={(transformedData) => {
                                    setDisplayData(transformedData);
                                    message.info('数据已转换');
                                }}
                            />
                        )}

                        {/* 使用提示 */}
                        {!apiData && (
                            <Alert
                                message="使用说明"
                                description={
                                    <ol>
                                        <li>在上方配置接口参数并点击"发送请求"</li>
                                        <li>接口返回数据后，在中部选择需要展示的字段</li>
                                        <li>点击"生成数据"按钮，系统会根据选择的字段重新封装数据</li>
                                        <li>在底部查看和操作生成的数据</li>
                                    </ol>
                                }
                                type="info"
                                showIcon
                            />
                        )}
                    </Space>
                </Spin>
            </Content>

            <Footer className="app-footer">
                接口数据生成器 © {new Date().getFullYear()} - 基于 React + Ant Design + TypeScript
            </Footer>
        </Layout>
    );
};

export default App;