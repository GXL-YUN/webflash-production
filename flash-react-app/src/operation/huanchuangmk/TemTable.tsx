import React, { useState, useEffect } from 'react';
import { Table, Button, Checkbox, message } from 'antd';
import axios from 'axios';
import * as XLSX from 'xlsx';

// 定义接口类型
interface LimsData {
    FD_MAIM_ID: string;
    DOC_STATE: string;
    DOC_NAME: string;
    DOC_NUMBER: string;
    DOC_PRIORITY: string;
    DOC_SITE: string;
    DOC_PROJECT: string;
    DOC_UPDATETIME: string;
    DOC_AGING: string;
    FD_ID: string;
    FD_CREATOR_ID: string;
    // 表格列使用的字段
    RN?: number;
    FD_COL_VY6XBM?: string;
    FD_COL_6YI0L7?: string;
    FD_COL_3MM6EF?: number;
    FD_COL_1CBIHH?: number;
    FD_COL_1MRA3M?: string;
    FD_COL_VGKEFG?: string;
    FD_COL_ZMOHYP?: string;
    FD_COL_8MKYGI?: string;
    FD_COL_4QGWDA?: string;
}

interface ApiResponse {
    status: number;
    msg: string;
    data: {
        total: number;
        size: number;
        current: number;
        list: LimsData[];
    };
}

interface FilterState {
    key: string;
    value: string;
}

const App: React.FC = () => {
    const [data, setData] = useState<LimsData[]>([]);
    const [loading, setLoading] = useState(false);
    const [total, setTotal] = useState(0);
    const [current, setCurrent] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [filters, setFilters] = useState<FilterState[]>([]);
    const [localPageSize, setLocalPageSize] = useState(10);

    const [flage, setFlage] = useState(false);

    // 模拟的流程状态选项
    const processStatusOptions = [
        //  { label: '起草', value: '0' },
        // { label: '需求审批', value: '1' },
        // { label: '需求确认', value: '2' },
        // { label: '待接样', value: '3' },
        // { label: '待结果确认', value: '4' },
        // { label: '待样品取回', value: '5' },
        // { label: '已完结', value: '6' },
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
        { label: '前处理', value: '3' },
        { label: 'FIB', value: '4' },
        { label: '班组长审批', value: '5' },
        { label: 'TEM拍摄', value: '6' },
        { label: '二次质审', value: '7' },
        { label: '结案审批', value: '8' },
        { label: '已结案', value: '9' },
        { label: '领导审批', value: '20' },
        { label: '待topview', value: '12' },
        { label: '待前处理', value: '13' },
        { label: '待FIB', value: '14' },
        { label: '待TEM拍摄', value: '15' },
        { label: '交接审批', value: '16' },
        { label: '待sop编制', value: '17' },
        { label: '测试申请', value: '18' },
        { label: '返工等待', value: '25' },
        { label: '外诉已开单', value: '40' },
        { label: '委外中', value: '66' },
        { label: '废弃', value: '70' }
    ];


    const fd_type = [
        { label: 'A+', value: 'A+' },
        { label: 'B', value: 'B' },
        { label: 'C', value: 'C' },
        { label: 'D', value: 'D' },
        { label: 'F', value: 'F' }

    ];


    const fd_lable = [
        { label: '正常', value: '0' },
        { label: '返工', value: '1' }
    ];

    // 获取数据
    const fetchData = async (page: number = current, size: number = localPageSize, params: FilterState[] = filters) => {
        setLoading(true);
        try {
            const response = await axios.post<ApiResponse>(
                '/ekp_mkpass/back/lims/LimsTemListController/getAll',
                {
                    size,
                    current: page-1 ,
                    parem: params,
                }
            );


            debugger
            if (response.data.status === 0) {
                const responseData = response.data.data;
                console.log('接口返回:', {
                    total: responseData.total,
                    current: responseData.current,
                    pageSize: responseData.size,
                    dataLength: responseData.list?.length || 0,
                    list: responseData.list
                });

                setData(responseData.list || []);
                setTotal(responseData.total || 0);
                // 确保页码正确（接口返回的current可能从0开始或1开始）
                setCurrent(page);
                setPageSize(size);
                setLocalPageSize(size);
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
        setFlage(!flage)
    };
    // 处理筛选变化
    const handleFilterChange = (filterName: string, checkedValues: string[]) => {
        debugger
        console.log('筛选器名称:', filterName, '选中的值:', checkedValues);
        const newFilters = checkedValues.map(val => ({ key: filterName, value: val }));
        const existingOtherFilters = filters.filter(item => item.key !== filterName);
        const combinedFilters = [...newFilters, ...filters];

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

    // 导出数据
    // const handleExport = () => {
    //     message.info('导出功能待实现');
    // };


    // 导出数据函数 - 导出当前页面数据
    const handleExport = () => {
        if (data.length === 0) {
            message.warning('没有数据可以导出');
            return;
        }

        try {
            // 准备数据
            const exportData = data.map((item, index) => ({
                序号: (current - 1) * localPageSize + index + 1,
                文档名称: item.DOC_NAME || '',
                单号: item.DOC_NUMBER || '',
                样品柜位: item.FD_COL_VY6XBM || '',
                测试柜位: item.FD_COL_6YI0L7 || '',
                样品数量: item.FD_COL_3MM6EF || 0,
                测试点数: item.FD_COL_1CBIHH || 0,
                优先级: item.DOC_PRIORITY || '',
                是否为返工: item.FD_COL_1MRA3M === '1' ? '是' : item.FD_COL_1MRA3M === '2' ? '否' : '-',
                当前站点: item.FD_COL_VGKEFG || '',
                流入当前站点时长: item.FD_COL_ZMOHYP || '',
                项目群: item.FD_COL_8MKYGI || '',
                项目号: item.DOC_PROJECT || '',
                预计结果上传时间: item.FD_COL_4QGWDA || '',
                时效: item.DOC_AGING || ''
            }));

            // 创建工作簿
            const wb = XLSX.utils.book_new();
            const ws = XLSX.utils.json_to_sheet(exportData);

            // 设置列宽
            const wscols = [
                { wch: 8 },   // 序号
                { wch: 30 },  // 文档名称
                { wch: 20 },  // 单号
                { wch: 15 },  // 样品柜位
                { wch: 15 },  // 测试柜位
                { wch: 10 },  // 样品数量
                { wch: 10 },  // 测试点数
                { wch: 10 },  // 优先级
                { wch: 10 },  // 是否为返工
                { wch: 15 },  // 当前站点
                { wch: 20 },  // 流入当前站点时长
                { wch: 15 },  // 项目群
                { wch: 15 },  // 项目号
                { wch: 20 },  // 预计结果上传时间
                { wch: 15 },  // 时效
            ];
            ws['!cols'] = wscols;

            // 添加筛选条件信息作为备注
            if (filters.length > 0) {
                const filterText = `筛选条件: ${filters.map(f => `${f.key || f.value}`).join(', ')}`;
                ws['!merges'] = [{ s: { r: 0, c: 0 }, e: { r: 0, c: 14 } }];
                ws['A1'] = { v: filterText, t: 's' };
                ws['!rows'] = [{ hpt: 20 }];
            }

            // 添加工作表到工作簿
            XLSX.utils.book_append_sheet(wb, ws, '流程查看列表');

            // 生成文件名
            const date = new Date();
            const dateStr = `${date.getFullYear()}${(date.getMonth() + 1).toString().padStart(2, '0')}${date.getDate().toString().padStart(2, '0')}`;
            const timeStr = `${date.getHours().toString().padStart(2, '0')}${date.getMinutes().toString().padStart(2, '0')}`;
            const fileName = `流程查看列表_${dateStr}_${timeStr}.xlsx`;

            // 导出文件
            XLSX.writeFile(wb, fileName);

            message.success(`导出成功！共 ${exportData.length} 条记录`);
        } catch (error) {
            console.error('导出失败:', error);
            message.error('导出失败，请重试');
        }
    };

    // 表格列定义
    const columns = [
        {
            title: '序号',
            key: 'index',
            width: 80,
            align: 'center' as const,
            render: (_: any, __: any, index: number) => {
                // 计算当前页的序号
                return (current - 1) * localPageSize + index + 1;
            }
        },
        {
            title: '文档名称',
            dataIndex: 'DOC_NAME',
            key: 'DOC_NAME',
            width: 200,
            ellipsis: true,
        },
        {
            title: '单号',
            dataIndex: 'DOC_NUMBER',
            key: 'DOC_NUMBER',
            width: 150,
            ellipsis: true,
            sorter: (a :LimsData, b :LimsData) => {
                const strA = (a.DOC_NUMBER || '').toString().trim();
                const strB = (b.DOC_NUMBER || '').toString().trim();
                return strA.localeCompare(strB, 'zh-CN-u-co-pinyin');
            },
        },
        {
            title: '样品柜位',
            dataIndex: 'FD_COL_VY6XBM',
            key: 'FD_COL_VY6XBM',
            width: 120,
            ellipsis: true,
            render: (text: string) => text || '-',
        },
        // {
        //     title: '测试柜位',
        //     dataIndex: 'FD_COL_6YI0L7',
        //     key: 'FD_COL_6YI0L7',
        //     width: 120,
        //     ellipsis: true,
        //     render: (text: string) => text || '-',
        // },
        {
            title: '样品数量',
            dataIndex: 'DOC_NUM',
            key: 'DOC_NUM',
            width: 100,
            align: 'center' as const,
            render: (text: number) => text || '0',
        },
        {
            title: '测试点数',
            dataIndex: 'DOC_PT',
            key: 'DOC_PT',
            width: 100,
            align: 'center' as const,
            render: (text: number) => text || '0',
        },
        {
            title: '优先级',
            dataIndex: 'DOC_PRIORITY',
            key: 'DOC_PRIORITY',
            width: 100,
            align: 'center' as const,

            render: (text: string) => {
                //text === '1' ? '是' : text === '0' ? '否' : '-'
                return text === '1' ? 'B' : text === '1' ? 'A' : 'C';
            },
            sorter: (a :LimsData, b :LimsData) => {
                const strA = (a.DOC_NUMBER || '').toString().trim();
                const strB = (b.DOC_NUMBER || '').toString().trim();
                return strA.localeCompare(strB, 'zh-CN-u-co-pinyin');
            },
        },
        {
            title: '是否为返工',
            dataIndex: 'DOC_STATE',
            key: 'DOC_STATE',
            width: 100,
            align: 'center' as const,
            render: (text: string) => text === '1' ? '是' : text === '0' ? '否' : '-',
        },
        {
            title: '当前站点',
            dataIndex: 'DOC_SITE',
            key: 'DOC_SITE',
            width: 100,
            align: 'center' as const,
            //render: (text: string) => text || '-',

            render: (text: string) => {
                //text === '1' ? '是' : text === '0' ? '否' : '-'
                const item = statusList.find(item => item.value === text);
                return item ? item.label : '未知状态';
            },
        },
        {
            title: '流入当前站点时长',
            dataIndex: 'DOC_NEWSITETIME',
            key: 'DOC_NEWSITETIME',
            width: 150,
            align: 'center' as const,
            render: (text: string) => text || '-',
        },
        // {
        //     title: '项目群',
        //     dataIndex: 'FD_COL_8MKYGI',
        //     key: 'FD_COL_8MKYGI',
        //     width: 120,
        //     ellipsis: true,
        //     render: (text: string) => text || '-',
        // },
        {
            title: '项目号',
            dataIndex: 'DOC_PROJECT',
            key: 'DOC_PROJECT',
            width: 120,
            ellipsis: true,
            render: (text: string) => text || '-',
        },
        {
            title: '预计结果上传时间',
            dataIndex: 'DOC_UPDATETIME',
            key: 'DOC_UPDATETIME',
            width: 150,
            align: 'center' as const,
            render: (text: string) => text || '-',
        },
        {
            title: '时效',
            dataIndex: 'DOC_AGING',
            key: 'DOC_AGING',
            width: 120,
            align: 'center' as const,
            render: (text: string) => text || '-',
        },
    ];

    useEffect(() => {
        fetchData();
    }, []);

    return (
        <div style={{ padding: '20px', backgroundColor: '#f0f2f5', minHeight: '100vh' }}>
            {/* 顶部筛选区 */}
            <div style={{ marginBottom: '20px', backgroundColor: '#fff', padding: '15px', borderRadius: '8px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '15px' }}>
                    <h3 style={{ margin: 0 }}>流程查看列表</h3>
                    <Button type="primary" onClick={handleExport}>导出数据</Button>
                </div>

                {/* 流程状态筛选 */}
                <div style={{ marginBottom: '15px' }}>
                    <div style={{ marginBottom: '10px', display: 'flex', justifyContent: 'space-between' }}>
                        <div>

                            优先级：
                            <Checkbox.Group name="fdname"
                                            options={fd_type.map(opt => ({ label: opt.label, value: opt.value }))}
                                            onChange={(values) => handleFilterChange("fdname", values as string[])}
                            />
                        </div>
                        <div style={{ display: 'flex', gap: '10px' }}>
                            {/* <Button size="small">全选</Button>
                            <Button size="small">全不选</Button> */}
                        </div>
                    </div>
                    <div style={{ marginBottom: '10px', display: 'flex', justifyContent: 'space-between' }}>
                        <div>
                            站点状态：
                            <Checkbox.Group name="fdname1"
                                            options={processStatusOptions.map(opt => ({ label: opt.label, value: opt.value }))}
                                            onChange={(values) => handleFilterChange("fdname1", values as string[])}
                            />
                        </div>
                        <div style={{ display: 'flex', gap: '10px' }}>
                            {/* <Button size="small">全选</Button>
                            <Button size="small">全不选</Button> */}
                        </div>
                    </div>

                    <div style={{ marginBottom: '10px', display: 'flex', justifyContent: 'space-between' }}>
                        <div>
                            单据状态：
                            <Checkbox.Group name="fdname2"
                                            options={fd_lable.map(opt => ({ label: opt.label, value: opt.value }))}
                                            onChange={(values) => handleFilterChange("fdname2", values as string[])}
                            />
                        </div>
                        <div style={{ display: 'flex', gap: '10px' }}>
                            {/* <Button size="small">全选</Button>
                            <Button size="small">全不选</Button> */}
                        </div>
                    </div>
                    {/* 其他隐藏筛选数据 */}
                    { flage && (
                        <div>
                            ceshi
                        </div>

                    ) }



                </div>

                {/* 其他筛选条件 */}
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Button  style={{ fontWeight: 'bold', float:'right' }} type='primary' size="small"   onClick={changView} >其他筛选条件</Button>
                </div>
            </div>

            {/* 列表区 */}
            <div style={{ backgroundColor: '#fff', padding: '15px', borderRadius: '8px' }}>
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
                        pageSizeOptions: ['10', '20', '50', '100',"1000"],
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