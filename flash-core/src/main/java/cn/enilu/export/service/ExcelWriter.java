package cn.enilu.export.service;

import cn.enilu.export.model.*;
import cn.enilu.export.model.Cell;
import cn.enilu.export.model.CellStyle;
import cn.enilu.export.model.DataValidation;
import cn.enilu.export.model.Row;
import cn.enilu.export.model.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Excel 写入服务类
 * 提供将 ExcelFile 对象转换为 Excel 文件并输出到流的功能
 */
@Service
public class ExcelWriter {

    /**
     * 基础 Excel 写入方法（旧版，功能较简单）
     *
     * @param excelFile Excel 文件元数据对象
     * @param os        输出流
     * @throws Exception 写入异常
     */
    public static void write(ExcelFile excelFile, OutputStream os) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        
        // 支持多个工作表（Sheet）
        List<Workbook> workbooks = excelFile.getWorkbook();
        if (workbooks == null || workbooks.isEmpty()) {
            // 兼容旧版单 Workbook 结构
            //workbooks = java.util.Collections.singletonList(excelFile.getWorkbook());
        }

        for (Workbook meta : workbooks) {
            XSSFSheet sheet = workbook.createSheet(meta.getSheetName());

            // 表头
            if (meta.getHeaders() != null) {
                buildHeader(sheet, meta.getHeaders(), workbook);
            }

            // 行
            if (meta.getRows() != null) {
                for (Row row : meta.getRows()) {
                    XSSFRow r = sheet.createRow(row.getRowIndex());
                    if (row.getCells() != null) {
                        for (Cell c : row.getCells()) {
                            // 注意：此处仍使用简单的哈希映射列索引，若需精确控制建议迁移至 writeExcel 方法
                            XSSFCell cell = r.createCell(getColIndex(c.getFieldCode()));
                            cell.setCellValue(c.getValue() == null ? "" : String.valueOf(c.getValue()));
                        }
                    }
                }
            }
        }

        workbook.write(os);
        workbook.close();
    }

    /**
     * 根据字段代码获取列索引（哈希取模方式，仅用于简单映射）
     *
     * @param fieldCode 字段代码
     * @return 列索引
     */
    private static int getColIndex(String fieldCode) {
        return Math.abs(fieldCode.hashCode() % 100);
    }


    /**
     * 高级 Excel 写入方法（支持列宽、多级表头、样式、数据校验等）
     *
     * @param excelFile Excel 文件元数据对象
     * @param os        输出流
     * @throws Exception 写入异常
     */
//    public static void writeExcel(ExcelFile excelFile, OutputStream os) throws Exception {
//
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet(excelFile.getWorkbook().getSheetName());
//
//        Workbook meta = excelFile.getWorkbook();
//
//        // ===== 列宽 =====
//        if (meta.getColumnWidth() != null) {
//            meta.getColumnWidth().forEach((k, v) -> sheet.setColumnWidth(k, v * 256));
//        }
//
//        // ===== 多级表头 =====
//        if (meta.getHeaders() != null) {
//            buildHeader(sheet, meta.getHeaders(), workbook);
//        }
//
//        // ===== 行数据 =====
//        for (Row rowMeta : meta.getRows()) {
//            XSSFRow row = sheet.createRow(rowMeta.getRowIndex());
//
//            if (meta.getRowHeight() != null && meta.getRowHeight().containsKey(rowMeta.getRowIndex())) {
//                row.setHeight(meta.getRowHeight().get(rowMeta.getRowIndex()));
//            }
//
//            for (Cell cellMeta : rowMeta.getCells()) {
//                XSSFCell cell = row.createCell(cellMeta.getFieldCode().hashCode() % 100);
//                cell.setCellValue(cellMeta.getValue() == null ? "" : cellMeta.getValue().toString());
//
//                if (cellMeta.getStyle() != null) {
//                    cell.setCellStyle(buildCellStyle(workbook, cellMeta.getStyle()));
//                }
//            }
//        }
//
//        // ===== 合并单元格 =====
//        //        if (meta.getMergeCells() != null) {
//        //            meta.getMergeCells().forEach(m ->
//        //                    sheet.addMergedRegion(new CellRangeAddress(
//        //                            m.getFirstRow(), m.getLastRow(),
//        //                            m.getFirstCol(), m.getLastCol()
//        //                    ))
//        //            );
//        //        }
//
//        // ===== 数据校验 =====
//        if (meta.getValidations() != null) {
//            buildValidation(sheet, meta.getValidations(), workbook);
//        }
//
//        workbook.write(os);
//        workbook.close();
//    }
//


    /**
     * 构建数据校验（如下拉列表）
     *
     * @param sheet       Excel 工作表
     * @param validations 数据校验配置列表
     * @param workbook    Excel 工作簿
     */
    private static void buildValidation(XSSFSheet sheet,
                                        List<DataValidation> validations,
                                        XSSFWorkbook workbook) {

        DataValidationHelper helper = sheet.getDataValidationHelper();

        for (DataValidation dv : validations) {
            CellRangeAddressList regions =
                    new CellRangeAddressList(
                            dv.getFirstRow(), dv.getLastRow(),
                            dv.getFirstCol(), dv.getLastCol()
                    );

            DataValidationConstraint constraint =
                    helper.createExplicitListConstraint(
                            dv.getDropdownOptions().toArray(new String[0])
                    );

            XSSFDataValidation validation =
                    (XSSFDataValidation) helper.createValidation(constraint, regions);

            validation.setSuppressDropDownArrow(true);
            sheet.addValidationData(validation);
        }
    }

    /**
     * 构建表头入口方法
     *
     * @param sheet    Excel 工作表
     * @param nodes    表头节点列表
     * @param workbook Excel 工作簿
     * @return 最大深度（行数）
     */
    private static int buildHeader(XSSFSheet sheet,
                                   List<HeaderNode> nodes,
                                   XSSFWorkbook workbook) {

        int maxDepth = 0;

        for (HeaderNode node : nodes) {
            int depth = buildHeaderRecursive(sheet, node, 0, workbook);
            maxDepth = Math.max(maxDepth, depth);
        }
        return maxDepth;
    }

    /**
     * 递归构建多级表头
     *
     * @param sheet    Excel 工作表
     * @param node     当前表头节点
     * @param rowIndex 当前行索引
     * @param workbook Excel 工作簿
     * @return 当前分支的最大深度
     */
    private static int buildHeaderRecursive(XSSFSheet sheet,
                                            HeaderNode node,
                                            int rowIndex,
                                            XSSFWorkbook workbook) {

        XSSFRow row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        XSSFCell cell = row.createCell(node.getColIndex());
        cell.setCellValue(node.getTitle());

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);

        int endRow = rowIndex + node.getRowSpan() - 1;
        int endCol = node.getColIndex() + node.getColSpan() - 1;

        if (node.getRowSpan() > 1 || node.getColSpan() > 1) {
            sheet.addMergedRegion(
                    new CellRangeAddress(rowIndex, endRow, node.getColIndex(), endCol)
            );
        }

        int maxDepth = rowIndex + node.getRowSpan();

        if (node.getChildren() != null) {
            for (HeaderNode child : node.getChildren()) {
                int depth = buildHeaderRecursive(sheet, child, rowIndex + node.getRowSpan(), workbook);
                maxDepth = Math.max(maxDepth, depth);
            }
        }

        return maxDepth;
    }

    /**
     * 设置单元格值（根据类型自动匹配）
     *
     * @param cell  Excel 单元格
     * @param value 值对象
     */
    private static void setCellValue(XSSFCell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }


    /**
     * 解析颜色字符串为 POI 索引颜色
     *
     * @param color 颜色字符串（支持 #RRGGBB 或 IndexedColors 枚举名）
     * @return 颜色索引
     */
    private static short parseColor(String color) {
        if (color.startsWith("#")) {
            return hexToIndexedColor(color);
        }
        return IndexedColors.valueOf(color.toUpperCase()).getIndex();
    }

    /**
     * 将十六进制颜色转换为近似的 POI 索引颜色
     *
     * @param hex 十六进制颜色字符串
     * @return 颜色索引
     */
    private static short hexToIndexedColor(String hex) {
        switch (hex.toUpperCase()) {
            case "#FFFFFF": return IndexedColors.WHITE.getIndex();
            case "#000000": return IndexedColors.BLACK.getIndex();
            case "#FF0000": return IndexedColors.RED.getIndex();
            case "#00FF00": return IndexedColors.GREEN.getIndex();
            case "#0000FF": return IndexedColors.BLUE.getIndex();
            case "#FFFF00": return IndexedColors.YELLOW.getIndex();
            case "#F5F7FA": return IndexedColors.GREY_25_PERCENT.getIndex();
            default: return IndexedColors.AUTOMATIC.getIndex();
        }
    }

    /**
     * 构建单元格样式
     *
     * @param workbook Excel 工作簿
     * @param style    样式元数据
     * @return POI 单元格样式对象
     */
    private static XSSFCellStyle buildCellStyle(XSSFWorkbook workbook, CellStyle style) {

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        // ===== 字体 =====
        if (style.getFontSize() != null) {
            font.setFontHeightInPoints(style.getFontSize().shortValue());
        }
        if (Boolean.TRUE.equals(style.getBold())) {
            font.setBold(true);
        }
        if (style.getFontColor() != null) {
            font.setColor(parseColor(style.getFontColor()));
        }
        cellStyle.setFont(font);

        // ===== 背景色 =====
        if (style.getBackgroundColor() != null) {
            cellStyle.setFillForegroundColor(parseColor(style.getBackgroundColor()));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        // ===== 对齐 =====
        switch (style.getAlignment()) {
            case "center":
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                break;
            case "right":
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                break;
            default:
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        return cellStyle;
    }

}