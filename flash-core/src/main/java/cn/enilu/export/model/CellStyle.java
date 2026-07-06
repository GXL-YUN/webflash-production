package cn.enilu.export.model;

import lombok.Data;


/**
 *样式
 */
@Data
public class CellStyle {

    private String backgroundColor;
    private String fontColor;
    private Integer fontSize;
    private Boolean bold;
    private String alignment; // left / center / right
}