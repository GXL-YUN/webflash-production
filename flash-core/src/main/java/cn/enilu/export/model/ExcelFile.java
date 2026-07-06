package cn.enilu.export.model;

import lombok.Data;

import java.util.List;

/**
 * excal文件对象
 */
@Data
public class ExcelFile {

    /** 文件编码 */
    private String fileCode;

    /** 工作簿 */
    private List<Workbook> workbook;
}