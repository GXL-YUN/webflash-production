// types/demand.ts
export enum DemandStatus {
    PENDING = 'pending',      // 待排期
    SCHEDULED = 'scheduled',  // 已排期
    DEVELOPING = 'developing',// 开发中
    TESTING = 'testing',      // 测试中
    ONLINE = 'online',        // 已上线
    CLOSED = 'closed',        // 已关闭
    REJECTED = 'rejected',    // 已拒绝
}



export enum Priority {
    HIGH = 'high',    // 高
    MEDIUM = 'medium',// 中
    LOW = 'low',      // 低
}

export interface IDemand {
    id: string;                     // 需求编号
    name: string;                   // 需求名称
    description: string;            // 需求描述
    businessValue: string;          // 业务价值
    acceptanceCriteria: string;     // 验收标准
    status: DemandStatus;           // 当前状态
    priority: Priority;             // 优先级
    creator: string;                // 创建人
    createdAt: string;              // 创建时间
    scheduledAt?: string;           // 排期时间
    developStartAt?: string;        // 开发开始时间
    developEndAt?: string;          // 开发结束时间
    testStartAt?: string;          // 测试开始时间
    testEndAt?: string;            // 测试结束时间
    onlineAt?: string;             // 上线时间
    closedAt?: string;             // 关闭时间
    version: number;               // 版本号
    modules: string[];             // 涉及模块
    relatedRequirements: string[]; // 关联需求
    rtmId?: string;                // RTM编号
}

export interface IDevIssue {
    id: string;
    demandId: string;
    title: string;
    description: string;
    status: 'pending' | 'in_progress' | 'resolved' | 'closed';
    assignee: string;
    createdAt: string;
    resolvedAt?: string;
    priority: Priority;
}

export interface IOperationIssue {
    id: string;
    demandId: string;
    title: string;
    description: string;
    type: 'bug' | 'performance' | 'security' | 'other';
    status: 'open' | 'investigating' | 'fixing' | 'verified' | 'closed';
    severity: 'critical' | 'major' | 'minor' | 'trivial';
    createdAt: string;
    resolvedAt?: string;
}

export interface IChangeRequest {
    id: string;
    demandId: string;
    title: string;
    description: string;
    changeType: 'scope' | 'schedule' | 'cost' | 'quality';
    status: 'submitted' | 'reviewing' | 'approved' | 'rejected' | 'implemented';
    proposer: string;
    createdAt: string;
    reviewedAt?: string;
    impactAnalysis: string;
}