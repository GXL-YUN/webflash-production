/**
 * 基础附件展示组件
 */


import React from 'react';
import { Card, Button, Space, Tag, Image, Modal, message } from 'antd';
import { 
  FileTextOutlined, FileImageOutlined, FilePdfOutlined, FileWordOutlined,
  FileExcelOutlined, FilePptOutlined, FileZipOutlined, FileUnknownOutlined,
  DownloadOutlined, EyeOutlined, DeleteOutlined
} from '@ant-design/icons';

interface Attachment {
  uid: string;
  name: string;
  size?: number;
  type?: string;
  url?: string;
  thumbUrl?: string;
  status?: 'uploading' | 'done' | 'error' | 'removed';
  percent?: number;
}

interface AttachmentDisplayProps {
  attachments: Attachment[];
  onPreview?: (file: Attachment) => void;
  onDownload?: (file: Attachment) => void;
  onRemove?: (file: Attachment) => void;
  showActions?: boolean;
  maxPreviewSize?: number; // 最大预览大小（MB）
}

const AttachmentDisplay: React.FC<AttachmentDisplayProps> = ({
  attachments,
  onPreview,
  onDownload,
  onRemove,
  showActions = true,
  maxPreviewSize = 5 // 默认5MB以内文件可预览
}) => {
  const [previewVisible, setPreviewVisible] = React.useState(false);
  const [previewImage, setPreviewImage] = React.useState('');

  // 获取文件图标
  const getFileIcon = (file: Attachment) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const type = file.type?.toLowerCase();
    
    if (type?.includes('image')) return <FileImageOutlined style={{ color: '#52c41a', fontSize: 24 }} />;
    if (type?.includes('pdf') || extension === 'pdf') return <FilePdfOutlined style={{ color: '#ff4d4f', fontSize: 24 }} />;
    if (type?.includes('word') || extension === 'doc' || extension === 'docx') 
      return <FileWordOutlined style={{ color: '#1890ff', fontSize: 24 }} />;
    if (type?.includes('excel') || extension === 'xls' || extension === 'xlsx') 
      return <FileExcelOutlined style={{ color: '#52c41a', fontSize: 24 }} />;
    if (type?.includes('powerpoint') || extension === 'ppt' || extension === 'pptx') 
      return <FilePptOutlined style={{ color: '#faad14', fontSize: 24 }} />;
    if (type?.includes('zip') || extension === 'zip' || extension === 'rar') 
      return <FileZipOutlined style={{ color: '#722ed1', fontSize: 24 }} />;
    
    return <FileUnknownOutlined style={{ color: '#bfbfbf', fontSize: 24 }} />;
  };

  // 获取文件类型标签
  const getFileType = (file: Attachment) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const typeMap: Record<string, string> = {
      pdf: 'PDF', doc: 'Word', docx: 'Word', xls: 'Excel', xlsx: 'Excel',
      ppt: 'PPT', pptx: 'PPT', jpg: '图片', jpeg: '图片', png: '图片',
      gif: '图片', txt: '文本', zip: '压缩包', rar: '压缩包'
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

  // 处理预览
  const handlePreview = (file: Attachment) => {
    if (onPreview) {
      onPreview(file);
      return;
    }

    // 默认预览逻辑
    if (file.type?.includes('image')) {
      // 图片预览
      if (file.size && file.size > maxPreviewSize * 1024 * 1024) {
        message.info('图片过大，建议下载后查看');
        handleDownload(file);
        return;
      }
      setPreviewImage(file.url || file.thumbUrl || '');
      setPreviewVisible(true);
    } else {
      // 非图片文件，直接下载或在新窗口打开
      handleDownload(file);
    }
  };

  // 处理下载
  const handleDownload = (file: Attachment) => {
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
  const handleRemove = (file: Attachment) => {
    if (onRemove) {
      onRemove(file);
    }
  };

  return (
    <div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 16 }}>
        {attachments.map((file) => (
          <Card
            key={file.uid}
            size="small"
            style={{ width: '100%' }}
            actions={showActions ? [
              <Button 
                type="text" 
                icon={<EyeOutlined />} 
                onClick={() => handlePreview(file)}
              >
                预览
              </Button>,
              <Button 
                type="text" 
                icon={<DownloadOutlined />} 
                onClick={() => handleDownload(file)}
              >
                下载
              </Button>,
              onRemove && (
                <Button 
                  type="text" 
                  danger 
                  icon={<DeleteOutlined />} 
                  onClick={() => handleRemove(file)}
                >
                  删除
                </Button>
              )
            ].filter(Boolean) : undefined}
          >
            <Card.Meta
              avatar={getFileIcon(file)}
              title={
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <span style={{ flex: 1, marginRight: 8 }} title={file.name}>
                    {file.name}
                  </span>
                  <Tag color="blue">{getFileType(file)}</Tag>
                </div>
              }
              description={
                <Space direction="vertical" size={0} style={{ width: '100%' }}>
                  <span>大小: {formatFileSize(file.size)}</span>
                  <span>类型: {file.type || '未知'}</span>
                  {file.status === 'uploading' && file.percent && (
                    <span>上传进度: {file.percent}%</span>
                  )}
                </Space>
              }
            />
          </Card>
        ))}
      </div>

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
    </div>
  );
};

export default AttachmentDisplay;