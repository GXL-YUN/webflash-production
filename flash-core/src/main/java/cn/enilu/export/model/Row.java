package cn.enilu.export.model;

import lombok.Data;

import java.util.List;

/**
 * 行数据
 */
@Data
public class Row {

    /** 行号（0-based） */
    private int rowIndex;

    /** 单元格 */
    private List<Cell> cells;


}