import configEdit from "@/views/index/config";
import appName from "@/views/index/app";
import seName from "@/views/index";
//文件下载
import file from "@/views/cms/file";

import files from "@/views/index/attmain";
//工具访问网址访问
import bannerTag from "@/views/cms/banner/bannerTag";

import carousel from "@/views/index/carousel";
import add from "@/views/protal/view/add";
import total from "@/views/protal/view/total";


import contents from "@/views/protal/view/conten";

import alent from "@/views/protal/view/alent";
export default {
  components: {
    appName, configEdit, seName, bannerTag, carousel, file, files,total,contents,alent,add
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
