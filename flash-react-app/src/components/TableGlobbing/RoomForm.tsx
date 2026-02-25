// components/RoomForm.tsx
import React, {useState} from 'react';
import { Form, Input, Row, Col, Select, Upload, Button, DatePicker } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import CodeHighlighter from "../../operation/Knowledge/components/CodeHighlighter";
import MarkdownEditor from "../../operation/Knowledge/components/MarkdownEditor";

import FileUpdate  from "../File/components/FileUploadDisplay"

import FileUploadWithAction from "../../components/File/components/action/FileUploadWithAction"

const { TextArea } = Input;
const { Option } = Select;

interface RoomFormProps {
    form: any;
    onSubmit: (values: any) => void;
    initialValues?: any;
}

const RoomForm: React.FC<RoomFormProps> = ({ form, onSubmit, initialValues }) => {
    // 直接使用，DatePicker 返回的就是时间戳
    const [activeTab, setActiveTab] = useState<'markdown' | 'code'>('markdown');


    const handleSubmit = (values: any) => {
        const submitData = {
            ...values,
            // 确保转换为时间戳（毫秒）
            fdActoinTime: values.fdActoinTime ? values.fdActoinTime.valueOf() : null,
            fdEndTime: values.fdEndTime ? values.fdEndTime.valueOf() : null
        };
        // values 中的 fdActoinTime 和 fdEndTime 已经是时间戳格式
        onSubmit(submitData);
    };

    return (
        <Form
            form={form}
            layout="vertical"
            onFinish={handleSubmit}
            initialValues={initialValues}
        >
            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="项目名称"
                        name="fdName"
                        rules={[{ required: true, message: '请输入名称' }]}
                    >
                        <Input placeholder="请输入名称" />
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="项目状态"
                        name="fdType"
                        rules={[{ required: true, message: '请选择' }]}
                    >
                        <Select placeholder="请选择">
                            <Option value="是">是</Option>
                            <Option value="否">否</Option>
                        </Select>
                    </Form.Item>
                </Col>
            </Row>
            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="需求描述"
                        name="fdMassage"
                        rules={[{ required: true, message: '请输入需求描述' }]}
                    >
                        <Input placeholder="请输入需求描述" />
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="需求负责人"
                        name="fdUserName"
                        rules={[{ required: true, message: '请输入需求负责人' }]}>
                        <Input placeholder="请输入需求负责人" />
                    </Form.Item>
                </Col>
            </Row>
            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="项目开始时间"
                        name="fdActoinTime"
                        rules={[{ required: true, message: '请选择开始时间' }]}
                    >
                        <DatePicker
                            showTime
                            format="YYYY-MM-DD HH:mm:ss"
                            style={{ width: '100%' }}
                            placeholder="请选择开始时间"
                        />
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="项目结束时间"
                        name="fdEndTime"
                        rules={[{ required: true, message: '请选择结束时间' }]}
                    >
                        <DatePicker
                            showTime
                            format="YYYY-MM-DD HH:mm:ss"
                            style={{ width: '100%' }}
                            placeholder="请选择结束时间"
                        />
                    </Form.Item>
                </Col>
            </Row>


            {activeTab === 'markdown' ? (
                <Form.Item
                    name="richTextContent_long"
                    label="内容"
                    rules={[{ required: true, message: '请输入内容' }]}
                >
                    <MarkdownEditor />
                </Form.Item>
            ) : (
                <>

                    <Form.Item
                        name="richTextContent_long"
                        label="说明"
                        rules={[{ required: true, message: '请输入说明' }]}
                    >
                        <CodeHighlighter language='markdown'/>
                    </Form.Item>
                </>
            )}
                {/* <Form.Item name="attachments">
                    <FileUploadWithAction />
                </Form.Item> */}


                <Form.Item
                    name="attacherList"
                    label="附件"
                >
                    <FileUploadWithAction/>
                </Form.Item>

            {/*<Form.Item*/}
            {/*    name="richTextContent_long"*/}
            {/*    label="代码"*/}
            {/*    rules={[{ required: true, message: '需求说明' }]}*/}
            {/*>*/}
            {/*    <CodeHighlighter language= 'markdown' />*/}
            {/*</Form.Item>*/}

            <Form.Item
                label="需求说明"
                name="fdBz"
            >
                <TextArea
                    rows={4}
                    placeholder="请输入需求说明"
                    maxLength={500}
                    showCount
                />
            </Form.Item>
            <Form.Item label={null}>
                <div>
                    <Button type="primary" htmlType="submit">
                        提交
                    </Button>
                </div>
            </Form.Item>
        </Form>
    );
};

export default RoomForm;