// components/RoomTable.tsx
import React, {useState, useEffect, useRef, useCallback} from 'react';
import {RoomItem} from "./types/room";
import { BrowserRouter as Router, Routes, Route,Navigate } from 'react-router-dom';
import RoomTable ,{ChildMethods}  from './RoomTable';
import RoomForm from './RoomForm';
import {
    Table,
    Button,
    Space,
    Modal,
    Form,
    Input,
    message,
    Popconfirm,
    Card,
    Descriptions,
    Dropdown,
    Menu
} from 'antd';

import {DeleteOutlined, EditOutlined, EyeOutlined} from "@ant-design/icons";
import {roomService} from "./services/roomService";
const TableGlobbings: React.FC = () => {



    const [data, setData] = useState<RoomItem[]>([]);
    const [loading, setLoading] = useState(false);
    const [selectedRow, setSelectedRow] = useState<RoomItem | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [detailVisible, setDetailVisible] = useState(false);
    const [form] = Form.useForm();
    const [modalType, setModalType] = useState<'create' | 'edit'>('create');
    const childRef = useRef<ChildMethods>(null);
    // 表单提交
    const handleSubmit =  useCallback(async (values: any) => {
        try {
            if (modalType === 'create') {
                await roomService.createRoom(values);
                message.success('新增成功');
            } else {
                // 编辑逻辑
                // await roomService.updateRoom({ ...values, id: selectedRow?.id });
                message.success('更新成功');
            }
            setModalVisible(false);

            //调用子组件
            if (childRef.current) {
                const data = childRef.current.fetchData();
                //setChildData(data);
                console.log('刷新数据:', 0);
            }
            //fetchData();
        } catch (error) {
            message.error('操作失败');
        }
    },[form]);


    // 使用一个对象来管理所有方法
    const methods = {
        // 新增记录
        addDate: () => {
                form.resetFields();
                setModalType('create');
                setSelectedRow(null);
                setModalVisible(true);
                // 可以返回结果给子组件
                //return { success: true, receivedData: data };
        },
        submitForm: () => {
            console.log('提交表单:');
            // 这里可以发送到API
        },
        fetchData: () => {
            console.log('刷新表格数据');
            // 实现刷新数据的逻辑
            // 例如：重新调用API获取数据
        },
    };
    // 查看详情
    const handleViewDetail = (record: RoomItem) => {
        setSelectedRow(record);
        setDetailVisible(true);
    };

    // 编辑记录
    const handleEdit = (record: RoomItem) => {
        form.setFieldsValue(record);
        setModalType('edit');
        setSelectedRow(record);
        setModalVisible(true);
    };

    // 删除记录
    const handleDelete = async (id: string) => {
        try {
            // 这里需要根据实际接口实现删除逻辑
            // const result = await roomService.deleteRoom(id);
            message.success('删除成功');
           // childRef.current.fetchData();
        } catch (error) {
            message.error('删除失败');
        }
    };
    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            width: 80,
            //sorter: (a: RoomItem, b: RoomItem) => a.id - b.id
        },
        {
            title: '需求名称',
            dataIndex: 'fdName',
            key: 'fdName',
            width: 150
        },
        {
            title: '需求描述',
            dataIndex: 'fdMassage',
            key: 'fdMassage',
            width: 150
        },
        {
            title: '需求说明',
            dataIndex: 'fdBz',
            key: 'fdBz',
            width: 200,
            ellipsis: true
        },
        {
            title: '项目状态',
            dataIndex: 'fdType',
            key: 'fdType',
            width: 120
        },

        {
            title: '创建时间',
            dataIndex: 'createTime',
            key: 'createTime',
            width: 180
        },
        {
            title: '项目对接人',
            dataIndex: 'fdUserName',
            key: 'fdUserName',
            width: 200,
            ellipsis: true
        },
        {
            title: '操作',
            key: 'action',
            width: 150,
            fixed: 'right' as const,
            render: (_: any, record: RoomItem) => (
                <Space size="small">
                    <Button
                        type="link"
                        icon={<EyeOutlined />}
                         onClick={() => handleViewDetail(record)}
                        size="small"
                    >
                        查看
                    </Button>
                    <Button
                        type="link"
                        icon={<EditOutlined />}
                         onClick={() => handleEdit(record)}
                        size="small"
                    >
                        编辑
                    </Button>
                    <Popconfirm
                        title="确定要删除吗？"
                        onConfirm={() => handleDelete(record.id)}
                        okText="确定"
                        cancelText="取消"
                    >
                        <Button
                            type="link"
                            danger
                            icon={<DeleteOutlined />}
                            size="small"
                        >
                            删除
                        </Button>
                    </Popconfirm>
                </Space>
            )
        }
    ];
    return <div><RoomTable col={columns} url="/api/project/" ref={methods} />
        {/* 新增/编辑弹窗 */}
        <Modal
            title={modalType === 'create' ? '新增房间' : '编辑房间'}
            open={modalVisible}
            onOk={() => form.submit()}
            onCancel={() => setModalVisible(false)}
            width={800}
            destroyOnClose
        >
            <RoomForm
                form={form}
                onSubmit={handleSubmit}
                initialValues={selectedRow || {}}
            />
        </Modal>

        {/* 详情弹窗 */}
        <Modal
            title="详情"
            open={detailVisible}
            onCancel={() => setDetailVisible(false)}
            footer={[
                <Button key="close" onClick={() => setDetailVisible(false)}>
                    关闭
                </Button>
            ]}
            width={600}
        >
            {selectedRow && (
                <Descriptions column={2} bordered>
                    <Descriptions.Item label="ID">{selectedRow.id}</Descriptions.Item>
                    <Descriptions.Item label="需求名称">{selectedRow.fdName}</Descriptions.Item>
                    <Descriptions.Item label="需求描述">{selectedRow.fdMassage}</Descriptions.Item>
                    <Descriptions.Item label="项目状态">{selectedRow.fdType}</Descriptions.Item>
                    <Descriptions.Item label="负责人">{selectedRow.fdUserName}</Descriptions.Item>
                    <Descriptions.Item label="创建时间">{selectedRow.createTime}</Descriptions.Item>
                    <Descriptions.Item label="更新时间">{selectedRow.modifyTime}</Descriptions.Item>
                    <Descriptions.Item label="备注" span={2}>
                        {selectedRow.fdbz}
                    </Descriptions.Item>
                    {selectedRow.img && (
                        <Descriptions.Item label="图片" span={2}>
                            <img
                                src={selectedRow.img}
                                alt="房间图片"
                                style={{ maxWidth: '100%', maxHeight: 200 }}
                            />
                        </Descriptions.Item>
                    )}
                </Descriptions>
            )}
        </Modal>
    </div>
};
export default TableGlobbings;