// components/DemandManagement/ImportExport.tsx
import React, { useState } from 'react';
import { Modal, Tabs, Upload, Button, Radio, Checkbox, Form, Input, message } from 'antd';
import { UploadOutlined, DownloadOutlined } from '@ant-design/icons';
import * as XLSX from 'xlsx';

const { TabPane } = Tabs;
const { TextArea } = Input;
interface ImportExportProps {
    type: 'import' | 'export';
    visible: boolean;
    onClose: () => void;
    selectedIds?: string[];
    onImport?: (data: any) => void;
    onExport?: (data: any) => void;
}

const ImportExport: React.FC<ImportExportProps> = ({
                                                       type,
                                                       visible,
                                                       onClose,
                                                       selectedIds = [],
                                                       onImport,
                                                       onExport,
                                                   }) => {
    const [activeTab, setActiveTab] = useState(type === 'import' ? 'file' : 'format');
    const [exportFormat, setExportFormat] = useState('excel');
    const [selectedFields, setSelectedFields] = useState<string[]>([
        'id',
        'name',
        'description',
        'status',
        'createdAt',
    ]);

    const fieldOptions = [
        { label: '需求编号', value: 'id' },
        { label: '需求名称', value: 'name' },
        { label: '需求描述', value: 'description' },
        { label: '业务价值', value: 'businessValue' },
        { label: '状态', value: 'status' },
        { label: '优先级', value: 'priority' },
        { label: '创建人', value: 'creator' },
        { label: '创建时间', value: 'createdAt' },
        { label: '开发时间', value: 'developTime' },
        { label: '测试时间', value: 'testTime' },
        { label: '上线时间', value: 'onlineAt' },
    ];

    const handleFileUpload = (file: File) => {
        const reader = new FileReader();
        reader.onload = (e) => {
            try {
                const data = e.target?.result;
                const workbook = XLSX.read(data, { type: 'binary' });
                const sheetName = workbook.SheetNames[0];
                const worksheet = workbook.Sheets[sheetName];
                const jsonData = XLSX.utils.sheet_to_json(worksheet);

                onImport?.(jsonData);
                message.success(`成功导入 ${jsonData.length} 条数据`);
            } catch (error) {
                message.error('文件解析失败，请检查格式');
            }
        };
        reader.readAsBinaryString(file);
        return false; // 阻止默认上传行为
    };

    const handleExport = () => {
        // 这里模拟导出逻辑
        const exportData = {
            format: exportFormat,
            fields: selectedFields,
            selectedIds,
        };
        onExport?.(exportData);

        if (exportFormat === 'excel') {
            const ws = XLSX.utils.json_to_sheet([{ 需求编号: 'REQ-001', 需求名称: '示例需求' }]);
            const wb = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(wb, ws, '需求列表');
            XLSX.writeFile(wb, '需求列表.xlsx');
        }
    };

    return (
        <Modal
            title={type === 'import' ? '导入需求' : '导出需求'}
            open={visible}
            onCancel={onClose}
            footer={[
                <Button key="cancel" onClick={onClose}>
                    取消
                </Button>,
                <Button
                    key="confirm"
                    type="primary"
                    onClick={type === 'import' ? undefined : handleExport}
                >
                    {type === 'import' ? '导入' : '导出'}
                </Button>,
            ]}
            width={600}
        >
            {type === 'import' ? (
                <Tabs activeKey={activeTab} onChange={setActiveTab}>
                    <TabPane tab="文件导入" key="file">
                        <div style={{ textAlign: 'center', padding: '40px 0' }}>
                            <Upload
                                accept=".xlsx,.xls,.csv"
                                beforeUpload={handleFileUpload}
                                showUploadList={false}
                            >
                                <Button icon={<UploadOutlined />} type="primary" size="large">
                                    选择文件
                                </Button>
                            </Upload>
                            <div style={{ marginTop: 16, color: '#999' }}>
                                支持 .xlsx, .xls, .csv 格式，文件大小不超过 10MB
                            </div>
                            <div style={{ marginTop: 8 }}>
                                <a href="/templates/demand-template.xlsx" download>
                                    下载导入模板
                                </a>
                            </div>
                        </div>
                    </TabPane>
                    <TabPane tab="JSON导入" key="json">
                        <Form layout="vertical">
                            <Form.Item label="JSON数据">
                                <TextArea
                                    rows={10}
                                    placeholder='请输入JSON格式的需求数据，例如：[{"id": "REQ-001", "name": "需求名称"}]'
                                />
                            </Form.Item>
                        </Form>
                    </TabPane>
                </Tabs>
            ) : (
                <div>
                    <Form layout="vertical">
                        <Form.Item label="导出格式">
                            <Radio.Group
                                value={exportFormat}
                                onChange={e => setExportFormat(e.target.value)}
                            >
                                <Radio value="excel">Excel</Radio>
                                <Radio value="csv">CSV</Radio>
                                <Radio value="json">JSON</Radio>
                            </Radio.Group>
                        </Form.Item>

                        <Form.Item label="选择字段">
                            <Checkbox.Group
                                options={fieldOptions}
                                value={selectedFields}
                                onChange={setSelectedFields as any}
                                style={{ display: 'flex', flexDirection: 'column' }}
                            />
                        </Form.Item>

                        <Form.Item label="数据范围">
                            <Radio.Group defaultValue="selected">
                                <Radio value="selected">仅导出选中项 ({selectedIds.length} 项)</Radio>
                                <Radio value="all">导出全部数据</Radio>
                                <Radio value="filtered">导出当前筛选结果</Radio>
                            </Radio.Group>
                        </Form.Item>
                    </Form>
                </div>
            )}
        </Modal>
    );
};

export default ImportExport;