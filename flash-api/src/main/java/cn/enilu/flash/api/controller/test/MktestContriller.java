package cn.enilu.flash.api.controller.test;


import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class MktestContriller {


    @PostMapping("mk_ekppass/post")
    public JSONObject post(@RequestBody String key) {
        // 从完整URL中提取 /ekp_mkpass/back 后面的部分作为参数
        // 例如: https://testmmk.naura.com/ekp_mkpass/back/lims/LimsTemListController/getAll
        // 提取后得到: lims/LimsTemListController/getAll
        String pathParam = "";
        if (key != null && key.contains("/ekp_mkpass/back/")) {
            pathParam = key.substring(key.indexOf("/ekp_mkpass/back/") + "/ekp_mkpass/back/".length());
        } else if (key != null && key.startsWith("/")) {
            // 如果传入的已经是相对路径
            pathParam = key.startsWith("/") ? key.substring(1) : key;
        } else {
            pathParam = key;
        }
        
        JSONObject result = new JSONObject();
        result.put("originalUrl", key);
        result.put("extractedPath", pathParam);
        return result;
    }

    @GetMapping("mk_ekppass/get")
    public JSONObject get(@RequestParam String url) {
        // 从完整URL中提取 /ekp_mkpass/back 后面的部分作为参数
        // 例如: https://testmmk.naura.com/ekp_mkpass/back/lims/LimsTemListController/getAll
        // 提取后得到: lims/LimsTemListController/getAll
        String pathParam = "";
        if (url != null && url.contains("/ekp_mkpass/back/")) {
            pathParam = url.substring(url.indexOf("/ekp_mkpass/back/") + "/ekp_mkpass/back/".length());
        } else if (url != null && url.startsWith("/")) {
            // 如果传入的已经是相对路径
            pathParam = url.startsWith("/") ? url.substring(1) : url;
        } else {
            pathParam = url;
        }
        JSONObject result = new JSONObject();
        result.put("originalUrl", url);
        result.put("extractedPath", pathParam);
        return result;
    }
}
