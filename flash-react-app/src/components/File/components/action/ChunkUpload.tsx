

/**
 * 分片上传
 */
import React from 'react';
import { Upload, Button, message, Progress } from 'antd';
import { UploadOutlined } from '@ant-design/icons';

const ChunkUpload: React.FC = () => {
  const CHUNK_SIZE = 2 * 1024 * 1024; // 2MB

  const customRequest = async (options: any) => {
    const { file, onProgress, onSuccess, onError } = options;
    
    try {
      const fileSize = file.size;
      const chunks = Math.ceil(fileSize / CHUNK_SIZE);
      const fileMd5 = await calculateFileMD5(file); // 计算文件MD5

      // 检查文件是否已存在
      const checkResult = await checkFileExists(fileMd5, file.name);
      if (checkResult.exists) {
        onSuccess(checkResult);
        return;
      }

      // 分片上传
      for (let chunkIndex = 0; chunkIndex < chunks; chunkIndex++) {
        const start = chunkIndex * CHUNK_SIZE;
        const end = Math.min(start + CHUNK_SIZE, fileSize);
        const chunk = file.slice(start, end);

        const formData = new FormData();
        formData.append('file', chunk);
        formData.append('chunkIndex', chunkIndex.toString());
        formData.append('totalChunks', chunks.toString());
        formData.append('fileMd5', fileMd5);
        formData.append('fileName', file.name);

        await uploadChunk(formData, onProgress, chunkIndex, chunks);
      }

      // 合并文件
      const mergeResult = await mergeChunks(fileMd5, file.name);
      onSuccess(mergeResult);
      
    } catch (error) {
      onError(error);
    }
  };

  return (
    <Upload
      customRequest={customRequest}
      onChange={(info) => {
        if (info.file.status === 'done') {
          message.success('上传成功');
        }
      }}
    >
      <Button icon={<UploadOutlined />}>分片上传（大文件）</Button>
    </Upload>
  );
};

// 辅助函数
const calculateFileMD5 = (file: File): Promise<string> => {
  return new Promise((resolve) => {
    // 简化实现，实际项目中使用 crypto-js 等库
    resolve('mock-md5-' + file.name + file.size);
  });
};

const checkFileExists = async (md5: string, fileName: string) => {
  const response = await fetch('/api/upload/check', {
    method: 'POST',
    body: JSON.stringify({ md5, fileName }),
    headers: { 'Content-Type': 'application/json' }
  });
  return response.json();
};

const uploadChunk = async (formData: FormData, onProgress: any, chunkIndex: number, totalChunks: number) => {
  await fetch('/api/upload/chunk', {
    method: 'POST',
    body: formData
  });
  
  const percent = Math.round(((chunkIndex + 1) / totalChunks) * 100);
  onProgress({ percent });
};

const mergeChunks = async (md5: string, fileName: string) => {
  const response = await fetch('/api/upload/merge', {
    method: 'POST',
    body: JSON.stringify({ md5, fileName }),
    headers: { 'Content-Type': 'application/json' }
  });
  return response.json();
};

export default ChunkUpload;