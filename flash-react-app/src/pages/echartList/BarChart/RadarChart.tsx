import React, { useRef, useEffect } from 'react';
import * as echarts from 'echarts';

const RadarChart = () => {
    const chartRef = useRef(null);

    useEffect(() => {
        // 初始化图表
        const myChart = echarts.init(chartRef.current);

        // 图表配置
        const option = {
            title: {
                text: 'Basic Radar Chart'
            },
            legend: {
                data: ['Allocated Budget', 'Actual Spending']
            },
            radar: {
                // shape: 'circle',
                indicator: [
                    { name: 'Sales', max: 6500 },
                    { name: 'Administration', max: 16000 },
                    { name: 'Information Technology', max: 30000 },
                    { name: 'Customer Support', max: 38000 },
                    { name: 'Development', max: 52000 },
                    { name: 'Marketing', max: 25000 }
                ]
            },
            series: [
                {
                    name: 'Budget vs spending',
                    type: 'radar',
                    label: {
                        show: true
                    },
                    data: [
                        {
                            value: [4200, 3000, 20000, 35000, 50000, 18000],
                            name: 'Allocated Budget'
                        },
                        {
                            value: [5000, 14000, 28000, 26000, 42000, 21000],
                            name: 'Actual Spending'
                        }
                    ]
                }
            ]
        };

        // 设置图表选项
        myChart.setOption(option);

        // 响应式处理
        const handleResize = () => {
            myChart.resize();
        };

        window.addEventListener('resize', handleResize);

        // 清理函数
        return () => {
            window.removeEventListener('resize', handleResize);
            myChart.dispose();
        };
    }, []);

    // 返回 JSX
    return (
        <div
            ref={chartRef}
            style={{
                width: '100%',
                height: '500px',
                minHeight: '400px'
            }}
        />
    );
};

export default RadarChart;