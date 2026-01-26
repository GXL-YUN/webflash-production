// components/RoomForm.tsx
import React from 'react';
import { Form, Input, Row, Col, Select, Upload } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

const { TextArea } = Input;
const { Option } = Select;

interface RoomFormProps {
    form: any;
    onSubmit: (values: any) => void;
    initialValues?: any;
}
const RoomForm: React.FC<RoomFormProps> = ({ form, onSubmit, initialValues }) => {
    const normFile = (e: any) => {
        if (Array.isArray(e)) {
            return e;
        }
        return e?.fileList;
    };

    return (
        <Form
            form={form}
            layout="vertical"
            onFinish={onSubmit}
            initialValues={initialValues}
        >
            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="名称"
                        name="fdName"
                        rules={[{ required: true, message: '请输入名称' }]}
                    >
                        <Input placeholder="请输入名称" />
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="房间名称"
                        name="fdRoomName"
                        rules={[{ required: true, message: '请输入房间名称' }]}
                    >
                        <Input placeholder="请输入房间名称" />
                    </Form.Item>
                </Col>
            </Row>

            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="房间电话"
                        name="fdRoomPhone"
                        rules={[
                            { required: true, message: '请输入房间电话' },
                            { pattern: /^1\d{10}$/, message: '请输入正确的手机号' }
                        ]}
                    >
                        <Input placeholder="请输入房间电话" />
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="负责人电话"
                        name="fdPrincipalPhone"
                        rules={[{ pattern: /^1\d{10}$/, message: '请输入正确的手机号' }]}
                    >
                        <Input placeholder="请输入负责人电话" />
                    </Form.Item>
                </Col>
            </Row>

            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="地址"
                        name="fdAddres"
                        rules={[{ required: true, message: '请输入地址' }]}
                    >
                        <Input placeholder="请输入地址" />
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="负责人"
                        name="fdPrincipal"
                        rules={[{ required: true, message: '请输入负责人' }]}
                    >
                        <Input placeholder="请输入负责人" />
                    </Form.Item>
                </Col>
            </Row>

            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="是否整体出租"
                        name="fdIsWhole"
                    >
                        <Select placeholder="请选择">
                            <Option value="是">是</Option>
                            <Option value="否">否</Option>
                        </Select>
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="ABCDE分类"
                        name="fdAbcde"
                    >
                        <Select placeholder="请选择分类">
                            <Option value="A">A类</Option>
                            <Option value="B">B类</Option>
                            <Option value="C">C类</Option>
                            <Option value="D">D类</Option>
                            <Option value="E">E类</Option>
                        </Select>
                    </Form.Item>
                </Col>
            </Row>

            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        label="租赁状态"
                        name="fdlease"
                    >
                        <Select placeholder="请选择状态">
                            <Option value="在租">在租</Option>
                            <Option value="空置">空置</Option>
                            <Option value="待租">待租</Option>
                            <Option value="装修中">装修中</Option>
                        </Select>
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        label="上传图片"
                        name="img"
                        valuePropName="fileList"
                        getValueFromEvent={normFile}
                    >
                        <Upload
                            listType="picture-card"
                            maxCount={1}
                            beforeUpload={() => false}
                        >
                            <div>
                                <PlusOutlined />
                                <div style={{ marginTop: 8 }}>上传</div>
                            </div>
                        </Upload>
                    </Form.Item>
                </Col>
            </Row>

            <Form.Item
                label="备注"
                name="fdbz"
            >
                <TextArea
                    rows={4}
                    placeholder="请输入备注信息"
                    maxLength={500}
                    showCount
                />
            </Form.Item>
        </Form>
    );
};

export default RoomForm;