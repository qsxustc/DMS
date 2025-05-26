<template>

  <div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
            >
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="licenseid" label="车牌号"></el-table-column>
      <el-table-column prop="type" label="车型号"></el-table-column>
      <el-table-column prop="color" label="颜色"></el-table-column>
      <el-table-column prop="engineid" label="发动机号码"></el-table-column>
      <el-table-column prop="chassisid" label="底盘号码"></el-table-column>
      <el-table-column prop="dutyofficerid" label="责任人工号"></el-table-column>
      <el-table-column prop="createtime" label="加入系统时间"></el-table-column>
      <el-table-column prop="remarks" label="备注"></el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "carinfo",
  data() {
    return {
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      color: "",
      type: "",
      dutyofficerid: "",
      licenseid: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles: [],
      vis: false,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods: {

    load() {
      this.request.get("/car/driver", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          driverid:this.user.id
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save() {
      this.request.post("/car", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
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
      this.request.delete("/car/" + id).then(res => {
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
      this.request.post("/car/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.licenseid = ""
      this.color = ""
      this.type = ""
      this.dutyofficerid=""
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
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },

  }
}
</script>


<style>
.headerBg {
  background: #eee !important;
}
</style>
