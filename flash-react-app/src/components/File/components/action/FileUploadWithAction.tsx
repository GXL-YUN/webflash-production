/**
 * 支持表单集成的上传组件
 */
import React, { useState, useEffect } from 'react';
import { Upload, Button, message, List, Progress, Space, Tag } from 'antd';
import { UploadOutlined, DeleteOutlined, DownloadOutlined, EyeOutlined } from '@ant-design/icons';
import type { UploadFile } from 'antd/es/upload/interface';

// 定义接口返回的数据类型
interface UploadResponse {
  code: number;
  msg: string;
  data: {
    fdId: string;
    createTime: string;
    createBy: number;
    modifyTime: string;
    modifyBy: number;
    originalFileName: string;
    realFileName: string;
    fdModelId: string;
    fdKey: string;
    size: number;
  };
  success: boolean;
}

// 扩展 UploadFile 类型
interface CustomUploadFile extends UploadFile {
  fdId?: string;
  response?: UploadResponse;
}

interface FileUploadWithActionProps {
  value?: string[]; // 接收表单的值（fdId 数组）
  onChange?: (value: string[]) => void; // 值变化回调
}

const FileUploadWithAction: React.FC<FileUploadWithActionProps> = ({ 
  value = [], 
  onChange 
}) => {
  const [fileList, setFileList] = useState<CustomUploadFile[]>([]);

  // 同步表单值到文件列表
  useEffect(() => {
    // 如果外部值变化，同步到内部状态
    // 这里可以根据需要实现从 fdId 数组到文件列表的映射
  }, [value]);

  // 更新表单值
  const updateFormValue = (files: CustomUploadFile[]) => {
    const fdIds = files
      .filter(file => file.status === 'done' && file.fdId)
      .map(file => file.fdId as string);
    
    onChange?.(fdIds);
  };

  // 上传配置
  const uploadProps = {
    action: '/date/fileUtil/upLoder',
    name: 'file',
    multiple: true,
    accept: '.jpg,.jpeg,.png,.pdf,.doc,.docx,.xls,.xlsx',
    maxCount: 5,
    fileList,
    showUploadList: false,
    onChange: (info: any) => {
      let newFileList = [...info.fileList];

      // 限制文件大小 (10MB)
      newFileList = newFileList.map(file => {
        if (file.size && file.size > 10 * 1024 * 1024) {
          message.error(`${file.name} 文件大小不能超过10MB`);
          return {
            ...file,
            status: 'error'
          };
        }
        return file;
      });

      setFileList(newFileList);

      // 上传状态处理
      const { file } = info;
      if (file.status === 'done') {
        const response: UploadResponse = file.response;
        if (response.code === 20000) {
          message.success(`${file.name} 上传成功`);
          
          // 更新文件列表，添加 fdId
          const updatedFileList = newFileList.map(item => 
            item.uid === file.uid 
              ? { 
                  ...item, 
                  fdId: response.data.fdId,
                  status: 'done',
                  response: response
                } 
              : item
          );
          
          setFileList(updatedFileList);
          updateFormValue(updatedFileList);
          
          console.log('文件fdId:', response.data.fdId);
        } else {
          message.error(`${file.name} 上传失败: ${response.msg}`);
        }
      } else if (file.status === 'error') {
        message.error(`${file.name} 上传失败`);
      }
    },
    beforeUpload: (file: File) => {
      const isAllowedType = [
        'image/jpeg',
        'image/png',
        'application/pdf',
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
      ].includes(file.type);

      if (!isAllowedType) {
        message.error('只能上传 JPG, PNG, PDF, Word 文件!');
        return false;
      }
      return true;
    }
  };

  // 删除文件
  const handleRemove = (file: CustomUploadFile) => {
    const newFileList = fileList.filter(item => item.uid !== file.uid);
    setFileList(newFileList);
    updateFormValue(newFileList);
    message.success(`已删除: ${file.name}`);
  };

  // 下载文件
  const handleDownload = (file: CustomUploadFile) => {
    if (file.response?.data?.fdId) {
      const downloadUrl = `/date/fileUtil/download?fdId=${file.response.data.fdId}`;
      window.open(downloadUrl, '_blank');
    } else {
      message.info('文件链接不可用');
    }
  };

  // 预览文件
  const handlePreview = (file: CustomUploadFile) => {
    if (file.response?.data?.fdId) {
      const previewUrl = `/date/fileUtil/preview?fdId=${file.response.data.fdId}`;
      window.open(previewUrl, '_blank');
    } else {
      handleDownload(file);
    }
  };

  // 获取文件类型标签
  const getFileType = (file: CustomUploadFile) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const typeMap: Record<string, string> = {
      pdf: 'PDF', doc: 'Word', docx: 'Word', xls: 'Excel', xlsx: 'Excel',
      ppt: 'PPT', pptx: 'PPT', jpg: '图片', jpeg: '图片', png: '图片',
      gif: '图片', txt: '文本', zip: '压缩包', rar: '压缩包'
    };
    return typeMap[extension] || '文件';
  };

  // 获取文件图标颜色
  const getFileColor = (file: CustomUploadFile) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const colorMap: Record<string, string> = {
      pdf: 'red', doc: 'blue', docx: 'blue', xls: 'green', xlsx: 'green',
      ppt: 'orange', pptx: 'orange', jpg: 'purple', jpeg: 'purple', 
      png: 'purple', gif: 'cyan'
    };
    return colorMap[extension] || 'default';
  };

  // 格式化文件大小
  const formatFileSize = (bytes?: number) => {
    if (!bytes) return '未知';
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  };

  return (
    <div>
      <Upload {...uploadProps}>
        <Button icon={<UploadOutlined />}>选择文件</Button>
      </Upload>

      {fileList.length > 0 && (
        <List
          style={{ marginTop: 16 }}
          dataSource={fileList}
          renderItem={(file) => (
            <List.Item
              actions={[
                <Button 
                  type="text" 
                  icon={<EyeOutlined />} 
                  onClick={() => handlePreview(file)}
                  disabled={file.status !== 'done'}
                >
                  预览
                </Button>,
                <Button 
                  type="text" 
                  icon={<DownloadOutlined />} 
                  onClick={() => handleDownload(file)}
                  disabled={file.status !== 'done'}
                >
                  下载
                </Button>,
                <Button 
                  type="text" 
                  danger 
                  icon={<DeleteOutlined />} 
                  onClick={() => handleRemove(file)}
                >
                  删除
                </Button>
              ]}
            >
              <List.Item.Meta
                avatar={
                  <Tag color={getFileColor(file)}>
                    {getFileType(file)}
                  </Tag>
                }
                title={
                  <Space>
                    <span>{file.name}</span>
                    {file.status === 'uploading' && (
                      <Tag color="blue">上传中...</Tag>
                    )}
                    {file.status === 'done' && file.fdId && (
                      <Tag color="green">已上传</Tag>
                    )}
                    {file.status === 'error' && (
                      <Tag color="red">上传失败</Tag>
                    )}
                  </Space>
                }
                description={
                  <Space direction="vertical" size={0}>
                    <Space>
                      <span>大小: {formatFileSize(file.size)}</span>
                      <span>类型: {file.type || '未知'}</span>
                    </Space>
                    {file.fdId && (
                      <span style={{ color: '#666', fontSize: '12px' }}>
                        文件ID: {file.fdId}
                      </span>
                    )}
                    {file.status === 'uploading' && file.percent !== undefined && (
                      <Progress 
                        percent={file.percent} 
                        size="small" 
                        style={{ width: 200, margin: '4px 0' }}
                      />
                    )}
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

export default FileUploadWithAction;