// services/roomService.ts
import axios from 'axios';
import { ApiResponse, RoomItem, CreateRoomDto } from '../types/room';

const API_BASE_URL = '/date';

export const roomService = {
    // 获取列表
    async getRoomList(url:string): Promise<ApiResponse> {
        const response = await axios.post<ApiResponse>(`${API_BASE_URL}`+url+"/list");
        return response.data;
    },

    // 新增
    async createRoom(url:string,data: CreateRoomDto): Promise<ApiResponse> {
        const response = await axios.post<ApiResponse>(`${API_BASE_URL}`+url+`/add`, data);
        return response.data;
    },

    // 查看详情
    async getRoomDetail(url:string,id: number): Promise<ApiResponse> {
        const response = await axios.post<ApiResponse>(`${API_BASE_URL}`+url+`/view`, { id });
        return response.data;
    },

    // 导出为CSV
    exportToCSV(data: RoomItem[]): void {
        if (!data || data.length === 0) {
            //message.warning('没有数据可导出');
            return;
        }

        const headers = [
            'ID',
            '创建时间',
            '房间名称',
            '房间电话',
            '地址',
            '负责人',
            '负责人电话',
            '备注'
        ];

        const csvData = data.map(item => [
            item.id,
            item.createTime,
            item.fdbz
        ]);

        const csvContent = [
            headers.join(','),
            ...csvData.map(row => row.map(cell => `"${cell}"`).join(','))
        ].join('\n');

        const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        const url = URL.createObjectURL(blob);
        link.setAttribute('href', url);
        link.setAttribute('download', `房间列表_${new Date().getTime()}.csv`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    },

    // 导出为Excel (使用第三方库)
    exportToExcel(data: RoomItem[]): void {
        import('xlsx').then(xlsx => {
            const worksheet = xlsx.utils.json_to_sheet(data.map(item => ({
                'ID': item.id,
                '创建时间': item.createTime,
                '名称': item.fdName,
                '备注': item.fdbz
            })));

            const workbook = xlsx.utils.book_new();
            xlsx.utils.book_append_sheet(workbook, worksheet, '房间列表');
            xlsx.writeFile(workbook, `房间列表_${new Date().getTime()}.xlsx`);
        });
    }
};