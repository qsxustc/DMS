<template>

    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入车型" suffix-icon="el-icon-search"
                      v-model="type"></el-input>
            <el-input style="width: 200px" placeholder="请输入责任人工号" suffix-icon="el-icon-message" class="ml-5"
                      v-model="dutyofficerid"></el-input>
            <el-input style="width: 200px" placeholder="请输入车牌号" suffix-icon="el-icon-message" class="ml-5"
                      v-model="licenseid"></el-input>
            <el-input style="width: 200px" placeholder="请输入车辆颜色" suffix-icon="el-icon-message" class="ml-5"
                      v-model="color"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定批量删除这些数据吗？"
                    @confirm="delBatch"
            >
                <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>

        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID"></el-table-column>
            <el-table-column prop="licenseid" label="车牌号"></el-table-column>
            <el-table-column prop="type" label="车型号"></el-table-column>
            <el-table-column prop="color" label="颜色"></el-table-column>
            <el-table-column prop="engineid" label="发动机号码"></el-table-column>
            <el-table-column prop="chassisid" label="底盘号码"></el-table-column>
            <el-table-column prop="dutyofficerid" label="责任人工号"></el-table-column>
            <!--      <el-table-column prop="photo" label="清晰照片信息"></el-table-column>-->
            <el-table-column prop="createtime" label="加入系统时间"></el-table-column>
            <el-table-column prop="remarks" label="备注"></el-table-column>
            <el-table-column label="操作" width="200" align="center">

                <template slot-scope="scope">

                    <el-button type="success" slot="reference" @click="handleEdit(scope.row)"
                    >编辑 <i class="el-icon-edit"></i></el-button>

                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.id)"
                    >
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i>
                        </el-button>
                    </el-popconfirm>

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

        <el-dialog title="车辆信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="100px" size="small">
                <el-form-item label="车型">
                    <el-input v-model="form.type" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="颜色">
                    <el-input v-model="form.color" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="照片" >
                    <!--              <img v-if="form.photo" :src="form.photo" class="avatar"  width="100" height="100">-->
                    <el-upload
                            class="avatar-uploader"
                            :action="'http://' + serverIp +':9090/file/upload'"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                    >
                        <img v-if="form.photo" :src="form.photo" class="avatar" style="width: 140px">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="车牌号">
                    <el-input v-model="form.licenseid" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="发动机号码">
                    <el-input v-model="form.engineid" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="底盘号码">
                    <el-input v-model="form.chassisid" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="责任人工号">
                    <el-input v-model="form.dutyofficerid" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="加入系统时间">
                    <el-input v-model="form.createtime" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remarks" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
    name: "car",
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
            passwordFormVisible: false,
            formPassword: {},
            roles: [],
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
            this.request.get("/car/page", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    licenseid: this.licenseid,
                    color: this.color,
                    type: this.type,
                    dutyofficerid: this.dutyofficerid
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
        handleAvatarSuccess(res) {
            this.form.photo = res
            this.save()
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
            this.dutyofficerid = ""
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
