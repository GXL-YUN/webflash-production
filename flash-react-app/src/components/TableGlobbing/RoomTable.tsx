// components/RoomTable.tsx
import React, { useState, useEffect,useImperativeHandle } from 'react';
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
import {
    PlusOutlined,
    EyeOutlined,
    EditOutlined,
    DeleteOutlined,
    DownloadOutlined,
    MoreOutlined
} from '@ant-design/icons';
import { roomService } from './services/roomService';
import {ColDate,RoomItem } from './types/room';
import RoomForm from './RoomForm';


interface DataFormProps {
    col?: any;
    url: string;
    ref:ChildMethods;

}
export interface ChildMethods {
    addDate: () => void;
    submitForm:()=>void;
    fetchData:()=>void;
}


interface ChildProps {
    onCallParentMethod: (message: string) => void;
    onComplexMethod: (data: { type: string; value: any }) => string;
}
// @ts-ignore
const RoomTable: React.FC<DataFormProps> = (props) => {

    const [data, setData] = useState<RoomItem[]>([]);
    const [loading, setLoading] = useState(false);
    const [selectedRow, setSelectedRow] = useState<RoomItem | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [detailVisible, setDetailVisible] = useState(false);
    const [form] = Form.useForm();
    const [modalType, setModalType] = useState<'create' | 'edit'>('create');

    // 获取数据
    const fetchData = async () => {
        setLoading(true);
        try {
            const result = await roomService.getRoomList(props.url);
            if (result.code === 20000) {
                setData(result.data);
            } else {
                message.error(result.msg || '获取数据失败');
            }
        } catch (error) {
            message.error('请求失败，请检查网络连接');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    // 新增记录
    const handleCreate = () => {
        debugger
        props.ref.addDate();
        //调用父组件函数
    };

    // 暴露给父组件的方法
    // useImperativeHandle(ref, () => ({
    //     // 刷新数据
    //     refreshData: () => {
    //         fetchData();
    //     },
    //     // 自定义消息
    //     showMessage: (msg: string) => {
    //         alert(`来自父组件的消息: ${msg}`);
    //     }
    // }));
    // 导出菜单
    const exportMenu = (
        <Menu
            items={[
                {
                    key: 'csv',
                    label: '导出CSV',
                    onClick: () => roomService.exportToCSV(data)
                },
                {
                    key: 'excel',
                    label: '导出Excel',
                    onClick: () => roomService.exportToExcel(data)
                }
            ]}
        />
    );
    return (
        <div style={{ padding: 24 }}>
            <Card
                title="列表管理"
                extra={
                    <Space>
                        <Dropdown overlay={exportMenu} placement="bottomRight">
                            <Button icon={<DownloadOutlined />}>
                                导出
                            </Button>
                        </Dropdown>
                        <Button
                            type="primary"
                            icon={<PlusOutlined />}
                            onClick={handleCreate}
                        >添加
                        </Button>
                    </Space>
                }
            >
                <Table
                    columns={props.col}
                    dataSource={data}
                    rowKey="id"
                    loading={loading}
                    scroll={{ x: 1300 }}
                    pagination={{
                        showSizeChanger: true,
                        showQuickJumper: true,
                        showTotal: (total) => `共 ${total} 条`,
                        pageSizeOptions: ['10', '20', '50', '100']
                    }}
                />
            </Card>


        </div>
    );
};

export default RoomTable;