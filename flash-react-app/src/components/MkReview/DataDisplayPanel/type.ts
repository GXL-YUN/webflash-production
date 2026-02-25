import { SelectedDataItem } from '../types';

export interface DataDisplayPanelProps {
    data: any[];
    fields: any[];
    selectedItems?: SelectedDataItem[];
    loading: boolean;
    onDataTransform?: (transformedData: any[]) => void;
}