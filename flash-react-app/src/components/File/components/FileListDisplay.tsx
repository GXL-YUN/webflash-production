// src/components/File/FileListDisplay.tsx

/**
 * 列表组件
 */
import React from 'react';
import { List, Button, Tag, Space, Progress, Image, Modal, message } from 'antd';
import { 
  DownloadOutlined, 
  EyeOutlined, 
  DeleteOutlined,
  FileTextOutlined,
  FileImageOutlined,
  FilePdfOutlined,
  FileZipOutlined,
  FileExcelOutlined,
  FileWordOutlined,
  FilePptOutlined
} from '@ant-design/icons';

export interface FileItem {
  uid: string;
  name: string;
  size?: number;
  type?: string;
  url?: string;
  thumbUrl?: string;
  status?: 'uploading' | 'done' | 'error' | 'removed';
  percent?: number;
  createTime?: string;
}

interface FileListDisplayProps {
  files: FileItem[];
  onPreview?: (file: FileItem) => void;
  onDownload?: (file: FileItem) => void;
  onRemove?: (file: FileItem) => void;
  showActions?: boolean;
  showProgress?: boolean;
  bordered?: boolean;
  size?: 'small' | 'default' | 'large';
}

const FileListDisplay: React.FC<FileListDisplayProps> = ({
  files,
  onPreview,
  onDownload,
  onRemove,
  showActions = true,
  showProgress = true,
  bordered = false,
  size = 'default'
}) => {
  const [previewVisible, setPreviewVisible] = React.useState(false);
  const [previewImage, setPreviewImage] = React.useState('');

  // 获取文件图标
  const getFileIcon = (file: FileItem) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const type = file.type?.toLowerCase();
    
    if (type?.includes('image')) return <FileImageOutlined style={{ color: '#52c41a' }} />;
    if (type?.includes('pdf') || extension === 'pdf') return <FilePdfOutlined style={{ color: '#ff4d4f' }} />;
    if (type?.includes('word') || extension === 'doc' || extension === 'docx') 
      return <FileWordOutlined style={{ color: '#1890ff' }} />;
    if (type?.includes('excel') || extension === 'xls' || extension === 'xlsx') 
      return <FileExcelOutlined style={{ color: '#52c41a' }} />;
    if (type?.includes('powerpoint') || extension === 'ppt' || extension === 'pptx') 
      return <FilePptOutlined style={{ color: '#faad14' }} />;
    if (type?.includes('zip') || extension === 'zip' || extension === 'rar') 
      return <FileZipOutlined style={{ color: '#722ed1' }} />;
    
    return <FileTextOutlined style={{ color: '#bfbfbf' }} />;
  };

  // 获取文件类型标签
  const getFileType = (file: FileItem) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const typeMap: Record<string, string> = {
      pdf: 'PDF', doc: 'Word', docx: 'Word', xls: 'Excel', xlsx: 'Excel',
      ppt: 'PPT', pptx: 'PPT', jpg: '图片', jpeg: '图片', png: '图片',
      gif: '图片', txt: '文本', zip: '压缩包', rar: '压缩包', 
      mp4: '视频', avi: '视频', mov: '视频'
    };
    return typeMap[extension] || '文件';
  };

  // 格式化文件大小
  const formatFileSize = (bytes?: number) => {
    if (!bytes) return '未知';
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  };

  // 格式化时间
  const formatTime = (timeStr?: string) => {
    if (!timeStr) return '';
    return new Date(timeStr).toLocaleString('zh-CN');
  };

  // 处理预览
  const handlePreview = (file: FileItem) => {
    if (onPreview) {
      onPreview(file);
      return;
    }

    // 默认预览逻辑
    if (file.type?.includes('image')) {
      setPreviewImage(file.url || file.thumbUrl || '');
      setPreviewVisible(true);
    } else {
      handleDownload(file);
    }
  };

  // 处理下载
  const handleDownload = (file: FileItem) => {
    if (onDownload) {
      onDownload(file);
      return;
    }

    // 默认下载逻辑
    if (file.url) {
      window.open(file.url, '_blank');
    } else {
      message.info('文件链接不可用');
    }
  };

  // 处理删除
  const handleRemove = (file: FileItem) => {
    if (onRemove) {
      onRemove(file);
    }
  };

  // 获取状态标签颜色
  const getStatusColor = (status?: string) => {
    const colorMap: Record<string, string> = {
      uploading: 'blue',
      done: 'green',
      error: 'red',
      removed: 'default'
    };
    return colorMap[status || 'done'] || 'default';
  };

  return (
    <>
      <List
        size={size}
        bordered={bordered}
        dataSource={files}
        locale={{ emptyText: '暂无文件' }}
        renderItem={(file) => (
          <List.Item
            actions={showActions ? [
              <Button 
                type="link" 
                icon={<EyeOutlined />} 
                onClick={() => handlePreview(file)}
                disabled={file.status === 'uploading'}
              >
                预览
              </Button>,
              <Button 
                type="link" 
                icon={<DownloadOutlined />} 
                onClick={() => handleDownload(file)}
                disabled={file.status === 'uploading'}
              >
                下载
              </Button>,
              onRemove && (
                <Button 
                  type="link" 
                  danger 
                  icon={<DeleteOutlined />} 
                  onClick={() => handleRemove(file)}
                  disabled={file.status === 'uploading'}
                >
                  删除
                </Button>
              )
            ].filter(Boolean) : undefined}
          >
            <List.Item.Meta
              avatar={getFileIcon(file)}
              title={
                <Space>
                  <span title={file.name}>{file.name}</span>
                  {file.status && (
                    <Tag color={getStatusColor(file.status)}>
                      {file.status === 'uploading' ? '上传中' : 
                       file.status === 'done' ? '已完成' : 
                       file.status === 'error' ? '上传失败' : '已删除'}
                    </Tag>
                  )}
                  <Tag color="blue">{getFileType(file)}</Tag>
                </Space>
              }
              description={
                <Space direction="vertical" size={0}>
                  <Space>
                    <span>大小: {formatFileSize(file.size)}</span>
                    <span>类型: {file.type || '未知'}</span>
                  </Space>
                  {file.createTime && (
                    <span style={{ color: '#999', fontSize: '12px' }}>
                      上传时间: {formatTime(file.createTime)}
                    </span>
                  )}
                  {showProgress && file.status === 'uploading' && file.percent !== undefined && (
                    <Progress 
                      percent={file.percent} 
                      size="small" 
                      style={{ width: 200, margin: '4px 0' }}
                      status={file.percent === 100 ? 'success' : 'active'}
                    />
                  )}
                </Space>
              }
            />
          </List.Item>
        )}
      />

      {/* 图片预览模态框 */}
      <Modal
        open={previewVisible}
        footer={null}
        onCancel={() => setPreviewVisible(false)}
        width="80vw"
        style={{ top: 20 }}
      >
        <Image
          src={previewImage}
          style={{ width: '100%', maxHeight: '80vh', objectFit: 'contain' }}
          alt="预览"
        />
      </Modal>
    </>
  );
};

export default FileListDisplay;