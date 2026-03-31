// src/components/AppCard/AppCard.tsx
import React from 'react';
import { Card, Typography, Tooltip, Badge } from 'antd';
import {
    CalendarOutlined,
    FormOutlined,
    MessageOutlined,
    SafetyCertificateOutlined,
    ApartmentOutlined,
    FileTextOutlined,
    SettingOutlined,
    TeamOutlined,
    DashboardOutlined,
    FileOutlined,
    DatabaseOutlined,
    CloudOutlined
} from '@ant-design/icons';
import { AppCardItem } from './type';

const { Title, Text } = Typography;

// 创建一个包装组件来处理样式
const StyledIcon: React.FC<{
    icon: React.ReactNode;
    color?: string;
    size?: number;
}> = ({ icon, color = '#1890ff', size = 24 }) => {
    if (React.isValidElement(icon)) {
        return React.cloneElement(icon as React.ReactElement, {
            // style: { fontSize: size, color },
            // color,
            // size
        });
    }
    return <>{icon}</>;
};

interface AppCardProps {
    item: AppCardItem;
    onClick?: () => void;
}

// 定义图标映射类型
const antdIcons: Record<string, React.ReactNode> = {
    'calendar': <CalendarOutlined />,
    'form': <FormOutlined />,
    'message': <MessageOutlined />,
    'safety': <SafetyCertificateOutlined />,
    'apartment': <ApartmentOutlined />,
    'file': <FileTextOutlined />,
    'setting': <SettingOutlined />,
    'team': <TeamOutlined />,
    'dashboard': <DashboardOutlined />,
    'database': <DatabaseOutlined />,
    'cloud': <CloudOutlined />,
    'default': <FileOutlined />
};

const AppCard: React.FC<AppCardProps> = ({ item, onClick }) => {
    const {
        title,
        description,
        icon,
        iconType = 'antd',
        iconColor = '#1890ff',
        disabled = false,
        badge,
        badgeColor = '#ff4d4f'
    } = item;

    // 渲染图标
    const renderIcon = () => {
        if (React.isValidElement(icon)) {
            return (
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: 32,
                    height: 32,
                    fontSize: 24,
                    color: iconColor
                }}>
                    {React.cloneElement(icon as React.ReactElement, {
                        // style: { fontSize: 24, color: iconColor }
                    })}
                </div>
            );
        }

        switch (iconType) {
            case 'image':
                if (typeof icon === 'string') {
                    return (
                        <img
                            src={icon}
                            alt={title}
                            style={{
                                width: 32,
                                height: 32,
                                borderRadius: 6,
                                objectFit: 'contain'
                            }}
                        />
                    );
                }
                break;

            case 'custom':
                return (
                    <div style={{
                        width: 32,
                        height: 32,
                        backgroundColor: `${iconColor}20`,
                        color: iconColor,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        borderRadius: 6,
                        fontSize: 16,
                        fontWeight: 'bold'
                    }}>
                        {typeof icon === 'string' ? icon.charAt(0) : title.charAt(0)}
                    </div>
                );

            case 'antd':
            default:
                if (typeof icon === 'string') {
                    const IconComponent = antdIcons[icon] || antdIcons.default;
                    return (
                        <div style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            width: 32,
                            height: 32
                        }}>
                            {React.isValidElement(IconComponent) ?
                                React.cloneElement(IconComponent as React.ReactElement, {
                                    // style: { fontSize: 24, color: iconColor },
                                    // color: iconColor
                                }) :
                                null
                            }
                        </div>
                    );
                }
        }

        // 默认返回一个占位图标
        return (
            <div style={{
                width: 32,
                height: 32,
                backgroundColor: '#f0f0f0',
                color: '#999',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                borderRadius: 6,
                fontSize: 16,
                fontWeight: 'bold'
            }}>
                {title.charAt(0)}
            </div>
        );
    };

    const cardStyle: React.CSSProperties = {
        width: 240,
        height: 100,
        marginBottom: 16,
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.6 : 1,
        border: '1px solid #f0f0f0',
        borderRadius: 8,
        transition: 'all 0.3s',
        backgroundColor: '#fff',
        boxShadow: '0 1px 2px 0 rgba(0, 0, 0, 0.03)',
    };

    const bodyStyle: React.CSSProperties = {
        padding: 16,
        height: '100%',
        display: 'flex',
        alignItems: 'center'
    };

    return (
        <Badge
            count={badge}
            offset={[-8, 8]}
            color={badgeColor}
            size="small"
        >
            <Card
                hoverable={!disabled}
                onClick={disabled ? undefined : (onClick || item.onClick)}
                style={cardStyle}
                bodyStyle={bodyStyle}
                className={!disabled ? 'app-card-hover' : ''}
            >
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    width: '100%'
                }}>
                    {/* 图标区域 */}
                    <div style={{
                        marginRight: 12,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        flexShrink: 0
                    }}>
                        {renderIcon()}
                    </div>

                    {/* 文字区域 */}
                    <div style={{ flex: 1, minWidth: 0 }}>
                        <Title
                            level={5}
                            style={{
                                margin: 0,
                                fontSize: 16,
                                fontWeight: 600,
                                color: disabled ? '#999' : '#333',
                                lineHeight: 1.5
                            }}
                            ellipsis={{ tooltip: title }}
                        >
                            {title}
                        </Title>
                        {description && (
                            <Tooltip title={description} placement="bottom">
                                <Text
                                    type="secondary"
                                    style={{
                                        fontSize: 12,
                                        display: '-webkit-box',
                                        WebkitLineClamp: 2,
                                        WebkitBoxOrient: 'vertical',
                                        overflow: 'hidden',
                                        textOverflow: 'ellipsis',
                                        lineHeight: 1.5,
                                        marginTop: 4,
                                        color: disabled ? '#ccc' : '#666'
                                    }}
                                >
                                    {description}
                                </Text>
                            </Tooltip>
                        )}
                    </div>
                </div>
            </Card>
        </Badge>
    );
};

export default AppCard;