export interface ApiConfig {
    url: string;
    method: 'GET' | 'POST' | 'PUT' | 'DELETE';
    headers?: Record<string, string>;
    params?: Record<string, any>;
    body?: any;
}

export interface ApiConfigPanelProps {
    onApiResponse: (data: any, fields: any[]) => void;
    loading: boolean;
    setLoading: (loading: boolean) => void;
}