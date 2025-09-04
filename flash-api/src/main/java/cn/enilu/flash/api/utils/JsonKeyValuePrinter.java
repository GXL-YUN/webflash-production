package cn.enilu.flash.api.utils;


import org.json.JSONObject;

public class JsonKeyValuePrinter {
    public static void main(String[] args) {
        String jsonStr = "{\n" +
                "        \"fdNauraWlh\": \"NAURA物料号\",\n" +
                "        \"fdClientWlh\": \"客户物料号\",\n" +
                "        \"fdWlDes\": \"物料描述\",\n" +
                "        \"fdQuantity\": \"数量\",\n" +
                "        \"fdWsdjSource\": \"未税单价（原币）\",\n" +
                "        \"fdWsdjRmb\": \"未税单价（人民币）\",\n" +
                "        \"fdHsdjSource\": \"含税单价（原币）\",\n" +
                "        \"fdHsdjRmb\": \"含税单价（人民币）\",\n" +
                "        \"fdWszjSource\": \"未税总价（原币）\",\n" +
                "        \"fdWszjRmb\": \"未税总价（人民币）\",\n" +
                "        \"fdHszjSource\": \"含税总价（原币）\",\n" +
                "        \"fdHszjRmb\": \"含税总价（人民币）\",\n" +
                "        \"fdHtmlpd\": \"合同毛利判定\",\n" +
                "        \"fdDeliveryType\": \"交货方式\",\n" +
                "        \"fdBusinessType\": \"业务类别\",\n" +
                "        \"fdMaterialAttribute\": \"物料属性\",\n" +
                "        \"fdItemNo\": \"机台项目号\",\n" +
                "        \"fdModelNum\": \"机台型号\",\n" +
                "        \"fdDivision\": \"事业部\",\n" +
                "        \"fdEccn\": \"ECCN\",\n" +
                "        \"fdIsStopSupplying\": \"是否停供\",\n" +
                "        \"fdIsPreinvestment\": \"消耗预投数量\",\n" +
                "        \"fdMsg\": \"消息\",\n" +
                "        \"docMain\": \"所属明细表\",\n" +
                "        \"docIndex\": \"序号\",\n" +
                "        \"fdHtmlPosition\": \"毛利不符情况\",\n" +
                "        \"fdMaterialDes\": \"客户物料描述\",\n" +
                "        \"fdMaterialUnit\": \"物料单位\",\n" +
                "        \"fdMaterialModel\": \"规格型号\"\n" +
                "    }";
        JSONObject json = new JSONObject(jsonStr);

        for (String key : json.keySet()) {
            String value = json.getString(key);
            System.out.println(key);
        }
        for (String key : json.keySet()) {
            String value = json.getString(key);
            System.out.println(value);
        }
    }
}