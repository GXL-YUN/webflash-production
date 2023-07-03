
import articleApi from '@/api/article'
import {getApiUrl} from '@/utils/utils'




export default {
    name :"listVueData",
    data() {
        return {
            currentDate: new Date()
        };
    },

    fetchData() {
        articleApi.getList(queryData).then(response => {
            this.list = response.data.records
            for (const index in this.list) {
                const item = this.list[index]
                item.img = getApiUrl() + '/file/getImgStream?idFile=' + item.img
            }
            this.listLoading = false
            this.total = response.data.total
        })
    }
}