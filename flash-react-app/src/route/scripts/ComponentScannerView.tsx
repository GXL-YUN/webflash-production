// components/ComponentScannerView.tsx
import React, { useState, useEffect } from 'react';

interface ComponentInfo {
    name: string;
    filePath: string;
    hasDefaultExport: boolean;
    exports: string[];
}

interface ComponentManifest {
    scanTime: string;
    components: ComponentInfo[];
}

const ComponentScannerView: React.FC = () => {
    const [manifest, setManifest] = useState<ComponentManifest | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const loadManifest = async () => {
            try {
                setLoading(true);
                const response = await fetch('./component-manifest.json');

                if (!response.ok) {
                    throw new Error('无法加载组件清单');
                }

                const data: ComponentManifest = await response.json();
                setManifest(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : '未知错误');
            } finally {
                setLoading(false);
            }
        };

        loadManifest();
    }, []);

    if (loading) {
        return <div className="p-4">加载组件清单中...</div>;
    }

    if (error) {
        return (
            <div className="p-4 bg-red-50 border border-red-200 rounded">
                <h2 className="text-red-700 font-semibold">错误</h2>
                <p className="text-red-600">{error}</p>
                <button
                    onClick={() => window.location.reload()}
                    className="mt-2 px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                >
                    重试
                </button>
            </div>
        );
    }

    if (!manifest) {
        return <div className="p-4">未找到组件清单</div>;
    }

    return (
        <div className="p-6">
            <div className="mb-6">
                <h1 className="text-2xl font-bold text-gray-900">React组件扫描器</h1>
                <p className="text-gray-600">扫描时间: {new Date(manifest.scanTime).toLocaleString()}</p>
                <p className="text-gray-600">找到 {manifest.components.length} 个组件</p>
            </div>

            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                {manifest.components.map((component, index) => (
                    <div key={index} className="border rounded-lg p-4 bg-white shadow-sm">
                        <h3 className="font-semibold text-lg text-blue-600 mb-2">{component.name}</h3>
                        <p className="text-sm text-gray-600 mb-1">
                            <span className="font-medium">路径:</span> {component.filePath}
                        </p>
                        <p className="text-sm text-gray-600 mb-1">
                            <span className="font-medium">默认导出:</span>
                            <span className={component.hasDefaultExport ? "text-green-600" : "text-red-600"}>
                {component.hasDefaultExport ? '是' : '否'}
              </span>
                        </p>
                        {component.exports.length > 0 && (
                            <p className="text-sm text-gray-600">
                                <span className="font-medium">导出:</span> {component.exports.join(', ')}
                            </p>
                        )}
                    </div>
                ))}
            </div>

            {manifest.components.length === 0 && (
                <div className="text-center py-8 text-gray-500">
                    未找到任何React组件
                </div>
            )}
        </div>
    );
};

export default ComponentScannerView;