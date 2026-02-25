import { FieldConfig, SelectedDataItem } from '../types';

export interface FieldSelectorPanelProps {
    fields: FieldConfig[];
    onSelectionChange: (selectedItems: SelectedDataItem[]) => void;
    onDataGenerate: (selectedFields: string[]) => void;
    loading: boolean;
}