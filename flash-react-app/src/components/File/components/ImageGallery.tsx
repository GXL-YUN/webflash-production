
/**
 * 图片画廊展示组件
 */

import React, { useState } from 'react';
import { Image, Button, Space } from 'antd';
import { DeleteOutlined, DownloadOutlined } from '@ant-design/icons';

interface ImageGalleryProps {
  images: Array<{
    uid: string;
    url: string;
    name?: string;
    thumbUrl?: string;
  }>;
  onRemove?: (image: any) => void;
  onDownload?: (image: any) => void;
  maxHeight?: number;
}

const ImageGallery: React.FC<ImageGalleryProps> = ({
  images,
  onRemove,
  onDownload,
  maxHeight = 200
}) => {
  const [previewVisible, setPreviewVisible] = useState(false);

  return (
    <Image.PreviewGroup
      preview={{
        visible: previewVisible,
        onVisibleChange: (visible) => setPreviewVisible(visible),
      }}
    >
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
        {images.map((image) => (
          <div key={image.uid} style={{ position: 'relative' }}>
            <Image
              width={150}
              height={maxHeight}
              src={image.url || image.thumbUrl}
              alt={image.name}
              style={{ 
                objectFit: 'cover',
                borderRadius: 6,
                cursor: 'pointer'
              }}
              preview={false}
              onClick={() => setPreviewVisible(true)}
            />
            
            {/* 操作按钮 */}
            <Space 
              style={{ 
                position: 'absolute', 
                top: 8, 
                right: 8,
                opacity: 0,
                transition: 'opacity 0.3s'
              }}
              className="image-actions"
            >
              {onDownload && (
                <Button
                  type="primary"
                  size="small"
                  icon={<DownloadOutlined />}
                  onClick={(e) => {
                    e.stopPropagation();
                    onDownload(image);
                  }}
                />
              )}
              {onRemove && (
                <Button
                  type="primary"
                  danger
                  size="small"
                  icon={<DeleteOutlined />}
                  onClick={(e) => {
                    e.stopPropagation();
                    onRemove(image);
                  }}
                />
              )}
            </Space>
            
            {/* 悬停效果 */}
            <style>{`
              div:hover .image-actions {
                opacity: 1 !important;
              }
            `}</style>
          </div>
        ))}
      </div>
    </Image.PreviewGroup>
  );
};

export default ImageGallery;