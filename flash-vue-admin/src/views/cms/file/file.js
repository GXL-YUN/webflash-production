import api from '@/api/cms/fileInfo'
import { getApiUrl } from '@/utils/utils'

export default {
  name: 'file',
  data() {
    return {
      listQuery: {
        page: 1,
        limit: 20,
        originalFileName: undefined
      },
      total: 0,
      list: null,
      listLoading: true,
      selRow: {},
      downloadUrl: '',
      viewPdf:'',
      imgHost: ''
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.downloadUrl = getApiUrl() + '/file/download?idFile='
      this.imgHost = getApiUrl() + '/file/getImgStream?idFile='
      this.viewPdf = getApiUrl() + '/fileUtil/previewPdf.do?idFile='
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      api.getList(this.listQuery).then(response => {
        this.list = response.data.records
        this.listLoading = false
        this.total = response.data.total
      })
    },
    search() {
      this.listQuery.page = 1
      this.fetchData()
    },
    reset() {
      this.listQuery.originalFileName = ''
      this.listQuery.page = 1
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleClose() {

    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData()
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    },
    download(id, fileName,falge) {

      window.location.href = this.downloadUrl + id + '&fileName=' + fileName
    },


    view(id, fileName) {
      window.location.href = this.viewPdf + id + '&fileName=' + fileName
    },

    chkFileType(fileName, types) {
      const typeArr = types.split(',')
      for (const i in typeArr) {
        const type = typeArr[i]
        var d = fileName.length - type.length
        const ret = (d >= 0 && fileName.lastIndexOf(type) == d)
        if (ret) {
          return ret
        }
      }

    }

  }
}
