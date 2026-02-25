import React, { useState } from 'react';
import { Upload, Button, message, List, Space, Tag } from 'antd';
import { UploadOutlined, DeleteOutlined, EyeOutlined, DownloadOutlined } from '@ant-design/icons';
import type { UploadFile, RcFile } from 'antd/es/upload/interface';

// 直接使用 Ant Design 的 UploadFile 类型，不要扩展
interface FileUploadDisplayProps {
  value?: UploadFile[];
  onChange?: (fileList: UploadFile[]) => void;
  maxCount?: number;
  maxSize?: number;
  key?:string

}

const FileUploadDisplay: React.FC<FileUploadDisplayProps> = ({ 
  value = [], 
  onChange, 
  maxCount = 5, 
  maxSize = 10 ,
  key
}) => {
  const [fileList, setFileList] = useState<UploadFile[]>(value);
const [action_url, setaction_url] = useState<string>("/date/fileUtil/upLoder")

  // 处理文件上传
  const handleUpload = (info: any) => {
    debugger
    let newFileList = [...info.fileList];
    
    // 限制文件大小
    newFileList = newFileList.map(file => {
      if (file.size && file.size > maxSize * 1024 * 1024) {
        message.error(`文件 ${file.name} 大小不能超过 ${maxSize}MB`);
        return null;
      }
      return file;
    }).filter(Boolean);

    // 限制文件数量
    if (newFileList.length > maxCount) {
      message.error(`最多只能上传 ${maxCount} 个文件`);
      newFileList = newFileList.slice(0, maxCount);
    }

    setFileList(newFileList);

    //上传附件
    onChange?.(newFileList);
  };

  // 删除文件
  const handleRemove = (file: UploadFile) => {
    const newFileList = fileList.filter(item => item.uid !== file.uid);
    setFileList(newFileList);
    onChange?.(newFileList);
  };

  // 下载文件
  const handleDownload = (file: UploadFile) => {
    if (file.url) {
      window.open(file.url, '_blank');
    } else if (file.originFileObj) {
      // 安全地处理 originFileObj
      const url = URL.createObjectURL(file.originFileObj as Blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = file.name || 'download';
      a.click();
      URL.revokeObjectURL(url);
    }
  };

  // 预览文件
  const handlePreview = (file: UploadFile) => {
    if (file.type?.includes('image')) {
      // 图片预览
      if (file.originFileObj) {
        const img = document.createElement('img');
        img.src = file.url || URL.createObjectURL(file.originFileObj as Blob);
        img.onload = () => {
          console.log('图片加载完成');
        };
      }
    } else {
      // 其他文件类型，直接下载
      handleDownload(file);
    }
  };

  // 获取文件类型标签
  const getFileType = (file: UploadFile) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const typeMap: Record<string, string> = {
      pdf: 'PDF', doc: 'Word', docx: 'Word', xls: 'Excel', xlsx: 'Excel',
      ppt: 'PPT', pptx: 'PPT', jpg: '图片', jpeg: '图片', png: '图片',
      gif: '图片', txt: '文本', zip: '压缩包', rar: '压缩包'
    };
    return typeMap[extension] || '文件';
  };

  // 获取文件图标颜色
  const getFileColor = (file: UploadFile) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const colorMap: Record<string, string> = {
      pdf: 'red', doc: 'blue', docx: 'blue', xls: 'green', xlsx: 'green',
      ppt: 'orange', pptx: 'orange', jpg: 'purple', jpeg: 'purple', 
      png: 'purple', gif: 'cyan'
    };
    return colorMap[extension] || 'default';
  };

  return (
    <div>
      <Upload
        fileList={fileList}
        action={action_url}
        onChange={handleUpload}
        beforeUpload={() => false}
        multiple
        showUploadList={false}
      >
        <Button icon={<UploadOutlined />}>选择文件</Button>
        <span style={{ marginLeft: 8, color: '#999' }}>
          最多 {maxCount} 个文件，单个文件不超过 {maxSize}MB
        </span>
      </Upload>

      {fileList.length > 0 && (
        <List
          style={{ marginTop: 16 }}
          dataSource={fileList}
          renderItem={(file) => (
            <List.Item
              actions={[
                <Button type="text" icon={<EyeOutlined />} onClick={() => handlePreview(file)}>
                  预览
                </Button>,
                <Button type="text" icon={<DownloadOutlined />} onClick={() => handleDownload(file)}>
                  下载
                </Button>,
                <Button type="text" danger icon={<DeleteOutlined />} onClick={() => handleRemove(file)}>
                  删除
                </Button>
              ]}
            >
              <List.Item.Meta
                avatar={<Tag color={getFileColor(file)}>{getFileType(file)}</Tag>}
                title={file.name}
                description={
                  <Space>
                    <span>大小: {file.size ? `${(file.size / 1024 / 1024).toFixed(2)}MB` : '未知'}</span>
                    <span>类型: {file.type || '未知'}</span>
                  </Space>
                }
              />
            </List.Item>
          )}
        />
      )}
    </div>
  );
};

export default FileUploadDisplay;