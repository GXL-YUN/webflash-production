import React, {useState, useEffect, useCallback} from 'react';
import {Table, Button, Modal, Checkbox, message, Form, Drawer} from 'antd';


import type { DrawerProps, RadioChangeEvent } from 'antd';
import axios from 'axios';
import * as XLSX from 'xlsx';  //导出
import { useNavigate, Link, NavLink } from 'react-router-dom';
import RoomForm from '../from/RoomForm';   //新增表单
import {roomService} from "../../../components/TableGlobbing/services/roomService";
import {RoomItem,columns} from './list'

// 分页信息接口
interface PageInfo {
    sort: {
        sorted: boolean;
        unsorted: boolean;
        empty: boolean;
    };
    offset: number;
    limit: number;
    total: number;
    size: number;
    pages: number;
    current: number;
    searchCount: boolean;
    records: RoomItem[];
}

// 完整的接口响应接口
interface ApiResponse {
    code: number;           // 对应原来的 status
    msg: string;            // 对应原来的 msg
    data: PageInfo;         // 对应原来的 data
    success: boolean;       // 新增字段
}

// 过滤
interface FilterState {
    key: string;
    value: string;
}

const App: React.FC = () => {
    const navigate = useNavigate();
    const [data, setData] = useState<RoomItem[]>([]);
    const [loading, setLoading] = useState(false);
    const [total, setTotal] = useState(0);
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [filters, setFilters] = useState<FilterState[]>([]);
    const [localPageSize, setLocalPageSize] = useState(10);
    const [flage, setFlage] = useState(false);
    const url = "/date/api/project/";

    const [modalVisible, setModalVisible] = useState(false);
    const [modalType, setModalType] = useState<'create' | 'edit'>('create');
    const [form] = Form.useForm();
    const [editopen, setEditOpen] = useState(false);
    const [selectedRow, setSelectedRow] = useState<RoomItem | null>(null);
    const [placement, setPlacement] = useState<DrawerProps['placement']>('right');

    // 模拟的流程状态选项
    const processStatusOptions = [
        { label: '待前处理', value: '7' },
        { label: '前处理', value: '8' },
        { label: '待FIB', value: '9' },
        { label: 'FIB', value: '10' },
        { label: '待TEM', value: '11' },
        { label: 'TEM', value: '12' },
    ];
    type StatusItem = {
        label: string;
        value: string;
    };
    const statusList: StatusItem[] = [
        { label: '接样中', value: '0' },
        { label: 'sop编制', value: '1' },
        { label: 'topview', value: '2' },
    ];
    const fd_type = [
        { label: 'A+', value: 'A+' },
        { label: 'B', value: 'B' },
    ];
    const fd_lable = [
        { label: '正常', value: '0' },
        { label: '返工', value: '1' }
    ];
    // 表单提交
    const handleSubmit =  useCallback(async (values: any) => {
        debugger
        try {
            if (modalType === 'create') {
                await roomService.createRoom(url,values);
                setEditOpen(false);
                message.success('新增成功');
                fetchData();
            } else {
                // 编辑逻辑
                // await roomService.updateRoom({ ...values, id: selectedRow?.id });
                message.success('更新成功');
                fetchData();
            }
            setModalVisible(false);
            //fetchData();
        } catch (error) {
            message.error('操作失败');
        }
    },[form]);
    // 获取数据
    const fetchData = async (page: number = current, size: number = localPageSize, params: FilterState[] = filters) => {
        setLoading(true);
        try {
            const response = await axios.post<ApiResponse>(
                url + "list",
                {
                    size,
                    current: page ,
                    parem: params,
                }
            );
            if (response.data.success === true) {
                const responseData = response.data.data;
                console.log('接口返回:', {
                    total: responseData.total,
                    current: responseData.current,
                    pageSize: responseData.size,
                    dataLength: responseData.records?.length || 0,
                    records: responseData.records
                });

                // 使用 records 而不是 list
                setData(responseData.records || []);
                setTotal(responseData.total || 0);
                setCurrent(responseData.current || page);
                setPageSize(responseData.size || size);
                setLocalPageSize(responseData.size || size);
            } else {
                message.error('获取数据失败: ' + response.data.msg);
            }
        } catch (error) {
            console.error('Error fetching data:', error);
            message.error('网络请求失败');
        } finally {
            setLoading(false);
        }
    };
    const changView = () => {
        setFlage(!flage);
    };

    // 处理筛选变化
    const handleFilterChange = (filterName: string, checkedValues: string[]) => {
        debugger;
        console.log('筛选器名称:', filterName, '选中的值:', checkedValues);

        const newFilters = checkedValues.map(val => ({ key: filterName, value: val }));

        // 保留其他类型的筛选条件
        const existingOtherFilters = filters.filter(item => item.key !== filterName);
        const combinedFilters = [...existingOtherFilters, ...newFilters];

        setFilters(combinedFilters);
        setCurrent(1);
        fetchData(1, localPageSize, combinedFilters);
    };

    // 处理分页变化
    const handleTableChange = (pagination: any) => {
        console.log('分页变化:', {
            current: pagination.current,
            pageSize: pagination.pageSize
        });

        if (pagination.pageSize && pagination.pageSize !== localPageSize) {
            setLocalPageSize(pagination.pageSize);
            setCurrent(1);
            fetchData(1, pagination.pageSize, filters);
        } else {
            setCurrent(pagination.current);
            fetchData(pagination.current, localPageSize, filters);
        }
    };

    const handleAdd = () => {
        form.resetFields();
        setModalType('create');
        setSelectedRow(null);
        setModalVisible(true);
        setEditOpen(true);
    };

    //加载数据初始化
    useEffect(() => {
        fetchData();
    }, []);

    return (
        <div style={{ padding: '20px', backgroundColor: '#f0f2f5', minHeight: '100vh' }}>
            {/* 顶部筛选区 */}
            <div style={{ marginBottom: '20px', backgroundColor: '#fff', padding: '15px', borderRadius: '8px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '15px' }}>
                    <h3 style={{ margin: 0 }}>筛选</h3>
                    {/*<Button type="primary" onClick={handleExport}>导出数据</Button>*/}
                    <Button type="primary" onClick={handleAdd}>新增</Button>
                </div>

                {/* 筛选区域 */}
                <div style={{ marginBottom: '15px' }}>
                    {/* 其他隐藏筛选数据 */}
                    {flage && (
                        <div style={{
                            marginTop: '15px',
                            padding: '10px',
                            backgroundColor: '#f5f5f5',
                            borderRadius: '4px'
                        }}>
                            <div style={{marginBottom: '10px'}}>
                                <div style={{fontWeight: 'bold', marginBottom: '8px'}}>站点状态：</div>
                                <Checkbox.Group
                                    name="fdname1"
                                    options={processStatusOptions.map(opt => ({label: opt.label, value: opt.value}))}
                                    onChange={(values) => handleFilterChange("fdname1", values as string[])}
                                />
                            </div>

                            <div style={{marginBottom: '10px'}}>
                                <div style={{fontWeight: 'bold', marginBottom: '8px'}}>单据状态：</div>
                                <Checkbox.Group
                                    name="fdname2"
                                    options={fd_lable.map(opt => ({label: opt.label, value: opt.value}))}
                                    onChange={(values) => handleFilterChange("fdname2", values as string[])}
                                />
                            </div>
                            <div style={{marginBottom: '10px'}}>
                                <div style={{fontWeight: 'bold', marginBottom: '8px'}}>优先级：</div>
                                <Checkbox.Group
                                    name="fdname"
                                    options={fd_type.map(opt => ({label: opt.label, value: opt.value}))}
                                    onChange={(values) => handleFilterChange("fdname", values as string[])}
                                />
                            </div>
                        </div>
                    )}
                </div>

                {/* 其他筛选条件按钮 */}
                <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                    <Button
                        style={{fontWeight: 'bold', float: 'right'}}
                        type='primary'
                        size="small"
                        onClick={changView}
                    >
                        {flage ? '收起其他筛选' : '展开其他筛选'}
                    </Button>
                </div>
            </div>
            {/* 新增/编辑弹窗 */}
            {/*<Modal*/}
            {/*    title={modalType === 'create' ? '新增房间' : '编辑房间'}*/}
            {/*    open={modalVisible}*/}
            {/*    onOk={() => form.submit()}*/}
            {/*    onCancel={() => setModalVisible(false)}*/}
            {/*    width={800}*/}
            {/*    destroyOnClose*/}
            {/*>*/}
            {/*    <RoomForm*/}
            {/*        form={form}*/}
            {/*        onSubmit={handleSubmit}*/}
            {/*        initialValues={selectedRow || {}}*/}
            {/*    />*/}
            {/*</Modal>*/}
            <Drawer
                title="列表详情数据"
                placement={placement}
                onClose={() => setEditOpen(false)}
                open={editopen}
                width={1000}
            >
                <RoomForm
                    form={form}
                    onSubmit={handleSubmit}
                    initialValues={selectedRow || {}}
                />
            </Drawer>


            {/* 列表区 */}
            <div style={{backgroundColor: '#fff', padding: '15px', borderRadius: '8px'}}>
                <Table
                    dataSource={data}
                    columns={columns}
                    rowKey="FD_MAIM_ID"
                    loading={loading}
                    pagination={{
                        current,
                        pageSize: localPageSize,
                        total,
                        showSizeChanger: true,
                        showQuickJumper: true,
                        showTotal: (total) => `共 ${total} 条`,
                        pageSizeOptions: ['10', '20', '50', '100', '1000'],
                    }}
                    onRow={(record) => {
                        return {
                            onClick: (event) => {navigate("/ProjectDashboard/"+record.fdId); console.info('点击成功:',record)}, // 点击行
                        };
                    }}
                    onChange={handleTableChange}
                    scroll={{ x: 'max-content' }}
                    bordered
                    locale={{
                        emptyText: '暂无数据'
                    }}
                />
            </div>
        </div>
    );
};

export default App;