package cn.enilu.export.model;

import lombok.Data;

import java.util.List;

@Data
public class DataValidation {

    /** 校验区域 */
    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;

    /** 下拉选项 */
    private List<String> dropdownOptions;

    /** 是否必填 */
    private boolean required;
}