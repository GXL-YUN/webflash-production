package cn.enilu.flash.api.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class XmlParserDOM {
    public static void main(String[] args) {
        String xml = "\n" +
                "<model >\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_col_sqsj\" label=\"申请时间1\" type=\"Date\" formula=\"true\" defaultValue=\"DateTimeFunction.getNow()\" recalculateOnSave=\"true\" businessType=\"datetime\" isMark=\"false\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_col_qsgs\" label=\"签署公司\" notNull=\"true\" enumValues=\"北京北方华创微电子装备有限公司|NMC;合肥北方华创微电子装备有限公司|NHF;无锡北方华创微电子装备有限公司|NWX;武汉北方华创微电子装备有限公司|NWH;深圳北方华创微电子装备有限公司|NSZ;上海北方华创微电子装备有限公司|NSH\" type=\"String\" businessType=\"select\" isMark=\"false\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_col_fwzx\" label=\"服务中心\" notNull=\"true\" enumValues=\"北京服务中心|1;武汉服务中心|2;合肥服务中心|3;上海服务中心|4;台湾服务中心|5;安装交付一中心|6;安装交付二中心|7;内协转售单|8\" type=\"String\" businessType=\"select\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_htlx\" label=\"合同类型1\" notNull=\"true\" enumValues=\"框架协议|XY;销售订单|XS1;销售合同|XS2;credit赠送|XS3;其他|XS4\" type=\"String\" businessType=\"inputRadio\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_cust_code\" label=\"客户编号\" type=\"String\" businessType=\"inputText\" length=\"200\" canDisplay=\"false\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"new_account_id\" label=\"客户名称\" type=\"String\" businessType=\"relationChoose\" customElementProperties=\"{&quot;isMultiVal&quot;:&quot;true&quot;,&quot;controlId&quot;:&quot;new_account_id&quot;}\" />\n" +
                "<extendSimpleProperty name=\"new_account_id_dataFdId\" label=\"客户名称(不需映射)\" type=\"String\" businessType=\"relationChoose\" store=\"false\" customElementProperties=\"{quot;isMultiValquot;:quot;truequot;,quot;controlIdquot;:quot;new_account_idquot;,quot;isShowquot;:quot;falsequot;}\" />\n" +
                "<extendSimpleProperty name=\"new_account_id_dataModelName\" label=\"客户名称(不需映射)\" type=\"String\" businessType=\"relationChoose\" store=\"false\" customElementProperties=\"{quot;isMultiValquot;:quot;truequot;,quot;controlIdquot;:quot;new_account_idquot;,quot;isShowquot;:quot;falsequot;}\" />\n" +
                "<extendSimpleProperty name=\"new_account_id_dataSourceId\" label=\"客户名称(不需映射)\" type=\"String\" businessType=\"relationChoose\" store=\"false\" customElementProperties=\"{quot;isMultiValquot;:quot;truequot;,quot;controlIdquot;:quot;new_account_idquot;,quot;isShowquot;:quot;falsequot;}\" />\n" +
                "<extendSimpleProperty name=\"new_account_id_text\" label=\"客户名称(显示值)\" type=\"String\" businessType=\"relationChoose\" customElementProperties=\"{quot;isMultiValquot;:quot;truequot;,quot;controlIdquot;:quot;new_account_idquot;,quot;isShowquot;:quot;falsequot;}\" />\n" +
                "<extendSimpleProperty name=\"new_account_id_selectedDatas\" label=\"客户名称(不需映射)\" type=\"String\" businessType=\"relationChoose\" store=\"false\" customElementProperties=\"{quot;isMultiValquot;:quot;truequot;,quot;controlIdquot;:quot;new_account_idquot;,quot;isShowquot;:quot;falsequot;}\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_3d9eb62e5004e6\" label=\"客户名称隐藏显示值\" type=\"String\" businessType=\"inputText\" length=\"200\" readOnly=\"true\" canDisplay=\"false\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"new_po\" label=\"客户订单号\" type=\"String\" businessType=\"inputText\" length=\"200\" notNull=\"true\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"fd_col_khddrq\" label=\"客户订单日期\" type=\"Date\" notNull=\"true\" businessType=\"datetime\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_ywlb\" label=\"业务类别\" notNull=\"true\" enumValues=\"自产类备件|selfSpareParts;贸易类备件|tradeSpareParts;自产类服务|selfService;贸易类服务|tradeService;混合类|mixed\" type=\"String\" businessType=\"inputRadio\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_yzlx\" label=\"用章类型1\" notNull=\"true\" enumValues=\"公章|1;合同章|2\" type=\"String\" businessType=\"inputRadio\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_sfsa\" label=\"是否涉A\" notNull=\"true\" enumValues=\"是|1;否|2\" type=\"String\" businessType=\"inputRadio\" isMark=\"false\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_price_type\" label=\"定价类型\" notNull=\"true\" enumValues=\"含税|1;不含税|2\" type=\"String\" businessType=\"inputRadio\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_currency_type\" label=\"币种1\" enumValues=\"CNY|CNY;USD|USD;JPY|JPY;SGD|SGD;HKD|HKD\" type=\"String\" businessType=\"select\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_exchange_rates\" label=\"汇率1\" type=\"Double\" businessType=\"inputText\" length=\"16\" scale=\"6\" formula=\"true\" defaultValue=\"1\" isMark=\"false\" customElementProperties=\"displayFormat={'displayFormat':'false'}\" />\n" +
                "<extendSimpleProperty name=\"fd_duty_rate\" label=\"税率\" notNull=\"true\" enumValues=\"0%|0%;3%|3%;6%|6%;9%|9%;13%|13%;16%|16%;17%|17%\" type=\"String\" businessType=\"select\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_htzje\" label=\"合同总金额1\" type=\"BigDecimal\" businessType=\"inputText\" length=\"16\" scale=\"2\" isMark=\"false\" customElementProperties=\"scale='2'\" />\n" +
                "<extendSimpleProperty name=\"fd_col_rmbje\" label=\"合同总金额（人民币）1\" type=\"BigDecimal\" scale=\"2\" businessType=\"calculation\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_ydjhrq\" label=\"合同约定交货日期\" type=\"Date\" notNull=\"true\" businessType=\"datetime\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_zbq\" label=\"质保期（合同约定）\" type=\"String\" businessType=\"inputText\" length=\"200\" notNull=\"true\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"fd_col_khbzzq\" label=\"客户标准账期\" type=\"String\" businessType=\"inputText\" length=\"200\" notNull=\"true\" readOnly=\"true\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"fd_col_fkfs\" label=\"付款方式\" notNull=\"true\" enumValues=\"现金|1;银行承兑汇票|2;商业承兑汇票|3;抵账|4;供应链融资|5\" type=\"String\" businessType=\"inputCheckbox\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_ystk\" label=\"验收条款\" type=\"String\" businessType=\"inputText\" length=\"200\" notNull=\"true\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"fd_col_htfktj\" label=\"合同付款条件1\" notNull=\"true\" enumValues=\"同于标准账期|1;不同于标准账期|2\" type=\"String\" businessType=\"inputRadio\" formula=\"true\" defaultValue=\"&quot;1&quot;\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_btyhtzq\" label=\"合同付款条件2\" type=\"String\" businessType=\"inputText\" length=\"200\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"fd_col_sfxgkhzq\" label=\"是否修改客户账期\" notNull=\"true\" enumValues=\"覆盖|2;不变|3\" type=\"String\" businessType=\"inputRadio\" formula=\"true\" defaultValue=\"&quot;3&quot;\" isMark=\"false\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_col_lr\" label=\"利润1\" notNull=\"true\" enumValues=\"满足毛利要求|1;不满足最低毛利要求|2;低于成本|3\" type=\"String\" businessType=\"select\" isMark=\"false\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_col_zsyj\" label=\"赠送依据\" type=\"String\" businessType=\"inputText\" length=\"200\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"fd_account_detail\" label=\"申请明细总金额\" type=\"Double\" businessType=\"inputText\" length=\"16\" canDisplay=\"false\" isMark=\"false\" customElementProperties=\"displayFormat={'displayFormat':'false'}\" />\n" +
                "<extendSimpleProperty name=\"fd_factory\" label=\"工厂code\" type=\"String\" businessType=\"inputText\" length=\"200\" formula=\"true\" defaultValue=\"1000\" canDisplay=\"false\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "<extendSimpleProperty name=\"fd_error_value\" label=\"合同总金额误差值\" type=\"String\" businessType=\"inputText\" length=\"200\" defaultValue=\"2\" canDisplay=\"false\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"new_specialagreement\" label=\"特别约定\n" +
                "（非常规、个性化承诺条款、赠送条款等）\n" +
                "\" type=\"String\" length=\"4000\" businessType=\"textarea\" isMark=\"false\" />\n" +
                "<extendSimpleProperty name=\"fd_col_fwyj\" label=\"是否有法务意见\" notNull=\"true\" enumValues=\"是|1;否|2\" type=\"String\" businessType=\"inputRadio\" isMark=\"false\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"fd_col_sxfwyj\" label=\"法务意见\" type=\"String\" length=\"4000\" notNull=\"true\" businessType=\"textarea\" isMark=\"false\" />\n" +
                "\n" +
                "<extendSimpleProperty name=\"new_sharepointadd\" label=\"文档地址\" type=\"String\" businessType=\"inputText\" length=\"200\" isMark=\"false\" customElementProperties=\"\" />\n" +
                "</model>"; // 您的XML内容

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            NodeList properties = doc.getElementsByTagName("extendSimpleProperty");
            JSONObject  mainData=new JSONObject();
            JSONArray mainArr=new JSONArray();

            for (int i = 0; i < properties.getLength(); i++) {
                Node node = properties.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getAttribute("name");
                    String label = element.getAttribute("label");
                    System.out.println("Name: " + name + ", Label: " + label);

                    JSONObject  massage=new JSONObject();
                    massage.put("fdKey",name);
                    massage.put("fdName",label);
                    mainArr.put(massage);
                }
            }

            mainData.put("fdMianDate",mainArr);
            createExcelFromJson(mainData, "ekp.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void createExcelFromJson(JSONObject jSONObject, String fileName) throws Exception {
        try {
            // 1. 解析JSON数据
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> dataMap = mapper.readValue(jSONObject.toString(), Map.class);
            List<Map<String, String>> fieldList = (List<Map<String, String>>) dataMap.get("fdMianDate");

            // 2. 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("字段明细");

            // 3. 设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // 4. 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"字段Key", "字段名称","字段类型","备注"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 5. 填充数据
            int rowNum = 1;
            for (Map<String, String> field : fieldList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(field.get("fdKey"));
                row.createCell(1).setCellValue(field.get("fdName"));
                row.createCell(2).setCellValue(field.get("fdType"));
                row.createCell(3).setCellValue(field.get("fdEnum"));
            }

            // 6. 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 7. 写入文件
            try (FileOutputStream outputStream = new FileOutputStream("D:\\项目文件\\接口文档字段\\"+fileName+".xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Excel文件生成成功！");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("生成Excel文件时出错: " + e.getMessage());
        }
    }
}