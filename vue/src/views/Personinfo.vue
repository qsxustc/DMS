<template>
    <div>

        <div style="display:inline-block;margin: 10px">
            <el-card style="width: 500px;">
                <el-form label-width="80px" size="small">
                    <el-upload
                            class="avatar-uploader"
                            :action="'http://' + serverIp +':9090/file/upload'"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                    >
                        <img v-if="form.avatarurl" :src="form.avatarurl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <el-form-item label="用户名">
                        <el-input v-model="form.username" disabled autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="记录员id">
                        <el-input  v-model="form.leaderid" autocomplete="off" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="姓名">
                        <el-input v-model="form.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="年龄">
                        <el-input v-model="form.age" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="驾龄">
                        <el-input v-model="form.driveyear" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="电话">
                        <el-input v-model="form.phone" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="地址">
                        <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="save">确 定</el-button>
                    </el-form-item>
                </el-form>
            </el-card>
        </div>

    </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Person",
  data() {
    return {
      serverIp: serverIp,
      form: {},

      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.getUser().then(res => {
      console.log(res)
      this.form = res
    })
  },
  methods: {
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    save() {
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")

          // 触发父级更新User的方法
          this.$emit("refreshUser")

          // 更新浏览器存储的用户信息
          this.getUser().then(res => {
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res))
          })

        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatarurl = res
      this.save()
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}


</style>
