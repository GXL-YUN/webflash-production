package cn.enilu.flash.api;

public class test {

    public static void main(String[] args) {
        String sql="SELECT\n" +
                "    dateList.fd_template_id,\n" +
                "    dateList.fd_name,\n" +
                " CASE  WHEN dateList.fd_module_code = 'km-review' THEN '流程管理'  ELSE '低代码'  END AS fd_module_code , "+
//                                "    dateList.fd_module_code,\n" +
                "    dateList.fd_process_instance_id,\n" +
                "    dateList.fd_node_name,\n" +
                "    dateList.fd_node_type,\n" +
                "    dateList.fd_node_number,\n" +
                "    dateList.fd_node_id,\n" +
                "    dateList.fd_node_instance_id,\n" +
                "\t\tmk_model_20260311ewnua.fd_col_lcpo_name,\n" +
                "\t\tmk_model_20260311ewnua.fd_col_lcpc_name\n" +
                "\t\tFROM (\n" +
                "    SELECT\n" +
                "        lbpm_process_instance.fd_template_id,\n" +
                "        SYS_LBPM_TEMPL.fd_name,\n" +
                "        SYS_LBPM_TEMPL.fd_module_code,\n" +
                "        lbpm_operation_log.fd_process_instance_id,\n" +
                "        lbpm_operation_log.fd_node_name,\n" +
                "        lbpm_operation_log.fd_node_type,\n" +
                "        lbpm_operation_log.fd_node_number,\n" +
                "        lbpm_operation_log.fd_node_id,\n" +
                "        lbpm_operation_log.fd_node_instance_id,\n" +
                "        ROW_NUMBER() OVER (\n" +
                "            PARTITION BY\n" +
                "                lbpm_process_instance.fd_template_id,\n" +
                "                SYS_LBPM_TEMPL.fd_name,\n" +
                "                lbpm_operation_log.fd_node_number\n" +
                "            ORDER BY\n" +
                "                lbpm_operation_log.fd_node_instance_id\n" +
                "        ) rn\n" +
                "    FROM lbpm_process_instance\n" +
                "    LEFT JOIN lbpm_operation_log\n" +
                "        ON lbpm_process_instance.fd_id = lbpm_operation_log.fd_process_instance_id\n" +
                "    LEFT JOIN SYS_LBPM_TEMPL\n" +
                "        ON SYS_LBPM_TEMPL.fd_id = lbpm_process_instance.fd_template_id\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t\n" +
                "    WHERE   lbpm_operation_log.fd_node_number!='N2' and lbpm_operation_log.fd_node_number!='N3' and SYS_LBPM_TEMPL.fd_name NOT LIKE '%已停用%' and  lbpm_operation_log.fd_identity_id = '"+
                ") dateList\n" +
                "LEFT JOIN mk_model_20260311ewnua\n" +
                "        ON dateList.fd_name = mk_model_20260311ewnua.fd_col_lcnewname\n" +
                "WHERE rn = 1\n" +
                "ORDER BY fd_name";


        System.out.println(sql);
    }
}
