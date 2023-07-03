import configEdit from "@/views/index/config";
import appName from "@/views/index/app";
import seName from "@/views/index/index";
//文件下载
import file from "@/views/cms/file/index";

import files from "@/views/index/attmain";
//工具访问网址访问
import bannerTag from "@/views/cms/banner/bannerTag";

import carousel from "@/views/index/carousel";


export default {
    components: {
        appName, configEdit ,seName,bannerTag,carousel,file ,files
    },
    data() {
        return {
            activeName: 'second'
        };
    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
        }
    }
};
