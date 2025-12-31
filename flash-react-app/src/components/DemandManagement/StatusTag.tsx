// components/DemandManagement/StatusTag.tsx
import React from 'react';
import { Tag } from 'antd';
import { DemandStatus } from './types/demand';


interface StatusTagProps {
    status: DemandStatus;
}

const StatusTag: React.FC<StatusTagProps> = ({ status }) => {
    const statusConfig = {
        [DemandStatus.PENDING]: { color: 'default', text: '待排期' },
        [DemandStatus.SCHEDULED]: { color: 'blue', text: '已排期' },
        [DemandStatus.DEVELOPING]: { color: 'processing', text: '开发中' },
        [DemandStatus.TESTING]: { color: 'orange', text: '测试中' },
        [DemandStatus.ONLINE]: { color: 'success', text: '已上线' },
        [DemandStatus.CLOSED]: { color: 'default', text: '已关闭' },
        [DemandStatus.REJECTED]: { color: 'red', text: '已拒绝' },
    };

    const config = statusConfig[status];
    return <Tag color={config.color}>{config.text}</Tag>;
};

export default StatusTag;