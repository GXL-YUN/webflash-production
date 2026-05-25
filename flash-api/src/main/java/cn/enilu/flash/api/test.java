package cn.enilu.flash.api;

public class test {
    public static void main(String[] args) {
        StringBuffer buff=new StringBuffer();
        buff.append( "select b.*, ROWNUM rn  from (SELECT\n" +
                "\t\tEMPLOYEE_CODE as id,     \n" +
                "    TO_CHAR(SCHEDULING_DATE, 'YYYYMM') AS month,\n" +
                "\t\tEMPLOYEE_NAME  as name,\n" +
                "\t\tDEPARTMENT_CODE as department,   \n" +
                "\t\tNMC_CODE as employeeId,\n" +
                "    JSON_ARRAYAGG(\n" +
                "        JSON_OBJECT(\n" +
                "\t\t\t\t'date' VALUE TO_CHAR(SCHEDULING_DATE, 'MM/DD'),\n" +
                "\t\t\t\t'dayOfWeek' VALUE TO_CHAR(SCHEDULING_DATE, 'DAY'),\n" +
                "\t\t\t\t'noWeekCount' VALUE TAKE_WORK_HOURS+SHI_DAYS+BING_DAYS+HUN_DAYS+HUN_DAYS+CHAN_DAYS+PEI_DAYS+PEI_DAYS+BU_J_DAYS+BU_F_DAYS+SANG_DAYS+GONG_DAYS+CHILD_DAYS+XIN_DAYS+\n" +
                "WELFARE_HOURS+WELFARE_DAYS+YU_DAYS+DU_DAYS,\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t'detail' value SCHEDULING_NAME\n" +
                "\t\t\t\t)\n" +
                "        RETURNING CLOB\n" +
                "    ) AS schedules\n" +
                "FROM MK_MODEL_20260423U037V\n" +
                "WHERE TO_CHAR(SCHEDULING_DATE, 'YYYYMM') = "+
                " GROUP BY TO_CHAR(SCHEDULING_DATE, 'YYYYMM')  ,EMPLOYEE_NAME  ,DEPARTMENT_CODE  ,    EMPLOYEE_CODE ,NMC_CODE\n)b ");

        System.out.println(buff.toString());

    }

}
