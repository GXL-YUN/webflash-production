// src/components/File/FileListDemo.tsx
/**
 * 文件列表展示演示组件
 */
import React, { useState } from 'react';
import { Card, Button, Space, Switch, Radio, message } from 'antd';
import { ReloadOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons';
import FileListDisplay, { FileItem } from './components/FileListDisplay';

const FileListDemo: React.FC = () => {
  const [files, setFiles] = useState<FileItem[]>([
    {
      uid: '1',
      name: '项目需求文档.pdf',
      size: 2048576,
      type: 'application/pdf',
      url: '/files/requirements.pdf',
      createTime: '2024-01-15T10:30:00Z',
      status: 'done'
    },
    {
      uid: '2',
      name: '产品设计图.jpg',
      size: 1024576,
      type: 'image/jpeg',
      url: '/images/design.jpg',
      thumbUrl: '/images/design-thumb.jpg',
      createTime: '2024-01-16T14:20:00Z',
      status: 'done'
    },
    {
      uid: '3',
      name: '用户数据.xlsx',
      size: 512345,
      type: 'application/vnd.ms-excel',
      url: '/files/user-data.xlsx',
      createTime: '2024-01-17T09:15:00Z',
      status: 'done'
    },
    {
      uid: '4',
      name: '演示文稿.pptx',
      size: 3567890,
      type: 'application/vnd.ms-powerpoint',
      url: '/files/presentation.pptx',
      createTime: '2024-01-18T16:45:00Z',
      status: 'done'
    },
    {
      uid: '5',
      name: '正在上传的大文件.zip',
      size: 10485760,
      type: 'application/zip',
      status: 'uploading',
      percent: 65
    }
  ]);

  const [showActions, setShowActions] = useState(true);
  const [showProgress, setShowProgress] = useState(true);
  const [bordered, setBordered] = useState(false);
  const [size, setSize] = useState<'small' | 'default' | 'large'>('default');

  // 处理预览
  const handlePreview = (file: FileItem) => {
    message.info(`预览文件: ${file.name}`);
    console.log('预览文件:', file);
  };

  // 处理下载
  const handleDownload = (file: FileItem) => {
    message.success(`开始下载: ${file.name}`);
    console.log('下载文件:', file);
  };

  // 处理删除
  const handleRemove = (file: FileItem) => {
    setFiles(files.filter(f => f.uid !== file.uid));
    message.success(`已删除: ${file.name}`);
  };

  // 添加测试文件
  const addTestFile = () => {
    const newFile: FileItem = {
      uid: Date.now().toString(),
      name: `测试文件-${Date.now()}.txt`,
      size: Math.floor(Math.random() * 1000000),
      type: 'text/plain',
      url: '/files/test.txt',
      createTime: new Date().toISOString(),
      status: 'done'
    };
    setFiles([...files, newFile]);
  };

  // 模拟上传进度
  const simulateUpload = () => {
    const uploadingFile = files.find(f => f.status === 'uploading');
    if (uploadingFile) {
      setFiles(files.map(f => 
        f.uid === uploadingFile.uid 
          ? { ...f, percent: Math.min((f.percent || 0) + 10, 100) }
          : f
      ));
    }
  };

  // 重置文件列表
  const resetFiles = () => {
    setFiles([
      {
        uid: '1',
        name: '项目需求文档.pdf',
        size: 2048576,
        type: 'application/pdf',
        url: '/files/requirements.pdf',
        createTime: '2024-01-15T10:30:00Z',
        status: 'done'
      },
      {
        uid: '2',
        name: '产品设计图.jpg',
        size: 1024576,
        type: 'image/jpeg',
        url: '/images/design.jpg',
        thumbUrl: '/images/design-thumb.jpg',
        createTime: '2024-01-16T14:20:00Z',
        status: 'done'
      },
      {
        uid: '3',
        name: '用户数据.xlsx',
        size: 512345,
        type: 'application/vnd.ms-excel',
        url: '/files/user-data.xlsx',
        createTime: '2024-01-17T09:15:00Z',
        status: 'done'
      }
    ]);
  };

  return (
    <Card 
      title="文件列表展示演示" 
      style={{ margin: 16 }}
      extra={
        <Space>
          <Button icon={<PlusOutlined />} onClick={addTestFile}>
            添加测试文件
          </Button>
          <Button icon={<ReloadOutlined />} onClick={resetFiles}>
            重置
          </Button>
          <Button onClick={simulateUpload}>
            模拟上传进度
          </Button>
        </Space>
      }
    >
      {/* 控制面板 */}
      <div style={{ marginBottom: 16, padding: '16px', background: '#f5f5f5', borderRadius: 6 }}>
        <Space wrap>
          <span>显示操作按钮:</span>
          <Switch checked={showActions} onChange={setShowActions} />
          
          <span>显示上传进度:</span>
          <Switch checked={showProgress} onChange={setShowProgress} />
          
          <span>显示边框:</span>
          <Switch checked={bordered} onChange={setBordered} />
          
          <span>列表尺寸:</span>
          <Radio.Group value={size} onChange={(e) => setSize(e.target.value)}>
            <Radio.Button value="small">小</Radio.Button>
            <Radio.Button value="default">默认</Radio.Button>
            <Radio.Button value="large">大</Radio.Button>
          </Radio.Group>
        </Space>
      </div>

      {/* 文件列表展示 */}
      <FileListDisplay
        files={files}
        onPreview={handlePreview}
        onDownload={handleDownload}
        onRemove={handleRemove}
        showActions={showActions}
        showProgress={showProgress}
        bordered={bordered}
        size={size}
      />

      {/* 统计信息 */}
      <div style={{ marginTop: 16, textAlign: 'right', color: '#666' }}>
        共 {files.length} 个文件，总大小: {formatTotalSize(files)}
      </div>
    </Card>
  );
};

// 计算总文件大小
const formatTotalSize = (files: FileItem[]) => {
  const totalBytes = files.reduce((sum, file) => sum + (file.size || 0), 0);
  if (totalBytes < 1024) return totalBytes + ' B';
  if (totalBytes < 1024 * 1024) return (totalBytes / 1024).toFixed(1) + ' KB';
  return (totalBytes / (1024 * 1024)).toFixed(1) + ' MB';
};

export default FileListDemo;