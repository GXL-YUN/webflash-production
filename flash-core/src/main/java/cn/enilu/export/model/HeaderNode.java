package cn.enilu.export.model;

import lombok.Data;

import java.util.List;

@Data
public class HeaderNode {

    /** 显示名称 */
    private String title;

    /** 字段编码 */
    private String fieldCode;

    /** 起始列 */
    private int colIndex;

    /** 跨几列 */
    private int colSpan = 1;

    /** 跨几行 */
    private int rowSpan = 1;

    /** 子节点（多级） */
    private List<HeaderNode> children;
}