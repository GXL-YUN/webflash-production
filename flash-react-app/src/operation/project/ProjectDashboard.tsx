import React, {useState, useEffect} from 'react';
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
import './css/ProjectDashboard.css';

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

const ProjectDashboard: React.FC = () => {
    // 状态管理
    const [timelineData, setTimelineData] = useState<TimelineItem[]>([
        {time: '2026-02-10 09:00', title: '项目启动会', description: '项目正式启动，明确目标和范围', status: 'success'},
        {time: '2026-02-12 14:30', title: '需求评审', description: '完成第一阶段需求评审', status: 'success'},
        {time: '2026-02-15 10:00', title: 'UI设计完成', description: 'UI设计稿交付', status: 'success'},
        {time: '2026-02-20 当前', title: '开发阶段', description: '核心功能开发中', status: 'processing'},
        {time: '2026-02-25 计划', title: '第一阶段测试', description: '完成单元测试和集成测试', status: 'processing'},
        {time: '2026-03-01 计划', title: '项目交付', description: '项目正式上线', status: 'processing'},
    ]);

    const [projectCards, setProjectCards] = useState<ProjectCard[]>([
        {
            title: '完成情况',
            icon: <CheckCircleOutlined style={{color: '#52c41a', fontSize: '20px'}}/>,
            data: [
                {label: '总体进度', value: 65, status: 'success'},
                {label: '已完成任务', value: '13/20'},
                {label: '按时交付率', value: '92%'},
                {label: '代码质量', value: 'A-'},
                {label: '已完成任务', value: '13/20'},
                {label: '按时交付率', value: '92%'},
                {label: '代码质量', value: 'A-'},
                {label: '已完成任务', value: '13/20'},
                {label: '按时交付率', value: '92%'},
                {label: '代码质量', value: 'A-'},
            ],
            extraInfo: '较上周提升8%'
        },
        {
            title: '风险点识别',
            icon: <WarningOutlined style={{color: '#faad14', fontSize: '20px'}}/>,
            data: [
                {label: '高风险', value: 2, status: 'error'},
                {label: '中风险', value: 3, status: 'warning'},
                {label: '低风险', value: 5, status: 'success'},
                {label: '已解决', value: 4},
                {label: '高风险', value: 2, status: 'error'},
                {label: '中风险', value: 3, status: 'warning'},
                {label: '低风险', value: 5, status: 'success'},
                {label: '已解决', value: 4},
                {label: '高风险', value: 2, status: 'error'},
                {label: '中风险', value: 3, status: 'warning'},
                {label: '低风险', value: 5, status: 'success'},
                {label: '已解决', value: 4},
                {label: '高风险', value: 2, status: 'error'},
                {label: '中风险', value: 3, status: 'warning'},
                {label: '低风险', value: 5, status: 'success'},
                {label: '已解决', value: 4},
            ],
            extraInfo: '本周新增1个风险点'
        },
        {
            title: '待确认项',
            icon: <QuestionCircleOutlined style={{color: '#1890ff', fontSize: '20px'}}/>,
            data: [
                {label: '待决策', value: 3, status: 'warning'},
                {label: '待评审', value: 2},
                {label: '待沟通', value: 4},
                {label: '超时未处理', value: 1, status: 'error'},
                {label: '待决策', value: 3, status: 'warning'},
                {label: '待评审', value: 2},
                {label: '待沟通', value: 4},
                {label: '超时未处理', value: 1, status: 'error'},
                {label: '待决策', value: 3, status: 'warning'},
                {label: '待评审', value: 2},
                {label: '待沟通', value: 4},
                {label: '超时未处理', value: 1, status: 'error'},
                {label: '待决策', value: 3, status: 'warning'},
                {label: '待评审', value: 2},
                {label: '待沟通', value: 4},
                {label: '超时未处理', value: 1, status: 'error'},
            ],
            extraInfo: '3天内需处理完成'
        }
    ]);

    const [treeData, setTreeData] = useState<TreeNode[]>([
        {
            title: '项目总览',
            key: '0',
            icon: <FolderOutlined/>,
            children: [
                {
                    title: '需求文档',
                    key: '0-0',
                    icon: <FileTextOutlined/>,
                    children: [
                        {title: 'PRD文档_V1.2', key: '0-0-0', status: 'completed'},
                        {title: '需求规格说明书', key: '0-0-1', status: 'completed'},
                        {title: '需求变更记录', key: '0-0-2', status: 'in-progress'},
                    ],
                },
                {
                    title: '设计稿',
                    key: '0-1',
                    icon: <FileImageOutlined/>,
                    children: [
                        {title: 'UI设计_V2.0', key: '0-1-0', status: 'completed'},
                        {title: '原型交互图', key: '0-1-1', status: 'completed'},
                        {title: '视觉规范', key: '0-1-2', status: 'in-progress'},
                    ],
                },
                {
                    title: '开发代码',
                    key: '0-2',
                    icon: <FolderOutlined/>,
                    children: [
                        {
                            title: '前端模块',
                            key: '0-2-0',
                            children: [
                                {title: '用户管理模块', key: '0-2-0-0', status: 'completed'},
                                {title: '权限控制模块', key: '0-2-0-1', status: 'in-progress'},
                                {title: '数据可视化模块', key: '0-2-0-2', status: 'pending'},
                            ],
                        },
                        {
                            title: '后端模块',
                            key: '0-2-1',
                            children: [
                                {title: 'API接口开发', key: '0-2-1-0', status: 'in-progress'},
                                {title: '数据库设计', key: '0-2-1-1', status: 'completed'},
                                {title: '定时任务模块', key: '0-2-1-2', status: 'pending'},
                            ],
                        },
                    ],
                },
                {
                    title: '测试文档',
                    key: '0-3',
                    icon: <FileTextOutlined/>,
                    children: [
                        {title: '测试用例_V1.0', key: '0-3-0', status: 'in-progress'},
                        {title: '测试报告', key: '0-3-1', status: 'pending'},
                    ],
                },
            ],
        },
    ]);

    const [attachments, setAttachments] = useState<Attachment[]>([
        {
            id: '1',
            name: '项目需求文档.pdf',
            type: 'pdf',
            size: '2.4 MB',
            uploadTime: '2026-02-10 10:30',
            uploader: '张三'
        },
        {
            id: '2',
            name: 'UI设计稿.zip',
            type: 'other',
            size: '15.7 MB',
            uploadTime: '2026-02-12 14:20',
            uploader: '李四'
        },
        {
            id: '3',
            name: '数据库设计文档.docx',
            type: 'doc',
            size: '1.2 MB',
            uploadTime: '2026-02-13 09:15',
            uploader: '王五'
        },
        {
            id: '4',
            name: '系统架构图.png',
            type: 'image',
            size: '3.8 MB',
            uploadTime: '2026-02-14 16:45',
            uploader: '赵六'
        },
        {
            id: '5',
            name: '测试用例.xlsx',
            type: 'other',
            size: '0.8 MB',
            uploadTime: '2026-02-15 11:20',
            uploader: '张三'
        },
        {id: '6', name: '项目周报.pdf', type: 'pdf', size: '1.1 MB', uploadTime: '2026-02-16 17:30', uploader: '李四'},
    ]);

    // 获取文件类型图标
    const getFileIcon = (type: string) => {
        switch (type) {
            case 'pdf':
                return <FilePdfOutlined style={{color: '#f5222d'}}/>;
            case 'doc':
                return <FileTextOutlined style={{color: '#1890ff'}}/>;
            case 'image':
                return <FileImageOutlined style={{color: '#52c41a'}}/>;
            default:
                return <FileTextOutlined/>;
        }
    };

    // 树节点状态标签
    const getStatusTag = (status?: 'completed' | 'in-progress' | 'pending') => {
        switch (status) {
            case 'completed':
                return <Tag color="success">已完成</Tag>;
            case 'in-progress':
                return <Tag color="processing">进行中</Tag>;
            case 'pending':
                return <Tag color="default">未开始</Tag>;
            default:
                return null;
        }
    };

    // 自定义树节点渲染
    const titleRender = (nodeData: TreeNode) => {
        return (
            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
        <span>
          {nodeData.icon}
            <span style={{marginLeft: 8}}>{nodeData.title}</span>
        </span>
                {getStatusTag(nodeData.status)}
            </div>
        );
    };

    return (
        <div className="project-dashboard">
            {/* 上层布局 - 时间线 + 项目卡片 (1:3比例) */}
            <Row gutter={[16, 16]} className="dashboard-top">
                <Col xs={24} md={6}>
                    <Card
                        title={
                            <div style={{display: 'flex', alignItems: 'center'}}>
                                <ClockCircleOutlined style={{marginRight: 8, color: '#1890ff'}}/>
                                <span>项目时间线</span>
                            </div>
                        }
                        bordered={false}
                        className="timeline-card"
                    >
                        <Timeline
                            mode="left"
                            items={timelineData.map((item) => ({
                                dot: item.status === 'success'
                                    ? <CheckCircleOutlined style={{color: '#52c41a'}}/>
                                    : item.status === 'error'
                                        ? <WarningOutlined style={{color: '#f5222d'}}/>
                                        : <ClockCircleOutlined style={{color: '#1890ff'}}/>,
                                color: item.status === 'success' ? 'green' : item.status === 'error' ? 'red' : 'blue',
                                children: (
                                    <>
                                        <div className="timeline-time">{item.time}</div>
                                        <div className="timeline-title">{item.title}</div>
                                        <div className="timeline-description">{item.description}</div>
                                    </>
                                ),
                            }))}
                        />
                    </Card>
                </Col>

                <Col xs={24} md={18}>
                    <Row gutter={[16, 16]}>
                        {projectCards.map((card, index) => (
                            <Col xs={24} md={8} key={index}>
                                <Card
                                    title={
                                        <div style={{display: 'flex', alignItems: 'center'}}>
                                            {card.icon}
                                            <span style={{marginLeft: 8}}>{card.title}</span>
                                        </div>
                                    }
                                    bordered={false}
                                    className="project-card"
                                >
                                    {card.data.map((item, idx) => (
                                        <div key={idx} className="card-data-item">
                                            <div className="data-label">{item.label}</div>
                                            <div className="data-value">
                                                {typeof item.value === 'number' ? (
                                                    <>
                                                        <Progress
                                                            percent={item.value}
                                                            size="small"
                                                            strokeColor={item.status === 'success' ? '#52c41a' : item.status === 'error' ? '#f5222d' : '#faad14'}
                                                            style={{width: '60%', marginRight: 8}}
                                                        />
                                                        <span>{item.value}%</span>
                                                    </>
                                                ) : (
                                                    <span className={`value-text ${item.status || ''}`}>
                            {item.value}
                          </span>
                                                )}
                                            </div>
                                        </div>
                                    ))}
                                    {card.extraInfo && (
                                        <Alert
                                            message={card.extraInfo}
                                            type="info"
                                            showIcon
                                            style={{marginTop: 16, fontSize: '12px'}}
                                            // size="small"
                                        />
                                    )}
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Col>
            </Row>

            <Divider/>

            {/* 中层布局 - 树状图 */}
            <div className="dashboard-middle">
                <Card
                    title={
                        <div style={{display: 'flex', alignItems: 'center'}}>
                            <FolderOutlined style={{marginRight: 8, color: '#1890ff'}}/>
                            <span>项目结构目录</span>
                        </div>
                    }
                    bordered={false}
                    className="tree-card"
                    extra={
                        <Space>
                            <Button type="primary" size="small">展开全部</Button>
                            <Button size="small">折叠全部</Button>
                            <Button size="small">导出结构</Button>
                        </Space>
                    }
                >
                    <Tree
                        showLine
                        showIcon
                        defaultExpandedKeys={['0', '0-0', '0-1', '0-2']}
                        treeData={treeData}
                        titleRender={titleRender}
                        style={{padding: '16px 0'}}
                    />
                </Card>
            </div>

            <Divider/>

            {/* 下层布局 - 项目资料附件 */}
            <div className="dashboard-bottom">
                <Card
                    title={
                        <div style={{display: 'flex', alignItems: 'center'}}>
                            <PaperClipOutlined style={{marginRight: 8, color: '#1890ff'}}/>
                            <span>项目资料附件</span>
                            <Badge count={attachments.length} style={{marginLeft: 8}}/>
                        </div>
                    }
                    bordered={false}
                    className="attachment-card"
                    extra={
                        <Upload>
                            <Button type="primary" icon={<PaperClipOutlined/>}>上传文件</Button>
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
                                    avatar={<Avatar icon={getFileIcon(item.type)}/>}
                                    title={
                                        <div style={{display: 'flex', alignItems: 'center'}}>
                                            <span style={{marginRight: 8}}>{item.name}</span>
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

export default ProjectDashboard;