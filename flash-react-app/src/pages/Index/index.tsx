import React from 'react';
import {Card, Row, Col, Descriptions} from 'antd';
import AppLayout from '../../components/Layout';

const Index: React.FC = () => {
    return (

            <div>
                <h1>关于我们</h1>
                <Descriptions title="公司信息" bordered>
                    <Descriptions.Item label="公司名称">某某科技有限公司</Descriptions.Item>
                    <Descriptions.Item label="联系电话">138-8888-8888</Descriptions.Item>
                    <Descriptions.Item label="公司地址">北京市朝阳区</Descriptions.Item>
                    <Descriptions.Item label="成立时间">2020-01-01</Descriptions.Item>
                    <Descriptions.Item label="公司简介" span={3}>
                        我们是一家专注于企业级应用开发的科技公司，致力于为客户提供优质的软件解决方案。
                    </Descriptions.Item>
                </Descriptions>
            </div>

    );
};

export default Index;