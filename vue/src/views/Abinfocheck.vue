<template>
    <div>
        <div style="margin: 10px 0;display: flex;">
            <div>
                <el-input style="width: 200px;margin-right: 10px" placeholder="请输入驾驶员姓名"
                          suffix-icon="el-icon-search"
                          v-model="name"></el-input>
                <el-input style="width: 200px;margin-right: 10px" placeholder="请输入车牌号"
                          suffix-icon="el-icon-search"
                          v-model="licenseid"></el-input>
                <el-input style="width: 200px;margin-right: 10px" placeholder="请输入车类型"
                          suffix-icon="el-icon-search"
                          v-model="cartype"></el-input>
                <el-select clearable v-model="abtype" placeholder="请选择异常行为类型" style="margin-right: 10px">
                    <el-option value="bothhandsleavingwheel">双手离开方向盘</el-option>
                    <el-option value="eyesclosed">闭眼</el-option>
                    <el-option value="nofacemask">不带口罩</el-option>
                    <el-option value="notbucklingup">抽烟</el-option>
                    <el-option value="smoke">抽烟</el-option>
                    <el-option value="notfacingfront">不面向前方</el-option>
                    <el-option value="cellphone">打电话</el-option>
                    <el-option value="yawning">打哈欠</el-option>
                    <el-option value="低头">head_lowered</el-option>
                </el-select>
                <el-button class="ml-5" type="primary" @click="load" style="margin-right: 10px">搜索</el-button>
                <el-button type="warning" @click="reset" style="margin-right: 10px">重置</el-button>
            </div>
        </div>
        <div style="margin-bottom: 5px">
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
            <el-button type="warning" @click="reset" style="margin-right: 10px">重置</el-button>
        </div>
        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
        >
            <el-table-column prop="id" label="编号" align="center" width="50"></el-table-column>
            <el-table-column prop="userid" label="驾驶员id" align="center" width="80"></el-table-column>
            <el-table-column prop="name" label="姓名" align="center" width="90"></el-table-column>
            <el-table-column prop="phone" label="电话" style="width: 200px" align="center"
                             width="100"></el-table-column>
            <el-table-column prop="time" label="产生时间" align="center" width="100"></el-table-column>
<!--            <el-table-column prop="abtype" label="类型" style="width: 200px" align="center" width="100">-->
<!--            </el-table-column>-->
            <el-table-column prop="remarks" label="异常信息" align="center" width="150"></el-table-column>
            <el-table-column prop="licenseid" label="车牌号" align="center"></el-table-column>
            <el-table-column prop="cartype" label="车辆类型" align="center" width="80"></el-table-column>
            <el-table-column prop="dutyofficerid" label="责任人工号" align="center" width="100"></el-table-column>
            <el-table-column prop="checkstate" label="审核状态" style="width: 200px" align="center"
                             width="100"></el-table-column>
            <el-table-column prop="checkremark" label="审核批示" align="center" width="150"></el-table-column>

            <el-table-column label="操作" width="100" align="center">
                <template slot-scope="scope">
                    <el-button type="warning" @click="shenpi(scope.row)">审批<i class="el-icon-document"></i>
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

        <el-dialog title="异常行为信息查看" :visible.sync="dialogFormzhongshen" width="50%">
            <el-form label-width="120px" size="small">
                <el-form-item label="编号">
                    <el-input v-model="form.id" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="驾驶员姓名">
                    <el-input v-model="form.name" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="驾驶员照片"
                >
                    <img v-if="form.avatarurl" :src="form.avatarurl" class="avatar" width="100" height="100">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-form-item>
                <el-form-item label="异常行为类型">
                    <el-select clearable v-model="form.abtype" placeholder="请选择类型" style="width: 100%">
                        <el-option value="bothhandsleavingwheel">双手离开方向盘</el-option>
                        <el-option value="eyesclosed">闭眼</el-option>
                        <el-option value="nofacemask">不带口罩</el-option>
                        <el-option value="notbucklingup">抽烟</el-option>
                        <el-option value="smoke">抽烟</el-option>
                        <el-option value="notfacingfront">不面向前方</el-option>
                        <el-option value="cellphone">打电话</el-option>
                        <el-option value="yawning">打哈欠</el-option>
                        <el-option value="低头">head_lowered</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="视频地址">
                    <el-input v-model="form.url" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="车牌号">
                    <el-input v-model="form.licenseid" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="车辆照片" size="mini"
                              label-width="120px">
                    <img v-if="form.photo" :src="form.photo" class="avatar" width="100" height="100">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
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
                <el-form-item label="备注">
                    <el-input v-model="form.remarks" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="异常信息查看">
                    <el-input v-model="form.url" autocomplete="off" :disabled="true"></el-input>
                    <template slot-scope="scope">
                        <el-button type="primary" @click="watchvideo(form.url)">查看</el-button>
                    </template>
                </el-form-item>

                <el-form-item label="审核状态">
                    <div>
                        <el-radio-group v-model="form.checkstate">
                            <el-radio-button label="ACCEPT">
                                <el-tag type="success">通过</el-tag>
                            </el-radio-button>
                            <el-radio-button label="REJECT">
                                <el-tag type="danger">拒绝</el-tag>
                            </el-radio-button>
                            <el-radio-button label="UNCHECKED">
                                <el-tag type="info">待审批</el-tag>
                            </el-radio-button>
                        </el-radio-group>
                    </div>
                </el-form-item>
                <el-form-item label="审核批示">
                    <!--          <el-input v-model="form.putInFirSta" autocomplete="off"></el-input>-->
                    <el-input v-model="form.checkremark" autocomplete="off" type="textarea"></el-input>
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
            videoVisible: false,
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
            datetime: "",
            licenseid: '',
            cartype: '',
            abtype: '',
            dialogFormzhongshen: false,
            multipleSelection: [],
            types: [],
            vis: false,
            jiluyuanid: '',
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
        }
    },
    created() {//ceshi
        this.load()
    },
    methods: {
        load() {
            this.request.get("/abinfo/page/", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                    abtype: this.abtype,
                    licenseid: this.licenseid,
                    cartype: this.cartype,
                    jiluyuanid: this.user.id,
                }
            }).then(res => {
                this.tableData = res.data.records
                this.total = res.data.total

            })

        },
        watchvideo(id) {
            this.playerOptions.sources[0].src = require("../../../mysql" + "/videos/" + id)
            this.videoVisible = true
            console.log(this.form)
        },
        timeload() {
            let start = ""
            let end = ""
            if (this.datetime && this.datetime.length >= 2) {
                start = this.datetime[0]
                end = this.datetime[1]
            }
            this.request.get("/abinfo/time", {
                params: {
                    start: start,
                    end: end,
                    leaderid: this.user.id
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
        del(row) {
            this.request.delete("/resPutIn/" + row.putInId).then(res => {
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
            let ids = this.multipleSelection.map(v => v.putInId)  // [{}, {}, {}] => [1,2,3]
            this.request.post("/resPutIn/del/batch", ids).then(res => {
                if (res.code === '200') {
                    this.$message.success("批量删除成功")
                    this.load()
                } else {
                    this.$message.error("批量删除失败")
                }
            })
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
</style>
