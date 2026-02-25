// components/BarChart.tsx
import React, { useRef, useImperativeHandle, forwardRef } from 'react';
import ReactECharts from 'echarts-for-react';
import { ECharts, EChartsOption } from 'echarts';

// 定义组件 Props 接口
export interface BarChartProps {
  /** 图表数据 */
  data: {
    categories: string[];
    values: number[];
  };
  /** 图表标题 */
  title?: string;
  /** 自定义样式 */
  style?: React.CSSProperties;
  /** 类名 */
  className?: string;
  /** 是否显示加载状态 */
  loading?: boolean;
  /** 柱状图颜色 */
  color?: string;
  /** 是否显示网格线 */
  showGrid?: boolean;
  /** 是否显示标签 */
  showLabel?: boolean;
  /** 柱条宽度 */
  barWidth?: number | string;
  /** 图表事件 */
  onEvents?: Record<string, (params: any) => void>;
}

// 定义组件 Ref 接口
export interface BarChartRef {
  /** 获取 ECharts 实例 */
  getInstance: () => ECharts | null;
  /** 重新调整图表尺寸 */
  resize: () => void;
}

const BarChart = forwardRef<BarChartRef, BarChartProps>((props, ref) => {
  const chartRef = useRef<any>(null);
  
  // 使用 useImperativeHandle 暴露方法给父组件
  useImperativeHandle(ref, () => ({
    getInstance: (): ECharts | null => {
      return chartRef.current?.getEchartsInstance() || null;
    },
    resize: (): void => {
      chartRef.current?.getEchartsInstance()?.resize();
    }
  }));

  // 生成图表配置
  const getOption = (): EChartsOption => {
    const { data, title, color, showGrid, showLabel, barWidth } = props;
    
    return {
      title: {
        text: title,
        left: 'center',
        textStyle: {
          fontSize: 16,
          fontWeight: 'bold'
        }
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: showGrid ? {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: title ? '15%' : '3%',
        containLabel: true
      } : undefined,
      xAxis: {
        type: 'category',
        data: data.categories,
        axisLabel: {
          rotate: data.categories.length > 6 ? 45 : 0, // 标签过多时旋转
          interval: 0 // 显示所有标签
        }
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '数值',
          type: 'bar',
          data: data.values,
          itemStyle: {
            color: color || '#5470c6'
          },
          barWidth: barWidth || '60%',
          label: showLabel ? {
            show: true,
            position: 'top',
            formatter: '{c}'
          } : undefined,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
  };

  return (
    <ReactECharts
      ref={chartRef}
      option={getOption()}
      style={props.style || { width: '100%', height: '400px' }}
      className={props.className}
      opts={{ renderer: 'canvas' }}
      onEvents={props.onEvents}
      showLoading={props.loading}
      loadingOption={{
        text: '加载中...',
        color: '#1890ff',
        textColor: '#000',
        maskColor: 'rgba(255, 255, 255, 0.8)'
      }}
    />
  );
});

BarChart.displayName = 'BarChart';
export default BarChart;