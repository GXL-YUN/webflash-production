import React, { useState, useEffect } from 'react';
import {
    Card,
    Timeline,
    Tree,
    Button,
    Upload,
    Progress,
    Tag,
    Row,
    Col,
    Divider,
    Space,
    Badge,
    Alert,
    List,
    Avatar
} from 'antd';
import {
    ClockCircleOutlined,
    CheckCircleOutlined,
    WarningOutlined,
    QuestionCircleOutlined,
    PaperClipOutlined,
    FileTextOutlined,
    FilePdfOutlined,
    FileImageOutlined,
    FolderOutlined
} from '@ant-design/icons';
import './css/SysAttMainListView.css';

interface TimelineItem {
    time: string;
    title: string;
    description: string;
    status: 'success' | 'processing' | 'error';
}
interface ProjectCard {
    title: string;
    icon: React.ReactNode;
    data: Array<{ label: string; value: string | number; status?: 'success' | 'warning' | 'error' }>;
    extraInfo?: string;
}

interface TreeNode {
    title: string;
    key: string;
    icon?: React.ReactNode;
    children?: TreeNode[];
    status?: 'completed' | 'in-progress' | 'pending';
}

interface Attachment {
    id: string;
    name: string;
    type: 'pdf' | 'doc' | 'image' | 'other';
    size: string;
    uploadTime: string;
    uploader: string;
}



interface attMianActionProps {
    fdId?: string;
    className?: string;
    defaultKey?: string; // 默认key值
    //onChange?: (value: FileInfo[]) => void; // 值变化回调
}
/**
 * 附件列表展示页面
 * @constructor
 */

const SysAttMainListView: React.FC<attMianActionProps> = ({
                                                              fdId = '',
                                                              className = '',
                                                              defaultKey = 'default_fixed_key'
                                                          }) => {
    // 状态管理

    debugger

    const [attachments, setAttachments] = useState<Attachment[]>([
        { id: '1', name: '项目需求文档.pdf', type: 'pdf', size: '2.4 MB', uploadTime: '2026-02-10 10:30', uploader: '张三' },
        { id: '2', name: 'UI设计稿.zip', type: 'other', size: '15.7 MB', uploadTime: '2026-02-12 14:20', uploader: '李四' },
        { id: '3', name: '数据库设计文档.docx', type: 'doc', size: '1.2 MB', uploadTime: '2026-02-13 09:15', uploader: '王五' },
        { id: '4', name: '系统架构图.png', type: 'image', size: '3.8 MB', uploadTime: '2026-02-14 16:45', uploader: '赵六' },
        { id: '5', name: '测试用例.xlsx', type: 'other', size: '0.8 MB', uploadTime: '2026-02-15 11:20', uploader: '张三' },
        { id: '6', name: '项目周报.pdf', type: 'pdf', size: '1.1 MB', uploadTime: '2026-02-16 17:30', uploader: '李四' },
        { id: '6', name: '项目周报.pdf', type: 'pdf', size: '1.1 MB', uploadTime: '2026-02-16 17:30', uploader: '李四' },
    ]);
    // 获取文件类型图标
    const getFileIcon = (type: string) => {
        switch (type) {
            case 'pdf': return <FilePdfOutlined style={{ color: '#f5222d' }} />;
            case 'doc': return <FileTextOutlined style={{ color: '#1890ff' }} />;
            case 'image': return <FileImageOutlined style={{ color: '#52c41a' }} />;
            default: return <FileTextOutlined />;
        }
    };
    return (
        <div className="project-dashboard">
            <Divider />
            {/* 下层布局 - 项目资料附件 */}
            <div className="dashboard-bottom">
                <Card
                    title={
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                            <PaperClipOutlined style={{ marginRight: 8, color: '#1890ff' }} />
                            <span>项目资料附件</span>
                            <Badge count={attachments.length} style={{ marginLeft: 8 }} />
                        </div>
                    }
                    bordered={false}
                    className="attachment-card"
                    extra={
                        <Upload>
                            <Button type="primary" icon={<PaperClipOutlined />}>上传文件</Button>
                        </Upload>
                    }
                >
                    <List
                        itemLayout="horizontal"
                        dataSource={attachments}
                        renderItem={(item) => (
                            <List.Item
                                actions={[
                                    <a key="download">下载</a>,
                                    <a key="preview">预览</a>,
                                    <a key="delete">删除</a>
                                ]}
                            >
                                <List.Item.Meta
                                    avatar={<Avatar icon={getFileIcon(item.type)} />}
                                    title={
                                        <div style={{ display: 'flex', alignItems: 'center' }}>
                                            <span style={{ marginRight: 8 }}>{item.name}</span>
                                            {/*<Tag size="small" color="blue">{item.type.toUpperCase()}</Tag>*/}
                                        </div>
                                    }
                                    description={
                                        <div className="attachment-meta">
                                            <span>大小: {item.size}</span>
                                            <span>上传时间: {item.uploadTime}</span>
                                            <span>上传人: {item.uploader}</span>
                                        </div>
                                    }
                                />
                            </List.Item>
                        )}
                    />
                </Card>
            </div>
        </div>
    );
};

export default SysAttMainListView;