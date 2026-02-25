import React, { useState } from 'react';
import { Upload, Button, message, Image, Modal } from 'antd';
import { UploadOutlined, DeleteOutlined } from '@ant-design/icons';
import type { UploadFile } from 'antd/es/upload/interface';

interface ImageUploadDisplayProps {
  value?: UploadFile[];
  onChange?: (fileList: UploadFile[]) => void;
  maxCount?: number;
}

const ImageUploadDisplay: React.FC<ImageUploadDisplayProps> = ({ 
  value = [], 
  onChange, 
  maxCount = 5 
}) => {
  const [fileList, setFileList] = useState<UploadFile[]>(value || []);
  const [previewVisible, setPreviewVisible] = useState(false);
  const [previewImage, setPreviewImage] = useState('');

  const handleUpload = (info: any) => {
    const validFiles = info.fileList.filter((file: UploadFile) => {
      const isImage = file.type?.includes('image');
      if (!isImage) {
        message.error('只能上传图片文件');
        return false;
      }
      return true;
    });

    if (validFiles.length > maxCount) {
      message.error(`最多只能上传 ${maxCount} 张图片`);
      validFiles.splice(maxCount);
    }

    setFileList(validFiles);
    onChange?.(validFiles);
  };

  const handleRemove = (file: UploadFile) => {
    const newFileList = fileList.filter(item => item.uid !== file.uid);
    setFileList(newFileList);
    onChange?.(newFileList);
  };

  const handlePreview = async (file: UploadFile) => {
    if (file.url) {
      setPreviewImage(file.url);
    } else if (file.originFileObj) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setPreviewImage(e.target?.result as string);
      };
      reader.readAsDataURL(file.originFileObj as Blob);
    }
    setPreviewVisible(true);
  };

  return (
    <div>
      <Upload
        listType="picture-card"
        fileList={fileList}
        onChange={handleUpload}
        onPreview={handlePreview}
        beforeUpload={() => false}
        multiple
      >
        {fileList.length >= maxCount ? null : (
          <div>
            <UploadOutlined />
            <div style={{ marginTop: 8 }}>上传图片</div>
          </div>
        )}
      </Upload>

      <Modal open={previewVisible} footer={null} onCancel={() => setPreviewVisible(false)}>
        
      </Modal>

      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, marginTop: 16 }}>
        {fileList.map(file => (
          <div key={file.uid} style={{ position: 'relative' }}>
            <Image
              width={100}
              height={100}
              src={file.url || (file.originFileObj ? URL.createObjectURL(file.originFileObj as Blob) : '')}
              alt={file.name || '图片'}
              style={{ objectFit: 'cover' }}
            />
            <Button
              type="primary"
              danger
              size="small"
              icon={<DeleteOutlined />}
              style={{ position: 'absolute', top: 0, right: 0 }}
              onClick={() => handleRemove(file)}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default ImageUploadDisplay;