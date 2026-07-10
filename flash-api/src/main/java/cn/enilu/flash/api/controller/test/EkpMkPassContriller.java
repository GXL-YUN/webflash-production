package cn.enilu.flash.api.controller.test;


import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.test.Girl;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.utils.factory.Page;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;



/**
 * 中间件代理控制器
 */
@RestController
@RequestMapping("/ekp_mkpass/back/EHS/multiplant")
public class EkpMkPassContriller {

    @PostMapping("/dateAll")
    public Object approver(@RequestBody JSONObject requertInfo) throws Exception {





        String id = "\n" +
                "{\n" +
                "    \"status\": 0,\n" +
                "    \"msg\": \"\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"id\": \"N1\",\n" +
                "            \"name\": \"N1 园区总览\",\n" +
                "            \"image\": \"maps/N1.jpg\",\n" +
                "            \"points\": [\n" +
                "                {\n" +
                "                    \"id\": \"N3-C座\",\n" +
                "                    \"name\": \"N3-C座\",\n" +
                "                    \"x\": 0.22,\n" +
                "                    \"y\": 0.21,\n" +
                "                    \"type\": \"warning\",\n" +
                "                    \"description\": \"122121\",\n" +
                "                    \"status\": 0,\n" +
                "                    \"lastUpdate\": 0,\n" +
                "                    \"lableDate\": [\n" +
                "                        {\n" +
                "                            \"name\": \"动火作业\",\n" +
                "                            \"number\": 1\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"有限空间作业\",\n" +
                "                            \"number\": 2\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"N3-室外\",\n" +
                "                    \"name\": \"N3-室外\",\n" +
                "                    \"x\": 0.223,\n" +
                "                    \"y\": 0.22,\n" +
                "                    \"type\": \"warning\",\n" +
                "                    \"description\": 0,\n" +
                "                    \"status\": 0,\n" +
                "                    \"lastUpdate\": 0,\n" +
                "                    \"lableDate\": [\n" +
                "                        {\n" +
                "                            \"name\": \"动火作业\",\n" +
                "                            \"number\": 1\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        return JSONUtil.parseObj(id);
    }


}
