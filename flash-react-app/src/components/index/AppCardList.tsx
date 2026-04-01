// src/components/AppCardList/AppCardList.tsx
import React from 'react';
import { Row, Col, Empty, Typography, Spin } from 'antd';
import AppCard from './AppCard';
import { AppCardItem } from './type';

const { Title } = Typography;

interface AppCardListProps {
    data: AppCardItem[];
    title?: string;
    subtitle?: string;
    loading?: boolean;
    emptyText?: string;
    columns?: {
        xs?: number;
        sm?: number;
        md?: number;
        lg?: number;
        xl?: number;
    };
    onItemClick?: (item: AppCardItem) => void;
}

const AppCardList: React.FC<AppCardListProps> = ({
                                                     data,
                                                     title,
                                                     subtitle,
                                                     loading = false,
                                                     emptyText = '暂无数据',
                                                     columns = { xs: 1, sm: 2, md: 3, lg: 4, xl: 6 },
                                                     onItemClick
                                                 }) => {
    if (loading) {
        return (
            <div style={{ padding: 24, textAlign: 'center' }}>
                <Spin size="large" />
            </div>
        );
    }

    if (!data || data.length === 0) {
        return <Empty description={emptyText} style={{ margin: 40 }} />;
    }

    return (
        <div style={{ padding: 16 }}>
            {/* 标题区域 */}
            {(title || subtitle) && (
                <div style={{ marginBottom: 20 }}>
                    {title && (
                        <Title level={4} style={{ marginBottom: 4 }}>
                            {title}
                        </Title>
                    )}
                    {subtitle && (
                        <div style={{ color: '#666', fontSize: 14 }}>
                            {subtitle}
                        </div>
                    )}
                </div>
            )}

            {/* 卡片列表 */}
            <Row gutter={[16, 16]} justify="start">
                {data.map((item) => (
                    <Col
                        key={item.key}
                        xs={24 / (columns.xs || 1)}
                        sm={24 / (columns.sm || 2)}
                        md={24 / (columns.md || 3)}
                        lg={24 / (columns.lg || 4)}
                        xl={24 / (columns.xl || 6)}
                        style={{ display: 'flex' }}
                    >
                        <AppCard
                            item={item}
                            onClick={() => onItemClick?.(item)}
                        />
                    </Col>
                ))}
            </Row>
        </div>
    );
};

export default AppCardList;