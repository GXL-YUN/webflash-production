import React, { useState } from 'react';
import { Card, message } from 'antd';
import AttachmentDisplay from './components/AttachmentDisplay';


interface config {
  list?: dateType[];
}
interface dateType {
  uid?: string;
  name?: string;
  size?: string;
  type?: string;
  url?: string;

}


const AttachmentDemo: React.FC<config> = (config) => {
  // 默认数据
  const [attachments, setAttachments] = useState([
    {
      uid: '1',
      name: '项目报告.pdf',
      size: 2048576,
      type: 'application/pdf',
      url: '/files/report.pdf',
    },
    {
      uid: '2',
      name: '产品图片.jpg',
      size: 1024576,
      type: 'image/jpeg',
      url: '/images/product.jpg',
      thumbUrl: '/images/product-thumb.jpg',
    },
    {
      uid: '3',
      name: '数据表格.xlsx',
      size: 512345,
      type: 'application/vnd.ms-excel',
      url: '/files/data.xlsx',
    },
  ]);
  debugger
  const handlePreview = (file: any) => {
    console.log('预览文件:', file);
    message.info(`预览文件: ${file.name}`);
  };

  const handleDownload = (file: any) => {
    console.log('下载文件:', file);
    // 实际下载逻辑
    message.success(`开始下载: ${file.name}`);
  };

  const handleRemove = (file: any) => {
    setAttachments(attachments.filter(att => att.uid !== file.uid));
    message.success(`已删除: ${file.name}`);
  };

  return (
    <Card title="附件展示" style={{ margin: 16 }}>
      <AttachmentDisplay
        attachments={attachments}
        onPreview={handlePreview}
        onDownload={handleDownload}
        onRemove={handleRemove}
        showActions={true}
        maxPreviewSize={5}
      />
    </Card>
  );
};

export default AttachmentDemo;