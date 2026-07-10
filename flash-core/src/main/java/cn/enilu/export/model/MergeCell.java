package cn.enilu.export.model;

import lombok.Data;


/**
 * 核定单元格
 */
@Data
public class MergeCell {

    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;
}