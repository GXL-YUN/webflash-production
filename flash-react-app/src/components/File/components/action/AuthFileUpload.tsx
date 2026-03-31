
/**
 * 带认证上传
 */

import React from 'react';
import { Upload, Button, message } from 'antd';
import { UploadOutlined } from '@ant-design/icons';

const AuthFileUpload: React.FC = () => {
  // 获取认证 token
  const getAuthToken = () => {
    return localStorage.getItem('token') || '';
  };

  const uploadProps = {
    action: '/date/fileUtil/upLoder',
    headers: {
      'Authorization': `Bearer ${getAuthToken()}`,
      //'X-Requested-With': null, // 解决某些框架的 CSRF 保护
    },
    name: 'file',
    onChange(info: any) {
      let newFileList = [...info.fileList];
      if (info.file.status === 'done') {
        const response = info.file.response;
        if (response.code === 200) {
          // 上传状态处理
          const { file } = info;
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


          message.success('上传成功');
        } else {
          message.error(response.message || '上传失败');
        }
      } else if (info.file.status === 'error') {
        message.error('上传失败');
      }
    },
    // 自定义请求（更灵活的控制）
    customRequest: (options: any) => {
      const { action, data, file, filename, headers, onError, onProgress, onSuccess } = options;

      const formData = new FormData();
      formData.append(filename || 'file', file);

      // 添加其他表单数据
      if (data) {
        Object.keys(data).forEach(key => {
          formData.append(key, data[key]);
        });
      }

      const xhr = new XMLHttpRequest();

      // 上传进度
      xhr.upload.onprogress = (event) => {
        if (event.lengthComputable) {
          onProgress({ percent: (event.loaded / event.total) * 100 }, file);
        }
      };

      xhr.onload = () => {
        if (xhr.status === 200) {
          const response = JSON.parse(xhr.responseText);
          onSuccess(response, file);
        } else {
          onError(new Error('上传失败'), file);
        }
      };

      xhr.onerror = () => {
        onError(new Error('网络错误'), file);
      };

      xhr.open('POST', action, true);

      // 设置请求头
      Object.keys(headers).forEach(key => {
        xhr.setRequestHeader(key, headers[key]);
      });

      xhr.send(formData);
    }
  };

  return (
    //  <Upload {...uploadProps}>
    //  {/*//<Upload >*/}
    //   <Button icon={<UploadOutlined />}>上传文件（带认证）</Button>
    // </Upload>


  <Upload {...uploadProps}>
    <Button icon={<UploadOutlined />}>选择文件</Button>
  </Upload>
  );
};

export default AuthFileUpload;