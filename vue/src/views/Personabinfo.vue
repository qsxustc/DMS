<template>
    <div>
        <div>
            <el-card>
                您本周期内的驾驶员行为考核得分为：
                <el-input-number v-model="mask" :disabled="true" :min="0" :max="100" label="描述文字"></el-input-number>
            </el-card>
        </div>
        <div style="margin: 10px 0;display: flex;">
            <div>
                <el-input style="width: 200px;margin-right: 10px" placeholder="请输入车牌号"
                          suffix-icon="el-icon-search"
                          v-model="licenseid"></el-input>
                <el-input style="width: 200px;margin-right: 10px" placeholder="请输入车类型"
                          suffix-icon="el-icon-search"
                          v-model="cartype"></el-input>
            </div>
            <el-button type="primary" @click="load" class="ml-5" style="margin-bottom: 10px;">搜索</el-button>
            <el-button type="warning" @click="reset" style="margin-bottom: 10px;margin-right: 10px">重置</el-button>

            <el-date-picker
                    v-model="datetime"
                    type="datetimerange"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
            </el-date-picker>
            <el-button type="primary" @click="timeload" class="ml-5" style="margin-bottom: 10px">搜索</el-button>
            <el-button type="warning" @click="reset" style="margin-bottom: 10px">重置</el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'">
            <el-table-column prop="id" label="编号" align="center" width="80"></el-table-column>
            <el-table-column prop="time" label="异常行为产生时间" align="center" width="120"></el-table-column>
            <el-table-column prop="abtype" label="异常行为类型" style="width: 200px" align="center" width="100">
            </el-table-column>
            <el-table-column prop="remarks" label="备注信息" align="center"></el-table-column>
            <el-table-column prop="mask" label="扣分" align="center" width="60"></el-table-column>
            <el-table-column prop="licenseid" label="车牌号" align="center" width="100"></el-table-column>
            <el-table-column prop="cartype" label="车辆类型" align="center" width="100"></el-table-column>
            <el-table-column prop="dutyofficerid" label="责任人工号" align="center" width="85"></el-table-column>
            <el-table-column prop="checkstate" label="审核状态" style="width: 100px" align="center"
                             width="90"></el-table-column>
            <el-table-column prop="checkremark" label="审核批示" align="center"></el-table-column>
            <el-table-column label="操作" width="100" align="center">
                <template slot-scope="scope">
                    <el-button type="warning" @click="shenpi(scope.row)">查看<i class="el-icon-document"></i>
                    </el-button>
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
        <!--    初审-->
        <el-dialog title="项目信息" :visible.sync="dialogFormzhongshen" width="50%">
            <el-form label-width="120px" size="small">
                <el-form-item label="驾驶员姓名">
                    <el-input v-model="form.name" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="异常行为类型">
                    <el-input v-model="form.abtype" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="车辆照片">
                    <img v-if="form.photo" :src="form.photo" class="avatar" width="140px">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-form-item>
                <el-form-item label="车牌号">
                    <el-input v-model="form.licenseid" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="视频地址">
                    <el-button type="warning" @click="watchvideo(form.url)" style="margin-bottom: 10px">查看</el-button>
                </el-form-item>
                <el-form-item label="车类型">
                    <el-input v-model="form.cartype" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="form.phone" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="车辆责任人工号">
                    <el-input v-model="form.dutyofficerid" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormzhongshen = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="视频信息" :visible.sync="videoVisible" width="80%">
            <div>
                <div style="display:inline-block;width: 77%;padding: 5px">
                    <el-card>
                        <video-player style="width: 100%;height: 100%;margin:0 auto;box-shadow:5px 5px 8px #888888"
                                      class="video-player vjs-custom-skin"
                                      ref="videoPlayer"
                                      :playsinline="true"
                                      :options="playerOptions"
                        ></video-player>
                    </el-card>
                </div>
                <div style="display:inline-block;width: 22%;padding: 5px">
                    <el-card>
                        <el-form label-width="110px" size="small">
                            <el-form-item label="双手离开方向盘">
                                <el-input v-model="form.bothhandsleavingwheel" disabled autocomplete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="闭眼">
                                <el-input v-model="form.eyesclosed" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <el-form-item label="不带口罩">
                                <el-input v-model="form.nofacemask" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <el-form-item label="长时间不眨眼">
                                <el-input v-model="form.notbucklingup" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <el-form-item label="抽烟">
                                <el-input v-model="form.smoke" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <el-form-item label="不面对屏幕">
                                <el-input v-model="form.notfacingfront" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <el-form-item label="打电话">
                                <el-input v-model="form.cellphone" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <el-form-item label="打哈欠">
                                <el-input v-model="form.yawning" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <el-form-item label="打电话">
                                <el-input v-model="form.headlowered" autocomplete="off" disabled></el-input>
                            </el-form-item>
                            <!--                          <el-form-item>-->
                            <!--                              <el-button type="primary" @click="save">确 定</el-button>-->
                            <!--                          </el-form-item>-->
                        </el-form>
                    </el-card>
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="videoVisible = false">取 消</el-button>
            </div>

        </el-dialog>
    </div>
</template>
<script>
import {serverIp} from "../../public/config";

export default {
    name: "abinfocheck",
    data() {
        return {
            serverIp: serverIp,
            tableData: [],
            total: 0,
            pageNum: 1,
            pageSize: 10,
            form: {},
            name: '',
            datetime: "",
            licenseid: '',
            videoVisible: false,
            cartype: '',
            like: true,
            value1: 4154.564,
            value2: 2222,
            title: '今年的增长',
            abtype: '',
            dialogFormzhongshen: false,
            multipleSelection: [],
            types: [],
            vis: false,
            jiluyuanid: '',
            playerOptions: {
                playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度
                autoplay: false, //如果true,浏览器准备好时开始回放。
                muted: false, // 默认情况下将会消除任何音频。
                loop: false, // 导致视频一结束就重新开始。
                preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
                language: 'zh-CN',
                aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
                fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
                sources: [{
                    type: "video/mp4",
                    src: "" //url地址
                }],
                poster: "", //你的封面地址
                // width: document.documentElement.clientWidth,
                notSupportedMessage: '此视频暂无法播放，请稍后再试', //允许覆盖Video.js无法播放媒体源时显示的默认信息。
                controlBar: {
                    timeDivider: true,
                    durationDisplay: true,
                    remainingTimeDisplay: false,
                    fullscreenToggle: true  //全屏按钮
                }
            },
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
            mask: 0,
        }
    },
    created() {//ceshi
        this.load()
    },
    methods: {
        watchvideo(id) {
            console.log(this.form)
            this.playerOptions.sources[0].src = require("../../../mysql" + "/videos/" + id)
            this.videoVisible = true
        },
        load() {
            this.request.get("/abinfo/driver", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                    abtype: this.abtype,
                    licenseid: this.licenseid,
                    cartype: this.cartype,
                    driverid: this.user.id
                }
            }).then(res => {
                console.log(res.data.records)
                this.tableData = res.data.records
                this.total = res.data.total
                this.mask = this.user.mask
            })

        },
        timeload() {
            let start = ""
            let end = ""
            if (this.datetime && this.datetime.length >= 2) {
                start = this.datetime[0]
                end = this.datetime[1]
            }
            this.request.get("/abinfo/time/driver", {
                params: {
                    start: start,
                    end: end,
                    driverid: this.user.id
                }
            }).then(res => {
                this.tableData = res.data
            })
        },
        save() {
            this.request.post("/abinfo/check", this.form).then(res => {
                if (res.code === '200') {
                    this.$message.success("保存成功")
                    this.dialogFormzhongshen = false
                    this.load()
                } else {
                    this.$message.error("保存失败")
                }
            })
        },
        shenpi(form) {
            if (form.checkstate === 'ACCEPT' || form.checkstate === 'REJECT') {
                this.$alert("已经审批过，无法修改结果")
                this.dialogFormzhongshen = false
                this.form = {}
            } else {
                this.dialogFormzhongshen = true
                this.form = JSON.parse(JSON.stringify(form))
            }
        },
        handleSelectionChange(val) {
            console.log(val)
            this.multipleSelection = val
        },
        reset() {
            this.name = ''
            this.abtype = ''
            this.cartype = ''
            this.licenseid = ''
            this.datetime = ''
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
    }
}
</script>
<style>
.headerBg {
    background: #eee !important;
}

.like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
}
</style>
