//数据model类型


import * as XLSX from 'xlsx';
import { message} from 'antd';

export interface RoomItem {
    fdId: string
    id: number,
    createTime: number,
    createBy: number,
    modifyTime: number,
    modifyBy: string,
    fdName: string,
    fdMassage: string,
    fdBz: string,
    fdType: string,
    fdUserName: string,
    fdActoinTime: number,
    fdEndTime: number,
    richTextContent_long: string,
    fileList: [],
    fileLists: [],
    fileListss: []
}
/**
 * 生成明细表单据数据
 * @param data
 * @param current
 * @param localPageSize
 */
    // 表格列定义
export  const columns = [
        // {
        //     title: '序号',
        //     key: 'index',
        //     width: 80,
        //     align: 'center' as const,
        //     render: (_: any, __: any, index: number) => {
        //         return (current - 1) * localPageSize + index + 1;
        //     }
        // },
        {
            title: '文档名称',
            dataIndex: 'fdName',
            key: 'fdName',
            width: 200,
            ellipsis: true,
            render: (text: string, record: RoomItem) => text || record.fdName || '-',
        },
        {
            title: '项目描述',
            dataIndex: 'fdMassage',
            key: 'fdMassage',
            width: 200,
            ellipsis: true,
            render: (text: string, record: RoomItem) => text || record.fdName || '-',
        },
        {
            title: '项目状态',
            dataIndex: 'fdType',
            key: 'fdType',
            width: 200,
            ellipsis: true,
            render: (text: string, record: RoomItem) => text || record.fdName || '-',
        },
        {
            title: '项目对接人',
            dataIndex: 'fdUserName',
            key: 'fdUserName',
            width: 200,
            ellipsis: true,
            render: (text: string, record: RoomItem) => text || record.fdName || '-',
        },
        {
            title: '项目开始时间',
            dataIndex: 'fdActoinTime',
            key: 'fdActoinTime',
            width: 200,
            ellipsis: true,
            render: (text: string, record: RoomItem) => text || record.fdName || '-',
        },
        {
            title: '项目结束时间',
            dataIndex: 'fdEndTime',
            key: 'fdEndTime',
            width: 200,
            ellipsis: true,
            render: (text: string, record: RoomItem) => text || record.fdName || '-',
        },
    ];

/**
 * 导出数据函数
 * @param data   数据组
 * @param current
 * @param localPageSize    分页数据
//  */
// const handleExport = (data,current,localPageSize,filters) => {
//     if (data.length === 0) {
//         message.warning('没有数据可以导出');
//         return;
//     }
//     try {
//         // 准备数据 - 根据实际字段调整
//         const exportData = data.map((item, index) => ({
//             序号: (current - 1) * localPageSize + index + 1,
//             文档名称: item.fdName || '',
//             文档名称1: item.fdName || '',
//             文档名称2: item.fdName || '',
//             文档名称3: item.fdName || '',
//             文档名称4: item.fdName || '',
//             文档名称5: item.fdName || '',
//             文档名称6: item.fdName || '',
//             文档名称7: item.fdName || '',
//             文档名称8: item.fdName || '',
//
//         }));
//
//         // 创建工作簿
//         const wb = XLSX.utils.book_new();
//         const ws = XLSX.utils.json_to_sheet(exportData);
//
//         // 设置列宽
//         const wscols = [
//             { wch: 8 },   // 序号
//             { wch: 30 },  // 文档名称
//             { wch: 20 },  // 单号
//             { wch: 15 },  // 样品柜位
//             { wch: 15 },  // 测试柜位
//             { wch: 10 },  // 样品数量
//             { wch: 10 },  // 测试点数
//             { wch: 10 },  // 优先级
//             { wch: 10 },  // 是否为返工
//             { wch: 15 },  // 当前站点
//             { wch: 20 },  // 流入当前站点时长
//             { wch: 15 },  // 项目号
//             { wch: 20 },  // 预计结果上传时间
//             { wch: 15 },  // 时效
//         ];
//         ws['!cols'] = wscols;
//
//         // 添加筛选条件信息作为备注
//         if (filters.length > 0) {
//             const filterText = `筛选条件: ${filters.map(f => `${f.key}: ${f.value}`).join(', ')}`;
//             ws['!merges'] = [{ s: { r: 0, c: 0 }, e: { r: 0, c: 13 } }];
//             ws['A1'] = { v: filterText, t: 's' };
//             ws['!rows'] = [{ hpt: 20 }];
//         }
//
//         // 添加工作表到工作簿
//         XLSX.utils.book_append_sheet(wb, ws, '流程查看列表');
//
//         // 生成文件名
//         const date = new Date();
//         const dateStr = `${date.getFullYear()}${(date.getMonth() + 1).toString().padStart(2, '0')}${date.getDate().toString().padStart(2, '0')}`;
//         const timeStr = `${date.getHours().toString().padStart(2, '0')}${date.getMinutes().toString().padStart(2, '0')}`;
//         const fileName = `流程查看列表_${dateStr}_${timeStr}.xlsx`;
//
//         // 导出文件
//         XLSX.writeFile(wb, fileName);
//
//         message.success(`导出成功！共 ${exportData.length} 条记录`);
//     } catch (error) {
//         console.error('导出失败:', error);
//         message.error('导出失败，请重试');
//     }
// };
//
//
