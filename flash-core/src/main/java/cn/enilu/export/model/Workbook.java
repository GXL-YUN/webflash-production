package cn.enilu.export.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 工作簿对象
 */
@Data
public class Workbook {

    /** 工作表名称 */
    private String sheetName;

    /** 行数据 */
    private List<Row> rows;

    /** 列宽（key=列索引，value=宽度） */
    private Map<Integer, Integer> columnWidth;

    /** 行高（key=行索引，value=高度） */
    private Map<Integer, Short> rowHeight;

    /** 数据校验 */
    private List<DataValidation> validations;

    /** 多级表头 */
    private List<HeaderNode> headers;
}