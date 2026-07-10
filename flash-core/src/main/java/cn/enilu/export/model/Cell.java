package cn.enilu.export.model;

import lombok.Data;

import java.util.List;

/**
 * 单元格对象
 */
@Data
public class Cell {

    /** 字段编码 */
    private String fieldCode;

    /** 单元格值 */
    private Object value;

    /** 样式 */
    private CellStyle style;

    /** 合并单元格 */
    private List<MergeCell> mergeCells;

}