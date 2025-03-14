package cn.enilu.flash.api.testmain;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.BOOLEAN;
import static java.sql.Types.NUMERIC;
import static org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType.FORMULA;
import static org.aspectj.apache.bcel.classfile.annotation.ElementValue.STRING;

public class ExcelReaderByField {

    public static void main(String[] args) {
        String excelFilePath ="C:/Users/think/Desktop/2025日常工作记录.xlsx";

        //获取当前月份
        LocalDate currentDate = LocalDate.now();

        // 获取当前月份
        int month = currentDate.getMonthValue();

        String sheetName = month+"月"; // 指定要读取的工作表名称
        String targetFieldName = "姓名"; // 要查找的字段名

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("未找到指定的Sheet页: " + sheetName);
                return;
            }

            // 查找目标字段所在的列索引
            int targetColumnIndex = -1;
            Row headerRow = sheet.getRow(0); // 假设第一行为表头
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equals(targetFieldName)) {
                    targetColumnIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (targetColumnIndex == -1) {
                System.out.println("未找到目标字段: " + targetFieldName);
                return;
            }

            // 遍历所有行，找到包含目标字段值的行并打印
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 从第二行开始
                Row row = sheet.getRow(i);
                Cell targetCell = row.getCell(targetColumnIndex);
                if (targetCell != null) {
                    System.out.print("行 " + i + ": ");
                    for (Cell cell : row) {
                        printCellValue(cell);
                    }
                    System.out.println();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                System.out.print(cell.getStringCellValue() + "\t");
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue() + "\t");
                } else {
                    System.out.print(cell.getNumericCellValue() + "\t");
                }
                break;
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue() + "\t");
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula() + "\t");
                break;
            default:
                System.out.print(" \t");
                break;
        }
    }
}