import configEdit from "@/view/index/config";
import appName from "@/view/index/app";
import seName from "@/view/index/index";
import carousel from "@/view/index/carousel";
export default {
    components: {
        appName, configEdit ,seName,carousel
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