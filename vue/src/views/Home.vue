<template>

  <div style="color: #666" >


    <div style="color: #666;font-size: 14px;width: 40%;display: inline-block">

      <span style="text-align-all: left;font-size: 18px;padding-top: 10px; color: black;border-bottom: black">欢迎你！{{
          user.name
        }}</span>

    </div>
    <div style="margin-top: 20px;width: 90%;margin-left: 20px" >
      <el-card >
        <el-row style="font-size: 20px">
          系统通知
        </el-row>
        <div>
          <div style="margin: 5px 0">
            <div style="padding: 8px 0; border-bottom: 1px dashed #ccc" v-for="item in tableData" :key="item.id">
              <div class="pd-10" style="font-size: 18px; color: #3F5EFB; cursor: pointer"
                   @click="chakanxiaoxi(item.id)">{{ item.name }}
              </div>
              <div style="font-size: 12px; margin-top: 5px">
                <i class="el-icon-user-solid"></i> <span>{{ item.user }}</span>
                <i class="el-icon-time" style="margin-left: 5px"></i> <span>{{ item.time }}</span>
              </div>
            </div>
          </div>
          <div style="padding: 10px 0">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[2, 5, 10, 20]"
                :page-size="pageSize"
                layout="total, prev, pager, next"
                :total="total">
            </el-pagination>
          </div>
        </div>
        <el-dialog

            :visible.sync="drawer">

          <div style="margin: 20px 0; ">
            <div class="pd-10" style="font-size: 20px; color: #3F5EFB; cursor: pointer">{{ article.name }}</div>
            <div style="font-size: 14px; margin-top: 10px">
              <i class="el-icon-user-solid"></i> <span>{{ article.user }}</span>
              <i class="el-icon-time" style="margin-left: 10px"></i> <span>{{ article.time }}</span>
            </div>
          </div>

          <div style="margin: 20px 0">
            <mavon-editor
                class="md"
                :value="article.content"
                :subfield="false"
                :defaultOpen="'preview'"
                :toolbarsFlag="false"
                :editable="false"
                :scrollStyle="true"
                :ishljs="true"
            />
          </div>
        </el-dialog>

      </el-card>
    </div>

  </div>
</template>

<script>

import axios from "axios";

export default {
  name: "Article",
  data() {
    return {
      article: {},
      drawer: false,
      form: {},
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      dialogFormVisible: false,
      teachers: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      content: '',
      viewDialogVis: false
    }
  },
  created() {
    this.load()
  },
  methods: {
    view(content) {
      this.content = content
      this.viewDialogVis = true
    },
    // 绑定@imgAdd event
    imgAdd(pos, $file) {
      let $vm = this.$refs.md
      // 第一步.将图片上传到服务器.
      const formData = new FormData();
      formData.append('file', $file);
      axios({
        url: 'http://localhost:9090/file/upload',
        method: 'post',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data'},
      }).then((res) => {
        // 第二步.将返回的url替换到文本原位置![...](./0) -> ![...](url)
        $vm.$img2Url(pos, res.data);
      })
    },
    load() {
      this.request.get("/article/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    changeEnable(row) {
      this.request.post("/article/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/article/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/article/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    save() {
      this.request.post("/article", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    download(url) {
      window.open(url)
    },
    chakanxiaoxi(id){
      this.drawer=true
      this.request.get("/article/" + id).then(res => {
        this.article = res.data
      })
    }
  }
}
</script>
