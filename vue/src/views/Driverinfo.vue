<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="请输入电话" suffix-icon="el-icon-message" class="ml-5" v-model="phone"></el-input>

      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>



    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55"></el-table-column>-->
      <el-table-column prop="id" label="ID" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名"   align="center"></el-table-column>
<!--      <el-table-column prop="role" label="角色">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag type="primary" v-if="scope.row.role === 'ROLE_ADMIN'">管理员</el-tag>-->
<!--          <el-tag type="warning" v-if="scope.row.role === 'ROLE_RECORDER'">记录员</el-tag>-->
<!--          <el-tag type="success" v-if="scope.row.role === 'ROLE_DRIVER'">驾驶员</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column prop="name" label="姓名" align="center"></el-table-column>
      <el-table-column prop="age" label="年龄" align="center"></el-table-column>
      <el-table-column prop="driveyear" label="驾龄"  align="center"></el-table-column>
      <el-table-column prop="phone" label="电话"  align="center"></el-table-column>
      <el-table-column prop="address" label="地址"  align="center"></el-table-column>
<!--      <el-table-column prop="leaderid" label="记录员ID"></el-table-column>-->
      <el-table-column label="操作"  width="300" align="center">
        <template slot-scope="scope">

          <el-button type="success" slot="reference" @click="handleEdit(scope.row)"
          >编辑 <i class="el-icon-edit"></i></el-button>

          <el-button type="warning" @click="handleChangePassword(scope.row)"
          >修改密码 <i class="el-icon-edit"></i></el-button>

        </template>
      </el-table-column>
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

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="驾龄">
          <el-input v-model="form.driveyear" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="记录员id">
          <el-input v-model="form.leaderid" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="passwordFormVisible" width="35%">
      <el-form label-width="120px" size="small" :model="formPassword" :rules="rules" ref="pass">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="formPassword.newPassword" autocomplete="off" show-password style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="formPassword.confirmPassword" autocomplete="off" show-password style="width: 80%"></el-input>
        </el-form-item>
        <el-button style="margin-left: 170px" type="warning" @click="passwordFormVisible=false">取 消</el-button>
        <el-button style="margin-left: 50px" type="primary" @click="savePassword">确 定</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "User",
  data() {
    return {
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      phone: "",
      role: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      passwordFormVisible:false,
      formPassword:{},
      roles: [],
      jiluyuanid:'',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      courses: [],
      vis: false,
      rules: {
        password: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
      },
    }
  },
  created() {
    this.load()
  },
  methods: {

    load() {
      this.request.get("/user/page/jiluyuan", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          phone: this.phone,
          role: this.role,
          leaderid:this.user.id
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
      this.request.post("/user", this.form).then(res => {
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
        if(row.role=="ROLE_ADMIN"|row.role=="ROLE_DEVELOPER"|row.role=="ROLE_RECORDER"){
            this.$message.error("你无权限修改管理员信息")
        }
        else {
            console.log(row)
            this.form = JSON.parse(JSON.stringify(row))
            this.dialogFormVisible = true
        }
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
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
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.username = ""
      this.phone = ""
      this.role = ""
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
    exp() {
      window.open(`http://${serverIp}:9090/userinfo/export`)
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    handleChangePassword(row) {
      this.formPassword = {}
      this.formPassword.username = row.username
      this.passwordFormVisible = true
    },
    savePassword() {
      this.$refs.pass.validate((valid) => {
        if (valid) {
          if (this.formPassword.newPassword !== this.formPassword.confirmPassword) {
            this.$message.error("2次输入的新密码不相同")
          }
          this.request.post("/user/password/qiang", this.formPassword).then(res => {
            if (res.code === '200') {
              this.$message.success("修改成功")

            } else {
              this.$message.error(res.msg)
            }
          })
          this.passwordFormVisible=false
        }
      })
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
