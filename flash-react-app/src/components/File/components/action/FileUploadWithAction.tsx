/**
 * 支持表单集成的上传组件
 */
import React, { useState, useEffect } from 'react';
import { Upload, Button, message, List, Progress, Space, Tag } from 'antd';
import { UploadOutlined, DeleteOutlined, DownloadOutlined, EyeOutlined } from '@ant-design/icons';
import type { UploadFile, UploadProps } from 'antd/es/upload/interface';

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
  fdKey?: string;
  response?: UploadResponse;
}

// 返回给表单的文件信息类型
interface FileInfo {
  fdId: string;
  fdKey: string;
  originalFileName: string;
  size: number;
  realFileName?: string;
  createTime?: string;
  fdModelId?: string;
  modifyBy?: number;
  createBy?: number;
  modifyTime?: string;
}

interface FileUploadWithActionProps {
  value?: FileInfo[]; // 接收表单的值（文件信息数组）
  onChange?: (value: FileInfo[]) => void; // 值变化回调
  maxCount?: number; // 最大文件数量
  accept?: string; // 可接受的文件类型
  action?: string; // 上传接口地址
  headers?: Record<string, string>; // 请求头
  disabled?: boolean; // 是否禁用
  withCredentials?: boolean; // 是否携带cookie
  fileKey?: string; // 文件key，重命名避免与React的key冲突
  defaultKey?: string; // 默认key值
}

const FileUploadWithAction: React.FC<FileUploadWithActionProps> = ({
                                                                     value = [],
                                                                     onChange,
                                                                     maxCount = 5,
                                                                     accept = '.jpg,.jpeg,.png,.pdf,.doc,.docx,.xls,.xlsx,.txt,.zip,.rar',
                                                                     action = '/date/fileUtil/upLoder',
                                                                     headers = {},
                                                                     disabled = false,
                                                                     withCredentials = false,
                                                                     fileKey = 'default_key', // 使用 fileKey 避免与React的key属性冲突
                                                                     defaultKey = 'default_fixed_key'
                                                                   }) => {
  const [fileList, setFileList] = useState<CustomUploadFile[]>([]);

  // 获取认证 token
  const getAuthToken = () => {
    return localStorage.getItem('token') || sessionStorage.getItem('token') || '';
  };

  // 同步表单值到文件列表
  useEffect(() => {
    if (value && value.length > 0) {
      const mappedFiles: CustomUploadFile[] = value.map((fileInfo, index) => ({
        uid: `existing-${index}-${fileInfo.fdId}`,
        name: fileInfo.originalFileName,
        status: 'done' as any,
        fdId: fileInfo.fdId,
        fdKey: fileInfo.fdKey || fileKey, // 优先使用已有的fdKey
        size: fileInfo.size,
        response: {
          code: 200,
          success: true,
          msg: '已上传',
          data: {
            fdId: fileInfo.fdId,
            fdKey: fileInfo.fdKey || fileKey,
            originalFileName: fileInfo.originalFileName,
            realFileName: fileInfo.realFileName || fileInfo.originalFileName,
            size: fileInfo.size,
            createTime: fileInfo.createTime || '',
            createBy: fileInfo.createBy || -1,
            modifyTime: fileInfo.modifyTime || '',
            modifyBy: fileInfo.modifyBy || -1,
            fdModelId: fileInfo.fdModelId || ''
          }
        } as UploadResponse
      }));

      setFileList(mappedFiles);
    }
  }, [value, fileKey]);

  // 更新表单值
  const updateFormValue = (files: CustomUploadFile[]) => {
    const fileInfoList: FileInfo[] = files
        .filter(file => file.status === 'done' && file.fdId)
        .map(file => {
          const responseData = file.response?.data;
          return {
            fdId: file.fdId as string,
            fdKey: file.fdKey || fileKey || defaultKey, // 使用优先级：文件已有的fdKey > props传入的fileKey > 默认值
            originalFileName: file.name || '',
            size: file.size || 0,
            realFileName: responseData?.realFileName || file.name || '',
            createTime: responseData?.createTime || new Date().toISOString(),
            createBy: responseData?.createBy || -1,
            modifyTime: responseData?.modifyTime || new Date().toISOString(),
            modifyBy: responseData?.modifyBy || -1,
            fdModelId: responseData?.fdModelId || ''
          };
        });

    onChange?.(fileInfoList);
  };

  // 自定义请求处理
  const customRequest = async (options: any) => {
    const { onProgress, onSuccess, onError, file, action: uploadUrl, data } = options;

    const formData = new FormData();
    formData.append('file', file);

    // 添加其他表单数据
    if (data) {
      Object.keys(data).forEach(key => {
        formData.append(key, data[key]);
      });
    }

    const xhr = new XMLHttpRequest();

    // 监听上传进度
    if (onProgress) {
      xhr.upload.onprogress = (event) => {
        if (event.lengthComputable) {
          const percent = Math.round((event.loaded / event.total) * 100);
          onProgress({ percent }, file);
        }
      };
    }

    xhr.onload = () => {
      if (xhr.status === 200) {
        try {
          const response = JSON.parse(xhr.responseText) as UploadResponse;
          if (response.code === 20000 || response.success || response.code === 200) {
            onSuccess(response, file);
          } else {
            onError(new Error(response.msg || '上传失败'), file);
          }
        } catch (error) {
          onError(error as Error, file);
        }
      } else {
        onError(new Error(`上传失败: ${xhr.statusText}`), file);
      }
    };

    xhr.onerror = () => {
      onError(new Error('网络错误'), file);
    };

    xhr.open('POST', uploadUrl || action, true);

    // 设置请求头
    const authToken = getAuthToken();
    const requestHeaders = {
      'Authorization': `Bearer ${authToken}`,
      ...headers
    };

    // Object.keys(requestHeaders).forEach(key => {
    //   if (requestHeaders[key] !== null) {
    //     xhr.setRequestHeader(key, requestHeaders[key]);
    //   }
    // });

    xhr.withCredentials = withCredentials;
    xhr.send(formData);
  };

  // 上传配置
  const uploadProps: UploadProps = {
    action,
    name: 'file',
    multiple: true,
    accept,
    maxCount,
    fileList: fileList as any[],
    showUploadList: false,
    disabled,
    headers: {
      'Authorization': `Bearer ${getAuthToken()}`,
      ...headers
    },
    withCredentials,
    onChange: (info) => {
      let newFileList = [...info.fileList] as CustomUploadFile[];

      // 限制文件大小 (10MB)
      newFileList = newFileList.map(file => {
        if (file.size && file.size > 10 * 1024 * 1024) {
          message.error(`${file.name} 文件大小不能超过10MB`);
          return {
            ...file,
            status: 'error' as any
          };
        }
        return file;
      });

      setFileList(newFileList);

      // 处理当前文件状态
      const { file } = info;
      if (file.status === 'done') {
        const response = file.response as UploadResponse;
        if (response?.code === 20000 || response?.success || response?.code === 200) {
          message.success(`${file.name} 上传成功`);

          // 更新文件列表，添加 fdId 和 fdKey
          const updatedFileList = newFileList.map(item =>
              item.uid === file.uid
                  ? {
                    ...item,
                    fdId: response.data?.fdId || `file_${Date.now()}`,
                    fdKey: fileKey || response.data?.fdKey || defaultKey, // 设置fdKey
                    status: 'done' as any,
                    response: response
                  }
                  : item
          );

          setFileList(updatedFileList);
          updateFormValue(updatedFileList);
        } else {
          message.error(`${file.name} 上传失败: ${response?.msg || '服务器错误'}`);
        }
      } else if (file.status === 'error') {
        message.error(`${file.name} 上传失败`);
      }
    },
    beforeUpload: (file) => {
      const fileType = file.type.toLowerCase();
      const fileExtension = file.name.split('.').pop()?.toLowerCase() || '';

      const allowedTypes = [
        'image/jpeg',
        'image/png',
        'image/jpg',
        'application/pdf',
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'application/vnd.ms-excel',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'text/plain',
        'application/zip',
        'application/x-rar-compressed'
      ];

      const allowedExtensions = accept.split(',').map(ext => ext.trim().replace('.', ''));

      const isAllowedByType = allowedTypes.includes(fileType);
      const isAllowedByExtension = allowedExtensions.includes(fileExtension) || allowedExtensions.includes('*');

      if (!isAllowedByType && !isAllowedByExtension) {
        message.error('不支持的文件类型! 请上传图片、文档、PDF、压缩包等格式文件。');
        return false;
      }

      // 检查文件数量
      if (fileList.length >= maxCount) {
        message.error(`最多只能上传 ${maxCount} 个文件`);
        return false;
      }

      return true;
    },
    customRequest // 使用自定义请求
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
    } else if (file.fdId) {
      const downloadUrl = `/date/fileUtil/download?fdId=${file.fdId}`;
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
    } else if (file.fdId) {
      const previewUrl = `/date/fileUtil/preview?fdId=${file.fdId}`;
      window.open(previewUrl, '_blank');
    } else {
      handleDownload(file);
    }
  };

  // 获取文件类型标签
  const getFileType = (file: CustomUploadFile) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const typeMap: Record<string, string> = {
      pdf: 'PDF',
      doc: 'Word',
      docx: 'Word',
      xls: 'Excel',
      xlsx: 'Excel',
      ppt: 'PPT',
      pptx: 'PPT',
      jpg: '图片',
      jpeg: '图片',
      png: '图片',
      gif: '图片',
      txt: '文本',
      zip: '压缩包',
      rar: '压缩包',
      mp4: '视频',
      mp3: '音频'
    };
    return typeMap[extension] || '文件';
  };

  // 获取文件图标颜色
  const getFileColor = (file: CustomUploadFile) => {
    const extension = file.name?.split('.').pop()?.toLowerCase() || '';
    const colorMap: Record<string, string> = {
      pdf: 'red',
      doc: 'blue',
      docx: 'blue',
      xls: 'green',
      xlsx: 'green',
      ppt: 'orange',
      pptx: 'orange',
      jpg: 'purple',
      jpeg: 'purple',
      png: 'purple',
      gif: 'cyan',
      txt: 'geekblue',
      zip: 'magenta',
      rar: 'volcano'
    };
    return colorMap[extension] || 'default';
  };

  // 格式化文件大小
  const formatFileSize = (bytes?: number) => {
    if (!bytes || bytes === 0) return '未知大小';
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
    return (bytes / (1024 * 1024 * 1024)).toFixed(1) + ' GB';
  };

  return (
      <div>
        <Upload {...uploadProps}>
          <Button
              icon={<UploadOutlined />}
              disabled={disabled || fileList.length >= maxCount}
          >
            选择文件 {fileList.length > 0 && `(${fileList.length}/${maxCount})`}
          </Button>
        </Upload>

        {fileList.length > 0 && (
            <List
                style={{ marginTop: 16 }}
                dataSource={fileList}
                renderItem={(file) => (
                    <List.Item
                        actions={[
                          <Button
                              type="link"
                              size="small"
                              icon={<EyeOutlined />}
                              onClick={() => handlePreview(file)}
                              disabled={file.status !== 'done'}
                          >
                            预览
                          </Button>,
                          <Button
                              type="link"
                              size="small"
                              icon={<DownloadOutlined />}
                              onClick={() => handleDownload(file)}
                              disabled={file.status !== 'done'}
                          >
                            下载
                          </Button>,
                          <Button
                              type="link"
                              size="small"
                              danger
                              icon={<DeleteOutlined />}
                              onClick={() => handleRemove(file)}
                              disabled={disabled}
                          >
                            删除
                          </Button>
                        ]}
                    >
                      <List.Item.Meta
                          avatar={
                            <Tag color={getFileColor(file)} style={{ minWidth: 50, textAlign: 'center' }}>
                              {getFileType(file)}
                            </Tag>
                          }
                          title={
                            <Space>
                    <span style={{ maxWidth: 300, overflow: 'hidden', textOverflow: 'ellipsis' }}>
                      {file.name}
                    </span>
                              {file.status === 'uploading' && (
                                  <Tag color="processing">上传中 {file.percent}%</Tag>
                              )}
                              {file.status === 'done' && (file.fdId || file.response?.data?.fdId) && (
                                  <Tag color="success">已上传</Tag>
                              )}
                              {file.status === 'error' && (
                                  <Tag color="error">上传失败</Tag>
                              )}
                            </Space>
                          }
                          description={
                            <Space direction="vertical" size={2} style={{ width: '100%' }}>
                              <Space size="middle">
                                <span>大小: {formatFileSize(file.size)}</span>
                                {file.type && <span>类型: {file.type}</span>}
                                {file.fdKey && <Tag color="blue">Key: {file.fdKey}</Tag>}
                              </Space>
                              {file.status === 'uploading' && file.percent !== undefined && (
                                  <Progress
                                      percent={Math.round(file.percent)}
                                      size="small"
                                      style={{ width: 200 }}
                                      status="active"
                                  />
                              )}
                              {(file.fdId || file.response?.data?.fdId) && (
                                  <span style={{ color: '#666', fontSize: '12px', fontFamily: 'monospace' }}>
                        文件ID: {file.fdId || file.response?.data?.fdId}
                      </span>
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