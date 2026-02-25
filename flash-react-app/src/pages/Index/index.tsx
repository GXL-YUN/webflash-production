// pages/Dashboard.tsx
import React, { useRef, useEffect } from 'react';

import BarChart, { BarChartRef } from '../echartList/BarChart/BarChart';
import RadarChart from '../echartList/BarChart/RadarChart';
import LineChar from '../echartList/BarChart/LineChar';
import SearchLTest from '../../components/Search/SearchLTest';

const Dashboard: React.FC = () => {
  const chartRef = useRef<BarChartRef>(null);

  // 示例数据
  const salesData = {
    categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月'],
    values: [120, 200, 150, 80, 70, 110, 130]
  };

  const productData = {
    categories: ['产品A', '产品B', '产品C', '产品D', '产品E'],
    values: [450, 320, 280, 190, 150]
  };

  useEffect(() => {
    // 图表初始化后可以调用方法
    setTimeout(() => {
      chartRef.current?.resize();
    }, 1000);
  }, []);

  // 图表点击事件处理
  const handleChartClick = (params: any) => {
    console.log('图表点击:', params);
    alert(`点击了: ${params.name}, 数值: ${params.value}`);
  };

  return (
      <div style={{padding: '20px', maxWidth: '1200px', margin: '0 auto'}}>
          <h1>柱状图组件示例</h1>
          <div style={{marginBottom: '40px'}}>
              <h2>基础柱状图</h2>
              <BarChart
                  ref={chartRef}
                  data={salesData}
                  title="月度销售额"
                  color="#91cc75"
                  showLabel={true}
                  onEvents={{
                      click: handleChartClick
                  }}
                  style={{
                      border: '1px solid #f0f0f0',
                      borderRadius: '8px',
                      padding: '10px'
                  }}
              />
          </div>

          <div style={{marginBottom: '40px'}}>
              <h2>产品销量对比</h2>
              <BarChart
                  data={productData}
                  title="产品销量排行"
                  color="#fac858"
                  barWidth={40}
                  showGrid={true}
                  onEvents={{
                      click: (params) => {
                          console.log('产品点击:', params);
                      }
                  }}
              />
          </div>

          <div style={{marginBottom: '40px'}}>
              <h2>雷达图</h2>
              <RadarChart/>
          </div>
          <div style={{marginBottom: '40px'}}>
              <h2>折线图</h2>
              <LineChar/>
          </div>

          <div style={{marginBottom: '40px'}}>
              <h2>搜索</h2>
              <SearchLTest/>
          </div>

      </div>
  );
};
export default Dashboard;